package com.iba.iot.datasimulator.common.json;


import com.fasterxml.jackson.databind.JsonNode;
import com.iba.iot.datasimulator.device.model.property.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DevicePropertyDeserializer extends PolymorphicDeserializer<DeviceProperty> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DevicePropertyDeserializer.class);

    /** **/
    public static final String DEVICE_VALUE_PROPERTY = "value";

    /**
     *
     * @param node
     * @return
     */
    protected Class<? extends DeviceProperty> determineType(JsonNode node) {

        JsonNode valueNode = node.get(DEVICE_VALUE_PROPERTY);
        if (valueNode != null) {

            if (valueNode.isNull() || valueNode.isTextual()) {
                return StringDeviceProperty.class;
            } else if (valueNode.isBoolean()) {
                return BooleanDeviceProperty.class;
            } else if (valueNode.isDouble()) {
                return DoubleDeviceProperty.class;
            } else if (valueNode.isInt()) {
                return IntegerDeviceProperty.class;
            } else if (valueNode.isLong()) {
                return LongDeviceProperty.class;
            }

        }

        logger.error("Couldn't detect device property type for json {}", node);
        throw new RuntimeException("Unknown device property type.");
    }
}
