package com.iba.iot.datasimulator.session.service.active.processing.injector;

import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.session.model.active.injector.AllDevicesInjector;
import com.iba.iot.datasimulator.session.util.DeviceInjectorUtil;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AllDevicesInjectorProcessor implements DeviceInjectionProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(AllDevicesInjectorProcessor.class);

    /** **/
    private Collection<Device> devices;

    /** **/
    private AllDevicesInjector injector;

    /**
     *
     * @param devices
     * @param injector
     */
    public AllDevicesInjectorProcessor(Collection<Device> devices, AllDevicesInjector injector) {

        this.injector = injector;
        this.devices = devices;
    }

    @Override
    public Observable<ActiveSessionPayload> process(ActiveSessionPayload payload) {

        logger.debug(">>> Processing all devices injection processor for payload {} and injector {}", payload, injector);

        LinkedList<ActiveSessionPayload> devicesPayload =
        devices.stream()
               .map((Device device) -> DeviceInjectorUtil.buildDeviceActiveSessionPayload(device, payload))
               .collect(Collectors.toCollection(LinkedList::new));

        final long delay = injector.getDelay();
        if (delay > 0 && devicesPayload.size() > 1) {

            return  Observable.concat(Observable.just(devicesPayload.poll()),
                                      Observable.fromIterable(devicesPayload).concatMap(devicePayload ->
                                             Observable.just(devicePayload).delay(delay, TimeUnit.MILLISECONDS)));

        } else {
            return Observable.fromIterable(devicesPayload);
        }
    }
}
