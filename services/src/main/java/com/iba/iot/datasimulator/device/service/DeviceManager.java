package com.iba.iot.datasimulator.device.service;

import com.iba.iot.datasimulator.device.model.Device;

import java.util.Collection;

/**
 *
 */
public interface DeviceManager {

    /**
     *
     * @param device
     */
    Device create(Device device);

    /**
     *
     * @param deviceId
     * @param device
     * @return
     */
    Device update(String deviceId, Device device);

    /**
     *
     * @param name
     * @param targetSystem
     * @return
     */
    Collection<Device> get(String name, String targetSystem);

    /**
     *
     * @param deviceIds
     * @return
     */
    Collection<Device> get(Collection<String> deviceIds);

    /**
     *
     * @param deviceId
     * @return
     */
    Device get(String deviceId);

    /**
     *
     * @param deviceId
     */
    void remove(String deviceId);
}
