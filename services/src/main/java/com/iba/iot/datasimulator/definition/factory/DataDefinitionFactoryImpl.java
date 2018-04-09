package com.iba.iot.datasimulator.definition.factory;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.definition.dao.DatasetDatabaseDao;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.DataDefinitionCreateUpdateRequest;
import com.iba.iot.datasimulator.definition.model.Dataset;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataDefinitionFactoryImpl implements DataDefinitionFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DataDefinitionFactoryImpl.class);

    @Autowired
    private DatasetDatabaseDao datasetDatabaseDao;

    @Override
    public DataDefinition buildFromCreateUpdateRequest(DataDefinitionCreateUpdateRequest dataDefinitionCreateUpdateRequest, String dataDefinitionId) {

        DataDefinition dataDefinition = new DataDefinition();

        if (StringUtils.isNotBlank(dataDefinitionId)){
            dataDefinition.setId(new ObjectId(dataDefinitionId));
        }

        dataDefinition.setName(dataDefinitionCreateUpdateRequest.getName());

        String datasetId = dataDefinitionCreateUpdateRequest.getDatasetId();
        processDataset(dataDefinition, datasetId);

        Schema schema = dataDefinitionCreateUpdateRequest.getSchema();
        dataDefinition.setSchema(schema);

        return dataDefinition;
    }


    /**
     *
     * @param dataDefinition
     * @param datasetId
     */
    private void processDataset(DataDefinition dataDefinition, String datasetId) {

        if (StringUtils.isNotBlank(datasetId)) {

            if (ObjectId.isValid(datasetId)) {

                Dataset dataset = datasetDatabaseDao.get(datasetId);
                dataDefinition.setDataset(dataset);

            } else {

                logger.error(">>> Bad dataset id format provided: {}", datasetId);
                throw new IllegalArgumentException("Wrong dataset id provided.");
            }
        }
    }
}
