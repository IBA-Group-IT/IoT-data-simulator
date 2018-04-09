package com.iba.iot.datasimulator.session.service.active.processing.injector;

import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.session.util.DeviceInjectorUtil;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RoundRobinDevicesInjectorProcessor implements DeviceInjectionProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(RoundRobinDevicesInjectorProcessor.class);

    /** **/
    private List<Device> devices;

    /** **/
    private int currentDeviceIndex;

    /**
     *
     * @param devices
     */
    public RoundRobinDevicesInjectorProcessor(Collection<Device> devices) {
        this.devices = new ArrayList<>(devices);
    }

    @Override
    public Observable<ActiveSessionPayload> process(ActiveSessionPayload payload) {

        logger.debug(">>> Processing round robin devices injection processor for payload {} and device index {}", payload, currentDeviceIndex);

        ActiveSessionPayload devicePayload = DeviceInjectorUtil.buildDeviceActiveSessionPayload(devices.get(currentDeviceIndex), payload);
        incrementIndex();

        return Observable.just(devicePayload);
    }

    /**
     *
     */
    private void incrementIndex() {

        currentDeviceIndex++;
        if (currentDeviceIndex >= devices.size()) {
            currentDeviceIndex = 0;
        }
    }
}
