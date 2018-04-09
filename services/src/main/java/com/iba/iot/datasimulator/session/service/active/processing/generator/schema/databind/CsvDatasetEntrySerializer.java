package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.databind;

import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.SchemaType;
import com.iba.iot.datasimulator.common.util.StringUtil;
import com.iba.iot.datasimulator.session.util.SortUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CsvDatasetEntrySerializer implements DatasetEntrySerializer {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CsvDatasetEntrySerializer.class);

    @Value("${dataset.csv.separator}")
    private String csvSeparator;

    @Override
    public String serialize(Map<String, Object> processingResults, SchemaType schemaType) {

        List<String> sortedValues = processingResults.entrySet()
                .stream()
                .sorted(SortUtil.stringKeyComparator)
                .map(Map.Entry::getValue)
                .map(value -> Objects.nonNull(value) ? value.toString() : StringUtil.EMPTY_STRING)
                .collect(Collectors.toList());

        String result = String.join(csvSeparator, sortedValues);
        logger.debug(">>> Cvs data serialization result: {}", result);

        return result;
    }

    @Override
    public SchemaRootMetadataType getType() {
        return SchemaRootMetadataType.CSV;
    }
}
