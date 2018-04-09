package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.databind;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.SchemaType;
import com.iba.iot.datasimulator.common.service.json.traverser.JsonPathBuildingTraverser;
import com.iba.iot.datasimulator.session.util.SortUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JsonDatasetEntrySerializer implements DatasetEntrySerializer {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(JsonDatasetEntrySerializer.class);

    @Autowired
    private JsonPathBuildingTraverser jsonTraverser;

    /** **/
    private JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    @Override
    public String serialize(Map<String, Object> processingResults, SchemaType schemaType) {

        JsonNode root = buildRootNode(schemaType);

        processingResults.entrySet()
                         .stream()
                         .sorted(SortUtil.stringKeyComparator)
                         .forEach((entry) -> {

                            String path = entry.getKey();
                            Object value = entry.getValue();
                            jsonTraverser.build(path, value, root);
                         });

        String result = root.toString();
        logger.debug(">>> Json data serialization result: {}", result);

        return result;
    }

    /**
     *
     * @param schemaType
     * @return
     */
    private JsonNode buildRootNode(SchemaType schemaType) {

        if (schemaType == SchemaType.OBJECT) {
            return nodeFactory.objectNode();
        }

        return nodeFactory.arrayNode();
    }

    @Override
    public SchemaRootMetadataType getType() {
        return SchemaRootMetadataType.JSON;
    }
}
