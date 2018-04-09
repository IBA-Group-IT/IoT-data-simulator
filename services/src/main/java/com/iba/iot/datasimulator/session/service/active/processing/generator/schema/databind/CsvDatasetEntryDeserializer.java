package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.databind;

import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CsvDatasetEntryDeserializer implements DatasetEntryDeserializer {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CsvDatasetEntryDeserializer.class);

    @Value("${dataset.csv.separator}")
    private String csvSeparator;

    @Override
    public Map<String, String> deserialize(String datasetEntry, boolean isDatasetProvided) {

        logger.debug(">>> Unfolding csv dataset for entry {} and dataset provided {}", datasetEntry, isDatasetProvided);

        Map<String, String> result = new HashMap<>();

        if (isDatasetProvided) {

            String[] values = datasetEntry.split(csvSeparator);
            result = IntStream.range(0, values.length)
                              .boxed()
                              .collect(
                                  Collectors.toMap(
                                     // CSV columns counting starts from 1
                                     i -> String.valueOf(i + 1),
                                     i -> values[i].trim()
                              ));
        }

        return result;
    }

    @Override
    public SchemaRootMetadataType getType() {
        return SchemaRootMetadataType.CSV;
    }
}
