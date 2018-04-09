package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.databind;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.util.StringUtil;
import com.iba.iot.datasimulator.common.util.JsonParseUtil;
import com.iba.iot.datasimulator.common.util.ParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class JsonDatasetEntryDeserializer implements DatasetEntryDeserializer {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(JsonDatasetEntryDeserializer.class);

    @Value("${session.processing.json.path.delimiter}")
    private String jsonPathDelimiter;

    @Override
    public Map<String, String> deserialize(String datasetEntry, boolean isDatasetProvided) throws IOException {

        logger.debug(">>> Unfolding json dataset for entry {} and dataset provided {}", datasetEntry, isDatasetProvided);

        Map<String, String> result = new HashMap<>();

        if (isDatasetProvided) {

            JsonNode jsonNode = JsonParseUtil.parseDatasetEntryJson(datasetEntry);
            parseJson(jsonNode, result, StringUtil.EMPTY_STRING);
        }

        return result;
    }

    /**
     *
     * @param jsonNode
     * @param result
     */
    void parseJson(JsonNode jsonNode,  Map<String, String> result, String currentPosition) {

        JsonNodeType nodeType = jsonNode.getNodeType();
        switch (nodeType) {

            case OBJECT:
                result.put(currentPosition, jsonNode.toString());
                processObjectNode((ObjectNode) jsonNode, result, currentPosition);
                break;

            case ARRAY:
                result.put(currentPosition, jsonNode.toString());
                processArrayNode((ArrayNode) jsonNode, result, currentPosition);
                break;

            default:
                result.put(currentPosition, jsonNode.asText());
        }
    }

    /**
     *
     * @param objectNode
     * @param result
     * @param currentPosition
     */
    void processObjectNode(ObjectNode objectNode, Map<String, String> result, String currentPosition) {

        Iterator<Map.Entry<String, JsonNode>> children = objectNode.fields();
        while(children.hasNext()) {

            Map.Entry<String, JsonNode> next = children.next();
            String propertyName = next.getKey();
            JsonNode child = next.getValue();

            parseJson(child, result, getPosition(propertyName, currentPosition));
        }
    }

    /**
     *
     * @param arrayNode
     * @param result
     * @param currentPosition
     */
    void processArrayNode(ArrayNode arrayNode, Map<String, String> result, String currentPosition) {

        Iterator<JsonNode> children = arrayNode.elements();

        // Array index counting starts from 1
        int index = 1;
        while(children.hasNext()) {

            JsonNode child = children.next();
            parseJson(child, result, ParseUtil.buildArrayPath(currentPosition, index));

            index++;
        }
    }

    /**
     *
     * @param propertyName
     * @param parentPosition
     * @return
     */
    private String getPosition(String propertyName, String parentPosition) {

        if (StringUtils.isEmpty(parentPosition)) {
            return propertyName;
        } else {
            return parentPosition + jsonPathDelimiter + propertyName;
        }
    }

    @Override
    public SchemaRootMetadataType getType() {
        return SchemaRootMetadataType.JSON;
    }
}
