package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor;

import com.iba.iot.datasimulator.common.model.schema.property.rule.DevicePropertySchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DevicePropertyRuleProcessor implements RuleProcessor{

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DevicePropertyRuleProcessor.class);

    @Override
    public SchemaPropertyRuleType getType() {
        return SchemaPropertyRuleType.DEVICE_PROPERTY;
    }

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue, RuleEngineContext context) throws Exception {

        logger.debug(">>> Processing device property rule for unfolded schema property: {}", unfoldedSchemaProperty);

        String position = unfoldedSchemaProperty.getPosition();

        DevicePropertySchemaPropertyRule devicePropertyRule = (DevicePropertySchemaPropertyRule) unfoldedSchemaProperty.getProperty().getRule();
        String devicePropertyName = devicePropertyRule.getPropertyName();

        Object devicePropertyValue = device.getProperties()
                .stream()
                .filter((property) -> property.getName().equalsIgnoreCase(devicePropertyName))
                .findFirst()
                .flatMap(deviceProperty -> Optional.of(deviceProperty.getValue()))
                .orElse(null);

        context.getProcessingResults().put(position, devicePropertyValue);
    }
}
