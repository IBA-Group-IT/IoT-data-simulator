package com.iba.iot.datasimulator.session.service.active.processing.sender.processor;

import com.iba.iot.datasimulator.common.model.security.Security;
import com.iba.iot.datasimulator.common.util.PojoUtil;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.service.active.processing.sender.PayloadSenderImpl;
import com.iba.iot.datasimulator.session.util.PayloadSenderUtil;
import com.iba.iot.datasimulator.target.model.Header;
import com.iba.iot.datasimulator.target.model.KafkaTargetSystem;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import com.iba.iot.datasimulator.target.model.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Order(value = 1)
public class TargetSystemPropertiesProcessor implements TargetSystemProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(PayloadSenderImpl.class);

    @Override
    public void process(TargetSystem targetSystem, Device device) {

        PayloadSenderUtil.findDeviceTargetSystem(device, targetSystem.getType())
                .ifPresent(deviceTargetSystem -> {

                    logger.debug(">>> Device {}: ", device);
                    logger.debug(">>> Overriding target system props by device specific {}", deviceTargetSystem);

                    PojoUtil.copyTargetSystemProperties(deviceTargetSystem, targetSystem);

                    processSecurity(targetSystem, deviceTargetSystem);
                    processHeaders(targetSystem, deviceTargetSystem);
                    processSerializers(targetSystem, deviceTargetSystem);
                });
    }

    /**
     *
     * @param targetSystem
     * @param deviceTargetSystem
     */
    private void processSecurity(TargetSystem targetSystem, TargetSystem deviceTargetSystem) {

        Security deviceSecurity = deviceTargetSystem.getSecurity();
        Security targetSecurity = targetSystem.getSecurity();

        if (deviceSecurity != null) {

            if (targetSecurity == null) {

                logger.debug(">>> Setting device security {} to target system {}", deviceSecurity, targetSystem);
                targetSystem.setSecurity(deviceSecurity);

            } else if (targetSecurity.getType() == deviceSecurity.getType()) {

                logger.debug(">>> Overriding target system security {} props by device security props", targetSystem, deviceSecurity);
                PojoUtil.copyProperties(deviceSecurity, targetSecurity);
            }
        }
    }

    /**
     *
     * @param targetSystem
     * @param deviceTargetSystem
     */
    private void processHeaders(TargetSystem targetSystem, TargetSystem deviceTargetSystem) {

        Collection<Header> deviceTargetSystemHeaders = deviceTargetSystem.getHeaders();
        Collection<Header> targetSystemHeaders = targetSystem.getHeaders();

        if (deviceTargetSystemHeaders != null) {

            if (targetSystemHeaders == null) {

                logger.debug(">>> Setting device headers {} to target system {}", deviceTargetSystemHeaders, targetSystem);
                targetSystem.setHeaders(deviceTargetSystemHeaders);

            } else {

                logger.debug(">>> Adding device headers {} to target system {}", deviceTargetSystemHeaders, targetSystem);
                targetSystem.setHeaders(mergeHeaders(deviceTargetSystemHeaders, targetSystemHeaders));
            }
        }
    }

    /**
     *
     * @param deviceTargetSystemHeaders
     * @param targetSystemHeaders
     * @return
     */
    private Collection<Header> mergeHeaders(Collection<Header> deviceTargetSystemHeaders, Collection<Header> targetSystemHeaders) {

        Map<String, Header> deviceHeaders = convert(deviceTargetSystemHeaders);
        Map<String, Header> targetHeaders = convert(targetSystemHeaders);

        Set<String> allHeaderNames = new HashSet<>(deviceHeaders.keySet());
        allHeaderNames.addAll(targetHeaders.keySet());

        return allHeaderNames.stream()
                             .map(headerName -> {

                                 if (deviceHeaders.containsKey(headerName)) {
                                     return deviceHeaders.get(headerName);
                                 } else {
                                    return targetHeaders.get(headerName);
                                 }
                             })
                             .collect(Collectors.toList());
    }

    /**
     *
     * @param headers
     * @return
     */
    private Map<String, Header> convert(Collection<Header> headers) {

        return headers.stream()
                      .collect(Collectors.toMap(
                        Header::getName,
                        Function.identity()
                      ));
    }

    /**
     *
     * @param targetSystem
     * @param deviceTargetSystem
     */
    private void processSerializers(TargetSystem targetSystem, TargetSystem deviceTargetSystem) {

        Serializer messageSerializer = targetSystem.getMessageSerializer();
        Serializer deviceMessageSerializer = deviceTargetSystem.getMessageSerializer();

        if (deviceMessageSerializer != null &&  messageSerializer != null) {
            PojoUtil.copyProperties(deviceMessageSerializer, messageSerializer);
        } else if (deviceMessageSerializer != null) {
            targetSystem.setMessageSerializer(deviceMessageSerializer);
        }

        if (targetSystem instanceof KafkaTargetSystem && deviceTargetSystem instanceof KafkaTargetSystem) {
            processKeySerializers((KafkaTargetSystem) targetSystem, (KafkaTargetSystem) deviceTargetSystem);
        }
    }

    /**
     *
     * @param targetSystem
     * @param deviceTargetSystem
     */
    private void processKeySerializers(KafkaTargetSystem targetSystem, KafkaTargetSystem deviceTargetSystem) {

        Serializer keySerializer = targetSystem.getKeySerializer();
        Serializer deviceKeySerializer = deviceTargetSystem.getKeySerializer();

        if (keySerializer != null && deviceKeySerializer != null) {
            PojoUtil.copyProperties(deviceKeySerializer, keySerializer);
        } else if (deviceKeySerializer != null) {
            targetSystem.setKeySerializer(deviceKeySerializer);
        }
    }
}
