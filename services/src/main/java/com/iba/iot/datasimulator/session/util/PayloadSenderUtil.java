package com.iba.iot.datasimulator.session.util;

import com.iba.iot.datasimulator.common.model.TargetSystemType;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.target.model.TargetSystem;

import java.util.Optional;

public class PayloadSenderUtil {

    /**
     *
     * @param device
     * @param type
     * @return
     */
    public static Optional<TargetSystem> findDeviceTargetSystem(Device device, TargetSystemType type) {

        if (device == null || device.getTargetSystems() == null) {
            return Optional.empty();
        }

        return device.getTargetSystems()
                     .stream()
                     .filter(targetSystem -> targetSystem.getType() == type)
                     .findFirst();
    }
}
