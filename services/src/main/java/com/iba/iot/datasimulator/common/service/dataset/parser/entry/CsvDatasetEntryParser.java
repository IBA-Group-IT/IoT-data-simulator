package com.iba.iot.datasimulator.common.service.dataset.parser.entry;

import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CsvDatasetEntryParser implements DatasetEntryTypedParser {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CsvDatasetEntryParser.class);

    @Value("${dataset.csv.separator}")
    private String csvDelimiter;

    @Override
    public String getValue(String datasetEntry, String column) {

        String[] columns = datasetEntry.split(csvDelimiter);
        Integer dateColumnIndex = Integer.valueOf(column);

        // It is assumed that csv columns position starts from 1
        String result = columns[dateColumnIndex - 1];

        logger.debug(">>> Dataset entry parser: dataset entry: {}, column: {}, value: {}", datasetEntry, column, result);
        return result;
    }

    @Override
    public SchemaRootMetadataType getType() {
        return SchemaRootMetadataType.CSV;
    }
}
