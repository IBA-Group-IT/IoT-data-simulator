package com.iba.iot.datasimulator.session.service.active.processing.injector;

import com.iba.iot.datasimulator.common.service.dataset.parser.entry.DatasetEntryParser;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.device.util.DeviceUtil;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.session.model.active.injector.SpecificDeviceInjector;
import com.iba.iot.datasimulator.session.util.DeviceInjectorUtil;
import com.iba.iot.datasimulator.session.util.SessionUtil;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SpecificDeviceInjectorProcessor implements DeviceInjectionProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SpecificDeviceInjectorProcessor.class);

    /** **/
    private List<Device> devices;

    /** **/
    private SpecificDeviceInjector deviceInjector;

    /** **/
    private Session session;

    @Autowired
    private DatasetEntryParser datasetEntryParser;

    /**
     *
     * @param devices
     * @param deviceInjector
     */
    public SpecificDeviceInjectorProcessor(List<Device> devices, SpecificDeviceInjector deviceInjector, Session session) {
        this.devices = devices;
        this.deviceInjector = deviceInjector;
        this.session = session;
    }

    @Override
    public Observable<ActiveSessionPayload> process(ActiveSessionPayload payload) throws IOException {

        logger.debug(">>> Processing specific devices injection processor for payload {}, and injector model {}", payload, deviceInjector);

        validate(payload);

        String datasetValue = getDatasetValue(payload);
        Device device = findDevice(datasetValue);
        if (device !=  null) {

            logger.debug(">>> Processing payload with device {}", device);
            return Observable.just(DeviceInjectorUtil.buildDeviceActiveSessionPayload(device, payload));

        } else {

            logger.warn(">>> Couldn't find device for specific device injector {} and dataset value {}", deviceInjector, datasetValue);
            return Observable.empty();
        }
    }

    /**
     *
     * @param payload
     */
    private void validate(ActiveSessionPayload payload) {

        if (!payload.isDatasetProvided()) {

            logger.error(">>> Cannot process specific device injection processor for session {} without attached dataset.", SessionUtil.getId(session));
            throw new RuntimeException("Specific device injector processing error");
        }
    }

    /**
     *
     * @param payload
     * @return
     * @throws IOException
     */
    private String getDatasetValue(ActiveSessionPayload payload) throws IOException {

        String datasetPosition = deviceInjector.getDatasetPosition();
        return datasetEntryParser.getValue(payload.getDatasetEntry(), datasetPosition, SessionUtil.getSchemaType(session));
    }

    /**
     *
     * @param datasetValue
     * @return
     */
    private Device findDevice(String datasetValue) {

        String devicePropertyName = deviceInjector.getDeviceProperty();
        return devices.stream()
                      .filter(device ->

                              DeviceUtil.findProperty(device, devicePropertyName)
                                        .flatMap(property -> Optional.of(property.getValue().toString()))
                                        .filter(value -> value.equalsIgnoreCase(datasetValue))
                                        .isPresent()
                      )
                      .findFirst()
                      .orElse(null);
    }
}
