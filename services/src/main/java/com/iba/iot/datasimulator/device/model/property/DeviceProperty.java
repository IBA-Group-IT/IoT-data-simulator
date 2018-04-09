package com.iba.iot.datasimulator.device.model.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.DevicePropertyDeserializer;

@JsonDeserialize(using = DevicePropertyDeserializer.class)
public interface DeviceProperty<T> {

    /**
     *
     * @return
     */
    String getName();

    /**
     *
     * @return
     */
    T getValue();
}
