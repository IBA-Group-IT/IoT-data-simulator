package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.random;


import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.IntegerRandomSchemaPropertyRule;
import com.iba.iot.datasimulator.common.util.MathUtil;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.RuleProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IntegerRandomRuleProcessor implements RuleProcessor{

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(IntegerRandomRuleProcessor.class);

    @Override
    public SchemaPropertyRuleType getType() {
        return SchemaPropertyRuleType.RANDOM_INTEGER;
    }

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue, RuleEngineContext context) throws Exception {

        logger.debug(">>> Processing random integer rule for unfolded schema property: {}", unfoldedSchemaProperty);

        String position = unfoldedSchemaProperty.getPosition();

        IntegerRandomSchemaPropertyRule integerRandomRule = (IntegerRandomSchemaPropertyRule) unfoldedSchemaProperty.getProperty().getRule();
        int randomValue = MathUtil.randomInt(integerRandomRule.getMin(), integerRandomRule.getMax());

        context.getProcessingResults().put(position, randomValue);
    }
}
