package com.iba.iot.datasimulator.session.dao;

import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.session.model.Session;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class SessionDaoImpl implements SessionDao {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionDaoImpl.class);

    @Autowired
    Datastore dataStore;

    @Override
    public Session save(Session session) {

        ModelEntityUtil.updateModified(session);
        dataStore.save(session);
        return session;
    }

    @Override
    public Collection<Session> get() {
        return dataStore.createQuery(Session.class).asList();
    }

    @Override
    public Session get(String sessionId) {
        return dataStore.get(Session.class, new ObjectId(sessionId));
    }

    @Override
    public Session update(Session session) {

        ModelEntityUtil.updateModified(session);
        dataStore.save(session);
        return session;
    }

    @Override
    public void remove(String sessionId) {
        dataStore.delete(Session.class, new ObjectId(sessionId));
    }

    @Override
    public Collection<Session> getByDataDefinitionId(String dataDefinitionId) {
        return dataStore.createQuery(Session.class).field("dataDefinition").equal(new ObjectId(dataDefinitionId)).asList();
    }
}
