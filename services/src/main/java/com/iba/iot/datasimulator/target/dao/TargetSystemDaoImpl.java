package com.iba.iot.datasimulator.target.dao;

import com.iba.iot.datasimulator.common.constant.Constants;
import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class TargetSystemDaoImpl implements TargetSystemDao {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(TargetSystemDaoImpl.class);
    
    @Autowired
    Datastore dataStore;

    @Override
    public TargetSystem save(TargetSystem targetSystem) {

        ModelEntityUtil.updateModified(targetSystem);
        dataStore.save(targetSystem);
        return targetSystem;
    }

    @Override
    public Collection<TargetSystem> get() {
        return dataStore.createQuery(TargetSystem.class).asList();
    }

    @Override
    public TargetSystem get(String targetId) {
        return dataStore.find(TargetSystem.class).disableValidation().field(Constants.ID_FIELD).equal(new ObjectId(targetId)).get();
    }

    @Override
    public TargetSystem update(TargetSystem targetSystem) {

        ModelEntityUtil.updateModified(targetSystem);
        dataStore.save(targetSystem);
        return targetSystem;
    }

    @Override
    public void remove(String targetId) {

        Query<TargetSystem> deleteQuery = dataStore.createQuery(TargetSystem.class)
                                                   .disableValidation()
                                                   .field(Constants.ID_FIELD)
                                                   .equal(new ObjectId(targetId));

        dataStore.delete(deleteQuery);
    }

    @Override
    public List<Session> getLinkedSessions(String targetId) {
        return dataStore.createQuery(Session.class).field("targetSystem").equal(new ObjectId(targetId)).asList();
    }
}
