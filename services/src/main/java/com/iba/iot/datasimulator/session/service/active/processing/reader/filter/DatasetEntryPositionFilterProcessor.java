package com.iba.iot.datasimulator.session.service.active.processing.reader.filter;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.service.dataset.parser.entry.DatasetEntryParser;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetEntryPositionFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DatasetEntryPositionFilterProcessor implements DatasetFilterProcessor<String> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetEntryPositionFilterProcessor.class);

    @Autowired
    private DatasetEntryParser datasetEntryParser;

    /** **/
    private String datasetEntryPosition;

    /** **/
    private String expectedDatasetEntryValue;

    /** **/
    private Schema schema;

    /**
     *
     * @param datasetEntryPositionFilter
     */
    public DatasetEntryPositionFilterProcessor(DatasetEntryPositionFilter datasetEntryPositionFilter, Schema schema) {

        this.datasetEntryPosition = datasetEntryPositionFilter.getPosition();
        this.expectedDatasetEntryValue = datasetEntryPositionFilter.getValue();
        this.schema = schema;
    }

    @Override
    public boolean filter(String datasetEntry) {

        logger.debug(">>> Processing dataset entry position filter for dataset entry: {}", datasetEntry);

        SchemaRootMetadataType schemaType = schema.getMetadata().getType();
        String datasetEntryValue = getDatasetEntryValue(datasetEntry, schemaType);

        if (StringUtils.isNotBlank(datasetEntryValue)) {

            if (StringUtils.isNotBlank(expectedDatasetEntryValue)){
                return expectedDatasetEntryValue.equalsIgnoreCase(datasetEntryValue);
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param datasetEntry
     * @param schemaType
     * @return
     */
    private String getDatasetEntryValue(String datasetEntry, SchemaRootMetadataType schemaType) {

        try {

            return datasetEntryParser.getValue(datasetEntry, datasetEntryPosition, schemaType);

        } catch (IOException exception) {

            logger.error(">>> An error occured during dataset entry value parsing: " + exception.getMessage(), exception);
            throw new RuntimeException("Dataset entry parsing exception.");
        }
    }
}
