package com.iba.iot.datasimulator.common.service.schema.manager;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.description.SchemaPropertyDescription;
import com.iba.iot.datasimulator.common.service.dataset.schema.DatasetSchemaDeriver;
import com.iba.iot.datasimulator.common.service.schema.manager.rule.SchemaDefaultRulesInjector;
import com.iba.iot.datasimulator.common.service.schema.parser.SchemaParser;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.definition.model.DatasetType;
import com.iba.iot.datasimulator.session.factory.reader.DatasetReaderFactory;
import com.iba.iot.datasimulator.session.service.active.processing.reader.DatasetReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 *
 */
@Service
public class SchemaManagerImpl implements SchemaManager {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaManagerImpl.class);

    @Autowired
    private SchemaParser<SchemaPropertyDescription> schemaParser;

    @Autowired
    private DatasetReaderFactory datasetReaderFactory;

    @Autowired
    private Map<DatasetType, DatasetSchemaDeriver> schemaDerivers;

    @Autowired
    private SchemaDefaultRulesInjector rulesInjector;

    @Override
    public Collection<SchemaPropertyDescription> getPropertiesDescription(Schema schema) {

        logger.debug(">>> Getting properties description for schema {}", schema);
        return schemaParser.parse(schema);
    }

    @Override
    public Schema deriveSchema(Dataset dataset) throws IOException {

        logger.debug(">>> Deriving schema for dataset {}", dataset);
        DatasetReader<String> datasetReader = datasetReaderFactory.buildStringDatasetReader(dataset);

        if (datasetReader.hasNext()) {

            DatasetType type = dataset.getType();
            if (schemaDerivers.containsKey(type)) {

                String datasetEntry = datasetReader.next();
                return schemaDerivers.get(type).derive(datasetEntry);
            }

            logger.error(">>> Cannot derive schema for dataset of type {}", type);
            throw new RuntimeException("Dataset type unsupported schema deriving error.");
        }

        logger.error(">>> Cannot derive schema for empty dataset {}", dataset);
        throw new RuntimeException("Dataset schema deriving error.");
    }

    @Override
    public Schema populateSchemaDefaultRules(DataDefinition dataDefinition) throws IOException {

        logger.debug(">>> Populating schema by default rules for data definition {}", dataDefinition);

        Dataset dataset = dataDefinition.getDataset();
        Schema schema = dataDefinition.getSchema();
        if (dataset != null) {
            return rulesInjector.injectDefaultRules(schema, true);
        } else {
            return rulesInjector.injectDefaultRules(schema, false);
        }
    }
}
