package com.iba.iot.datasimulator.definition.dao;

import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.session.model.Session;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class DataDefinitionDaoImpl implements DataDefinitionDao {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DataDefinitionDaoImpl.class);

    @Autowired
    private Datastore dataStore;

    @Override
    public DataDefinition save(DataDefinition dataDefinition) {

        ModelEntityUtil.updateModified(dataDefinition);
        dataStore.save(dataDefinition);
        return dataDefinition;
    }

    @Override
    public Collection<DataDefinition> get() {
        return dataStore.createQuery(DataDefinition.class).asList();
    }

    @Override
    public DataDefinition get(String dataDefinitionId) {
        return dataStore.get(DataDefinition.class, new ObjectId(dataDefinitionId));
    }

    @Override
    public void remove(String dataDefinitionId) {
        dataStore.delete(DataDefinition.class, new ObjectId(dataDefinitionId));
    }

    @Override
    public int getDatasetReferencesCounter(String datasetId) {
        return dataStore.createQuery(DataDefinition.class).field("dataset").equal(new ObjectId(datasetId)).asList().size();
    }

    @Override
    public DataDefinition update(DataDefinition dataDefinition) {

        ModelEntityUtil.updateModified(dataDefinition);
        dataStore.save(dataDefinition);
        return dataDefinition;
    }

    @Override
    public List<Session> getLinkedSessions(String dataDefinitionId) {
        return dataStore.createQuery(Session.class).field("dataDefinition").equal(new ObjectId(dataDefinitionId)).asList();
    }
}
