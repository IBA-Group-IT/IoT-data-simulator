package com.iba.iot.datasimulator.common.service.dataset.parser.entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.service.json.traverser.JsonPathBuildingTraverser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonDatasetEntryParser implements DatasetEntryTypedParser {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CsvDatasetEntryParser.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JsonPathBuildingTraverser jsonTraverser;

    @Override
    public String getValue(String datasetEntry, String jsonPath) throws IOException {

        JsonNode jsonNode = mapper.readTree(datasetEntry);
        String result = jsonTraverser.get(jsonPath, jsonNode);

        logger.debug(">>> Dataset entry parser: dataset entry: {}, json path: {}, value: {}", datasetEntry, jsonNode, result);
        return result;
    }

    @Override
    public SchemaRootMetadataType getType() {
        return SchemaRootMetadataType.JSON;
    }
}
