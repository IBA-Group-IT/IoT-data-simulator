package com.iba.iot.datasimulator.session.service.active.processing.sender.processor;

import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.target.model.TargetSystem;

/**
 *
 */
public interface TargetSystemProcessor {

    /**
     *
     * @param targetSystem
     * @param device
     */
    void process(TargetSystem targetSystem, Device device);

}
