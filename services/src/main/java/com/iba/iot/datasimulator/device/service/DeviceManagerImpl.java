package com.iba.iot.datasimulator.device.service;

import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.common.util.RemovalValidationUtil;
import com.iba.iot.datasimulator.device.dao.DeviceDao;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.Session;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DeviceManagerImpl implements DeviceManager {

    private static final Logger logger = LoggerFactory.getLogger(DeviceManagerImpl.class);

    @Autowired
    private DeviceDao deviceDao;

    @Override
    public Device create(Device device) {

        logger.debug(">>> Creating device: {}", device);
        return deviceDao.save(device);
    }

    @Override
    public Device update(String deviceId, Device device) {

        logger.debug(">>> Updating device {} by value: {}", deviceId, device);
        ModelEntityUtil.setId(device, deviceId);
        return deviceDao.update(device);
    }

    @Override
    public Collection<Device> get(String name, String targetSystem) {

        logger.debug(">>> Getting all devices");
        return ModelEntityUtil.sortByModified(deviceDao.get(name, targetSystem));
    }

    @Override
    public Collection<Device> get(Collection<String> deviceIds) {

        logger.debug(">>> Getting devices by ids {}", deviceIds);
        List<ObjectId> objectIds = new ArrayList<>();

        for (String deviceId: deviceIds) {
            objectIds.add(new ObjectId(deviceId));
        }

        return deviceDao.get(objectIds);
    }

    @Override
    public Device get(String deviceId) {

        logger.debug(">>> Getting device by id: {}", deviceId);
        return deviceDao.get(deviceId);
    }

    @Override
    public void remove(String deviceId) {

        RemovalValidationUtil.checkReferences(deviceId, deviceDao::getLinkedSessions, Session::getName);

        logger.debug(">>> Removing device by id: {}", deviceId);
        deviceDao.remove(deviceId);
    }
}
