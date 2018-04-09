package com.iba.iot.datasimulator.device.dao;

import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.Session;
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
public class DeviceDaoImpl implements DeviceDao {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DeviceDaoImpl.class);

    @Autowired
    Datastore dataStore;

    @Override
    public Device save(Device device) {

        ModelEntityUtil.updateModified(device);
        dataStore.save(device);
        return device;
    }

    @Override
    public Collection<Device> get(String name, String targetSystem) {

        Query<Device> query = dataStore.createQuery(Device.class);

        if (!StringUtils.isEmpty(name)) {
            query.field("name").startsWithIgnoreCase(name);
        }

        if (!StringUtils.isEmpty(targetSystem)) {
            query.field("targetSystems.type").startsWithIgnoreCase(targetSystem);
        }

        return query.asList();
    }

    @Override
    public Collection<Device> get(Collection<ObjectId> devices) {

        return dataStore.get(Device.class, devices).asList();
    }

    @Override
    public Device get(String deviceId) {
        return dataStore.get(Device.class, new ObjectId(deviceId));
    }

    @Override
    public Device update(Device device) {

        ModelEntityUtil.updateModified(device);
        dataStore.save(device);
        return device;
    }

    @Override
    public void remove(String deviceId) {
        dataStore.delete(Device.class, new ObjectId(deviceId));
    }

    @Override
    public List<Session> getLinkedSessions(String targetId) {
        return dataStore.createQuery(Session.class).field("devices").hasThisOne(new ObjectId(targetId)).asList();
    }
}
