package com.iba.iot.datasimulator.device.util;

import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.device.model.property.DeviceProperty;

import java.util.Collection;
import java.util.Optional;

/**
 *
 */
public class DeviceUtil {

    /**
     *
     * @param device
     * @param propertyName
     * @return
     */
    public static Optional<DeviceProperty> findProperty(Device device, String propertyName) {


        Collection<DeviceProperty> properties = device.getProperties();
        if (properties != null && properties.size() > 0) {

            return properties.stream()
                             .filter(property -> property.getName().equalsIgnoreCase(propertyName))
                             .findFirst();
        }

        return Optional.empty();
    }

}
