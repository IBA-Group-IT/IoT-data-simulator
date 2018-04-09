package com.iba.iot.datasimulator.session.factory.injector;

import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.injector.DeviceInjectionRule;
import com.iba.iot.datasimulator.session.model.active.injector.DeviceInjector;
import com.iba.iot.datasimulator.session.service.active.processing.injector.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DeviceInjectionProcessorFactoryImpl implements DeviceInjectionProcessorFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DeviceInjectionProcessorFactoryImpl.class);

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public DeviceInjectionProcessor build(Session session) {

        DeviceInjector deviceInjector = session.getDeviceInjector();
        Collection<Device> devices = session.getDevices();

        if (deviceInjector == null || devices == null || devices.isEmpty()) {
            return beanFactory.getBean(EmptyDeviceInjectorProcessor.class);
        }

        DeviceInjectionRule rule = deviceInjector.getRule();
        switch (rule) {

            case ALL:
                return beanFactory.getBean(AllDevicesInjectorProcessor.class, devices, deviceInjector);

            case SPECIFIC:
                return beanFactory.getBean(SpecificDeviceInjectorProcessor.class, devices, deviceInjector, session);

            case RANDOM:
                return beanFactory.getBean(RandomDeviceInjectorProcessor.class, devices);

            case ROUND_ROBIN:
                return beanFactory.getBean(RoundRobinDevicesInjectorProcessor.class, devices);
        }

        logger.error(">>> Cannot create active session with unsupported device injection rule {}", rule);
        throw new RuntimeException("Unsupported device injector type.");
    }
}
