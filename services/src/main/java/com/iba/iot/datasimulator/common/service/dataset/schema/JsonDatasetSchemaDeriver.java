package com.iba.iot.datasimulator.common.service.dataset.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iba.iot.datasimulator.common.factory.schema.SchemaBuilder;
import com.iba.iot.datasimulator.common.factory.schema.property.SchemaPropertyBuilder;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.ArraySchemaPropertyModel;
import com.iba.iot.datasimulator.common.model.schema.property.ObjectSchemaPropertyModel;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.service.dataset.schema.property.SchemaPropertyDeriver;
import com.iba.iot.datasimulator.common.util.JsonParseUtil;
import com.iba.iot.datasimulator.common.util.SchemaUtil;
import com.iba.iot.datasimulator.definition.model.DatasetType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Service
public class JsonDatasetSchemaDeriver implements DatasetSchemaDeriver {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(JsonDatasetSchemaDeriver.class);

    @Autowired
    private SchemaBuilder schemaBuilder;

    @Autowired
    private SchemaPropertyBuilder propertyBuilder;

    @Autowired
    private SchemaPropertyDeriver propertyDeriver;

    @Override
    public Schema derive(String datasetEntry) throws IOException {

        logger.debug(">>> Deriving Json schema for dataset entry: {}", datasetEntry);

        final JsonNode root = JsonParseUtil.parseDatasetEntryJson(datasetEntry);
        Schema schema = buildSchemaContainer(root);

        SchemaUtil.processSchema(schema,
                (objectSchema) -> {

                    Map<String, SchemaProperty> properties = processObjectNode((ObjectNode) root, StringUtils.EMPTY)
                            .getProperties();
                    objectSchema.getProperties().putAll(properties);

                },
                (arraySchema) -> {

                    Collection<SchemaProperty> items = processArrayNode((ArrayNode) root, StringUtils.EMPTY).getItems();
                    arraySchema.getItems().addAll(items);
                });

        logger.debug(">>> JSON dataset entry {} derived schema result: {}", datasetEntry, schema);
        return schema;
    }

    /**
     *
     * @param root
     * @return
     */
    private Schema buildSchemaContainer(JsonNode root) {

        if (root.isObject()) {
            return schemaBuilder.buildObjectSchema(SchemaRootMetadataType.JSON);
        } else if (root.isArray()) {
            return schemaBuilder.buildArraySchema(SchemaRootMetadataType.JSON);
        }

        logger.error(">>> Cannot derive schema from not container json node {}", root);
        throw new RuntimeException("JSON schema deriving error: unsupported json root node.");
    }

    /**
     *
     * @param objectNode
     * @param position
     * @return
     */
    private ObjectSchemaPropertyModel processObjectNode(ObjectNode objectNode, String position) {

        logger.debug(">>> Building object schema property for json node {}", objectNode.asText());
        ObjectSchemaPropertyModel objectProperty = propertyBuilder.buildObjectProperty(position);

        Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
        while (fields.hasNext()) {

            Map.Entry<String, JsonNode> fieldEntry = fields.next();
            String childName = fieldEntry.getKey();
            JsonNode childNode = fieldEntry.getValue();
            SchemaProperty childSchemaProperty = processChild(childNode, childName);
            objectProperty.getProperties().put(childName, childSchemaProperty);
        }

        return objectProperty;
    }

    /**
     *
     * @param arrayNode
     * @param position
     * @return
     */
    private ArraySchemaPropertyModel processArrayNode(ArrayNode arrayNode, String position) {

        logger.debug(">>> Building array schema property for json node {}", arrayNode.asText());
        ArraySchemaPropertyModel arrayProperty = propertyBuilder.buildArrayProperty(position);

        // array elements positions starts from 1
        int index = 1;
        Iterator<JsonNode> elements = arrayNode.elements();
        while (elements.hasNext()) {

            JsonNode childNode = elements.next();
            SchemaProperty childSchemaProperty = processChild(childNode, Integer.toString(index));
            arrayProperty.getItems().add(childSchemaProperty);
            index++;
        }

        return arrayProperty;
    }

    /**
     *
     * @param jsonNode
     * @param position
     * @return
     */
    private SchemaProperty processPrimitiveNode(JsonNode jsonNode, String position) {

        logger.debug(">>> Building primitive schema property for json node {}", jsonNode.asText());
        return propertyDeriver.derive(jsonNode, position);
    }

    /**
     *
     * @param childNode
     * @param position
     * @return
     */
    private SchemaProperty processChild(JsonNode childNode, String position) {

        JsonNodeType nodeType = childNode.getNodeType();
        switch (nodeType) {

            case OBJECT:
                return processObjectNode((ObjectNode) childNode, position);

            case ARRAY:
                return processArrayNode((ArrayNode) childNode, position);

            default:
                return processPrimitiveNode(childNode, position);

        }
    }

    @Override
    public DatasetType getType() {
        return DatasetType.JSON;
    }
}
