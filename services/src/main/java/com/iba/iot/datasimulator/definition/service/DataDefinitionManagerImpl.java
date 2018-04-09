package com.iba.iot.datasimulator.definition.service;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.description.SchemaPropertyDescription;
import com.iba.iot.datasimulator.common.service.schema.manager.SchemaManager;
import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.common.util.RemovalValidationUtil;
import com.iba.iot.datasimulator.definition.dao.DataDefinitionDao;
import com.iba.iot.datasimulator.definition.factory.DataDefinitionFactory;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.DataDefinitionCreateUpdateRequest;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.service.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
public class DataDefinitionManagerImpl implements DataDefinitionManager {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DataDefinitionManagerImpl.class);

    @Autowired
    private DataDefinitionFactory dataDefinitionFactory;

    @Autowired
    private DataDefinitionDao dataDefinitionDao;

    @Autowired
    private SchemaManager schemaManager;

    @Autowired
    private SessionManager sessionManager;

    @Override
    public DataDefinition create(DataDefinitionCreateUpdateRequest dataDefinitionCreateUpdateRequest) {

        logger.debug(">>> Creating new data definition: {}", dataDefinitionCreateUpdateRequest);

        DataDefinition dataDefinition = dataDefinitionFactory.buildFromCreateUpdateRequest(dataDefinitionCreateUpdateRequest, null);
        return dataDefinitionDao.save(dataDefinition);
    }

    @Override
    public DataDefinition create(DataDefinition dataDefinition) {
        logger.debug(">>> Creating data definition: {}", dataDefinition);
        return dataDefinitionDao.save(dataDefinition);
    }

    @Override
    public Collection<DataDefinition> get() {

        logger.debug(">>> Getting all data definitions.");
        return ModelEntityUtil.sortByModified(dataDefinitionDao.get());
    }

    @Override
    public DataDefinition get(String dataDefinitionId) {

        logger.debug(">>> Getting data definition by id: {}", dataDefinitionId);
        return dataDefinitionDao.get(dataDefinitionId);
    }

    @Override
    public void remove(String dataDefinitionId) {

        logger.debug(">>> Removing data definition by id: {}", dataDefinitionId);
        DataDefinition dataDefinition = dataDefinitionDao.get(dataDefinitionId);
        validate(dataDefinitionId, dataDefinition);
        RemovalValidationUtil.checkReferences(dataDefinitionId, dataDefinitionDao::getLinkedSessions, Session::getName);

        dataDefinitionDao.remove(dataDefinitionId);
    }

    @Override
    public Schema populateSchemaDefaultProcessingRules(String dataDefinitionId) throws IOException {

        logger.debug(">>> Populating data definition {} schema by default processing rules.", dataDefinitionId);
        DataDefinition dataDefinition = dataDefinitionDao.get(dataDefinitionId);
        validate(dataDefinitionId, dataDefinition);

        return schemaManager.populateSchemaDefaultRules(dataDefinition);
    }

    @Override
    public DataDefinition update(String dataDefinitionId, DataDefinitionCreateUpdateRequest dataDefinitionCreateUpdateRequest) throws IOException {

        DataDefinition dataDefinition = dataDefinitionFactory.buildFromCreateUpdateRequest(dataDefinitionCreateUpdateRequest, dataDefinitionId);
        dataDefinitionDao.update(dataDefinition);
        if (dataDefinition.getSchema() != null) {
            sessionManager.updateSessionsSchema(dataDefinition);
        }

        return dataDefinition;
    }

    @Override
    public Collection<SchemaPropertyDescription> getSchemaPropertiesDescription(String dataDefinitionId) {

        logger.debug(">>> Getting data definition {} schema properties description", dataDefinitionId);

        DataDefinition dataDefinition = dataDefinitionDao.get(dataDefinitionId);
        validate(dataDefinitionId, dataDefinition);

        return schemaManager.getPropertiesDescription(dataDefinition.getSchema());
    }

    /**
     *
     * @param dataDefinitionId
     * @param dataDefinition
     */
    private void validate(String dataDefinitionId, DataDefinition dataDefinition) {

        if (dataDefinition == null) {

            logger.error(">>> Cannot fetch data definition entity by id {}", dataDefinitionId);
            throw new IllegalArgumentException("Wrong data definition id provided");
        }
    }
}
