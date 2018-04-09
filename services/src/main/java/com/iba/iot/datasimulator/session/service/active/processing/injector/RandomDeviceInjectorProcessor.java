package com.iba.iot.datasimulator.session.service.active.processing.injector;

import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.session.util.DeviceInjectorUtil;
import io.reactivex.Observable;
import org.apache.commons.lang3.RandomUtils;
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
public class RandomDeviceInjectorProcessor implements DeviceInjectionProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(RandomDeviceInjectorProcessor.class);

    /** **/
    private List<Device> devices;

    /**
     *
     * @param devices
     */
    public RandomDeviceInjectorProcessor(Collection<Device> devices) {
        this.devices = new ArrayList<>(devices);
    }

    @Override
    public Observable<ActiveSessionPayload> process(ActiveSessionPayload payload) {

        logger.debug(">>> Processing random devices injection processor for payload {}", payload);

        int randomIndex = RandomUtils.nextInt(0, devices.size());
        ActiveSessionPayload devicePayload = DeviceInjectorUtil.buildDeviceActiveSessionPayload(devices.get(randomIndex), payload);

        return Observable.just(devicePayload);
    }
}
