package com.iba.iot.datasimulator.session.util;

import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import org.springframework.beans.BeanUtils;

/**
 *
 */
public class DeviceInjectorUtil {

    /**
     *
     * @param device
     * @param payload
     * @return
     */
    public static ActiveSessionPayload buildDeviceActiveSessionPayload(Device device, ActiveSessionPayload payload) {

        ActiveSessionPayload devicePayload = new ActiveSessionPayload();
        BeanUtils.copyProperties(payload, devicePayload);

        devicePayload.setDevice(device);
        return devicePayload;
    }

}
