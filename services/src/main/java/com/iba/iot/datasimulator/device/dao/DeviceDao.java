package com.iba.iot.datasimulator.device.dao;

import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.Session;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface DeviceDao {

    /**
     *
     * @param device
     */
    Device save(Device device);

    /**
     *
     * @param name
     * @param targetSystem
     * @return
     */
    Collection<Device> get(String name, String targetSystem);

    /**
     *
     * @param devices
     * @return
     */
    Collection<Device> get(Collection<ObjectId> devices);

    /**
     *
     * @param deviceId
     * @return
     */
    Device get(String deviceId);

    /**
     *
     * @param device
     * @return
     */
    Device update(Device device);

    /**
     *
     * @param deviceId
     */
    void remove(String deviceId);

    /**
     *
     * @param deviceId
     * @return
     */
    List<Session> getLinkedSessions(String deviceId);
}
