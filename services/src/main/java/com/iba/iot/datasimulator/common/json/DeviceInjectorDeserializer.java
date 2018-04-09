package com.iba.iot.datasimulator.common.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.iba.iot.datasimulator.session.model.active.injector.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DeviceInjectorDeserializer extends PolymorphicDeserializer<DeviceInjector> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DeviceInjectorDeserializer.class);

    /** **/
    public static final String RULE_PROPERTY = "rule";

    @Override
    protected Class<? extends DeviceInjector> determineType(JsonNode node) {

        DeviceInjectionRule deviceInjectionRule = DeviceInjectionRule.fromString(node.get(RULE_PROPERTY).asText());

        if (deviceInjectionRule != null) {

            switch (deviceInjectionRule) {

                case ALL:
                    return AllDevicesInjector.class;

                case SPECIFIC:
                    return SpecificDeviceInjector.class;

                case RANDOM:
                    return RandomDeviceInjector.class;

                case ROUND_ROBIN:
                    return RoundRobinDeviceInjector.class;
            }

        }

        logger.error("Couldn't detect device injector type for json {}", node);
        throw new RuntimeException("Unknown device injector type.");
    }
}
