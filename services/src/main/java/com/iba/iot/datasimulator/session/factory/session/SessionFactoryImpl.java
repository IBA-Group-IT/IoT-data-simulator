package com.iba.iot.datasimulator.session.factory.session;

import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.service.DataDefinitionManager;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.device.service.DeviceManager;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.SessionCreateUpdateRequest;
import com.iba.iot.datasimulator.session.model.active.injector.DeviceInjector;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import com.iba.iot.datasimulator.target.service.TargetSystemManager;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SessionFactoryImpl implements SessionFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionFactoryImpl.class);

    @Autowired
    DataDefinitionManager dataDefinitionManager;

    @Autowired
    TargetSystemManager targetSystemManager;

    @Autowired
    DeviceManager deviceManager;

    @Override
    public Session buildFromCreateUpdateRequest(SessionCreateUpdateRequest sessionCreateUpdateRequest, String sessionId) {

        Session session = new Session();

        if (StringUtils.isNotBlank(sessionId)) {
            session.setId(new ObjectId(sessionId));
        }

        session.setName(sessionCreateUpdateRequest.getName());
        session.setGenerator(sessionCreateUpdateRequest.getGenerator());
        session.setTimer(sessionCreateUpdateRequest.getTimer());
        session.setDatasetFilter(sessionCreateUpdateRequest.getDatasetFilter());
        session.setTicksNumber(sessionCreateUpdateRequest.getTicksNumber());
        session.setReplayLooped(sessionCreateUpdateRequest.isReplayLooped());

        String definitionId = sessionCreateUpdateRequest.getDataDefinitionId();
        if (StringUtils.isNotBlank(definitionId)) {
            DataDefinition dataDefinition = dataDefinitionManager.get(definitionId);
            session.setDataDefinition(dataDefinition);
        }

        String targetSystemId = sessionCreateUpdateRequest.getTargetSystemId();
        TargetSystem targetSystem = targetSystemManager.get(targetSystemId);
        if (targetSystem != null) {
            session.setTargetSystem(targetSystem);
        } else {
            logger.error(">>> Cannot find target system by provided id: {}", targetSystemId);
            throw new IllegalArgumentException("Wrong target system id provided.");
        }

        Collection<String> deviceIds = sessionCreateUpdateRequest.getDevices();
        if (deviceIds != null && deviceIds.size() > 0) {
            Collection<Device> devices = deviceManager.get(deviceIds);
            if (devices.size() > 0) {
                session.setDevices(devices);
            }
        }

        DeviceInjector deviceInjector = sessionCreateUpdateRequest.getDeviceInjector();
        session.setDeviceInjector(deviceInjector);

        return session;
    }
}
