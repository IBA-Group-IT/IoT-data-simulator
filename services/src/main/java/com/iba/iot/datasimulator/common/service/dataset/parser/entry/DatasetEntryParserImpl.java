package com.iba.iot.datasimulator.common.service.dataset.parser.entry;

import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class DatasetEntryParserImpl implements DatasetEntryParser {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetEntryParserImpl.class);

    @Autowired
    private Map<SchemaRootMetadataType, DatasetEntryTypedParser> datasetEntryParsers;

    @Override
    public String getValue(String datasetEntry, String position, SchemaRootMetadataType schemaType) throws IOException {

        logger.debug(">>> Parsing dataset entry {} for position {} and schema type {}", datasetEntry, position, schemaType);

        if (datasetEntryParsers.containsKey(schemaType)) {
            String value =  datasetEntryParsers.get(schemaType).getValue(datasetEntry, position);
            if (StringUtils.isNotEmpty(value)) {
                return value;
            }
        }

        logger.error(">>> Cannot find date field by position {} in dataset entry {}", position, datasetEntry);
        throw new RuntimeException("Cannot get specific value from dataset entry.");
    }
}
