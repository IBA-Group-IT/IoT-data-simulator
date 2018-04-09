package com.iba.iot.datasimulator.session.factory.injector;


import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.service.active.processing.injector.DeviceInjectionProcessor;

/**
 *
 */
public interface DeviceInjectionProcessorFactory {

    /**
     *
     *
     * @param session@return
     */
    DeviceInjectionProcessor build(Session session);
}
