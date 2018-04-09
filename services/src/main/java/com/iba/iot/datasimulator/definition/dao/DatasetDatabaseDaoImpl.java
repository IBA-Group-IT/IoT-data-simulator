package com.iba.iot.datasimulator.definition.dao;

import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.Dataset;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

@Repository
public class DatasetDatabaseDaoImpl implements DatasetDatabaseDao {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetDatabaseDaoImpl.class);

    @Autowired
    Datastore dataStore;

    @Override
    public Dataset save(Dataset dataset) {

        ModelEntityUtil.updateModified(dataset);
        dataStore.save(dataset);
        return dataset;
    }

    @Override
    public Collection<Dataset> get(String name, String type) {
        Query<Dataset> query = dataStore.createQuery(Dataset.class);

        if (!StringUtils.isEmpty(name)) {
            query.field("name").startsWithIgnoreCase(name);
        }

        if (!StringUtils.isEmpty(type)) {
            query.field("type").startsWithIgnoreCase(type);
        }

        return query.asList();
    }

    @Override
    public Dataset get(String datasetId) {
        return dataStore.get(Dataset.class, new ObjectId(datasetId));
    }

    @Override
    public Dataset update(Dataset dataset) {

        ModelEntityUtil.updateModified(dataset);
        dataStore.save(dataset);
        return dataset;
    }

    @Override
    public void remove(String datasetId) {
        dataStore.delete(Dataset.class, new ObjectId(datasetId));
    }

    @Override
    public List<DataDefinition> getLinkedDataDefinitions(String datasetId) {
        return dataStore.createQuery(DataDefinition.class).field("dataset").equal(new ObjectId(datasetId)).asList();
    }
}
