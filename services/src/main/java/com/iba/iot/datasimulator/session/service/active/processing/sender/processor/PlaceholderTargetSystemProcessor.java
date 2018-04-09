package com.iba.iot.datasimulator.session.service.active.processing.sender.processor;

import com.iba.iot.datasimulator.common.util.PojoUtil;
import com.iba.iot.datasimulator.common.util.StringUtil;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.device.model.property.DeviceProperty;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Order(value = 2)
public class PlaceholderTargetSystemProcessor implements TargetSystemProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(PlaceholderTargetSystemProcessor.class);

    @Value("${session.processing.target.system.placeholder.properties}")
    private String configuredPlaceholderProperties;

    /** **/
    private List<String> placeholderProperties;

    /** **/
    private Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{\\w+}");

    /** **/
    private final int PROPERTY_START_INDEX = 2;

    @PostConstruct
    private void init(){

        placeholderProperties = Arrays.asList(configuredPlaceholderProperties.split(StringUtil.COMMA));
        logger.info(">>> Registered target system placeholder properties: {}", placeholderProperties);
    }

    @Override
    public void process(TargetSystem targetSystem, Device device) {
        placeholderProperties.forEach(targetSystemPropertyName ->
                processPlaceholderProperty(targetSystemPropertyName, targetSystem, device));
    }

    /**
     *
     * @param targetSystemPropertyName
     * @param targetSystem
     * @param device
     */
    private void processPlaceholderProperty(String targetSystemPropertyName, TargetSystem targetSystem, Device device) {

        String property = PojoUtil.getProperty(targetSystemPropertyName, targetSystem);
        if (property != null) {
            handlePlaceholders(targetSystemPropertyName, property, targetSystem, device);
        }
    }

    /**
     *
     * @param targetSystemPropertyName
     * @param propertyValue
     * @param targetSystem
     * @param device
     */
    public void handlePlaceholders(String targetSystemPropertyName, String propertyValue, TargetSystem targetSystem, Device device) {

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(propertyValue);
        Map<String, String> processedPlaceholders = new HashMap<>();

        while (matcher.find()) {

            String rawPlaceholder = matcher.group();

            // Placeholder form in string: ${PLACEHOLDER}
            String placeholder = rawPlaceholder.substring(PROPERTY_START_INDEX, rawPlaceholder.length() - 1);
            String placeholderValue = getPlaceholderValue(device, placeholder);
            processedPlaceholders.put(placeholder, placeholderValue);
        }

        if (!processedPlaceholders.isEmpty()) {
            String processedPropertyValue = StringUtil.replacePlaceholders(propertyValue, processedPlaceholders);
            PojoUtil.setProperty(targetSystemPropertyName, processedPropertyValue, targetSystem);
        }
    }

    /**
     *
     * @param device
     * @param placeholderProperty
     * @return
     */
    private String getPlaceholderValue(Device device, String placeholderProperty) {

        Collection<DeviceProperty> properties = device.getProperties();
        DeviceProperty deviceProperty = properties.stream()
                .filter(property -> property.getName().equals(placeholderProperty))
                .findFirst()
                .orElse(null);

        if (deviceProperty != null) {
            return deviceProperty.getValue().toString();
        }

        logger.error(">>> Cannot get device property {} for placeholder processing for device {}", placeholderProperty, device);
        throw new RuntimeException("${" + placeholderProperty + "} placeholder processing error.");
    }
}
