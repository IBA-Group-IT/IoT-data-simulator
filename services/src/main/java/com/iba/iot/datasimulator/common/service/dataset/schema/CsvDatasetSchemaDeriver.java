package com.iba.iot.datasimulator.common.service.dataset.schema;

import com.iba.iot.datasimulator.common.factory.schema.SchemaBuilder;
import com.iba.iot.datasimulator.common.model.schema.ObjectSchemaModel;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.service.dataset.schema.property.SchemaPropertyDeriver;
import com.iba.iot.datasimulator.definition.model.DatasetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class CsvDatasetSchemaDeriver implements DatasetSchemaDeriver {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CsvDatasetSchemaDeriver.class);

    @Value("${dataset.csv.separator}")
    private String csvSeparator;

    @Value("${dataset.csv.schema.property.prefix}")
    private String schemaPropertyNamePrefix;

    @Autowired
    private SchemaBuilder schemaBuilder;

    @Autowired
    private SchemaPropertyDeriver propertyDeriver;

    @Override
    public Schema derive(String datasetEntry) {

        logger.debug(">>> Deriving CSV schema for dataset entry: {}", datasetEntry);

        ObjectSchemaModel schema = schemaBuilder.buildObjectSchema(SchemaRootMetadataType.CSV);

        String[] values = datasetEntry.split(csvSeparator);
        IntStream.range(0, values.length)
                 .boxed()
                 .forEach(index -> {

                     String datasetValue = values[index];
                     String position = Integer.toString(index + 1);
                     SchemaProperty schemaProperty = propertyDeriver.derive(datasetValue, position);
                     schemaProperty.getMetadata().setName(schemaPropertyNamePrefix + position);
                     schema.getProperties().put(position, schemaProperty);

                 });

        logger.debug(">>> CSV dataset entry {} derived schema result: {}", datasetEntry, schema);
        return schema;
    }

    @Override
    public DatasetType getType() {
        return DatasetType.CSV;
    }
}
