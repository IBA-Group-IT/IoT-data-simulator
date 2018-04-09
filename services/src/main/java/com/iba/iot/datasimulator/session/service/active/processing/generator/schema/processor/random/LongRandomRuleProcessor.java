package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.random;

import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.LongRandomSchemaPropertyRule;
import com.iba.iot.datasimulator.common.util.MathUtil;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.RuleProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LongRandomRuleProcessor implements RuleProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(LongRandomRuleProcessor.class);

    @Override
    public SchemaPropertyRuleType getType() {
        return SchemaPropertyRuleType.RANDOM_LONG;
    }

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue, RuleEngineContext context) throws Exception {

        logger.debug(">>> Processing random long rule for unfolded schema property: {}", unfoldedSchemaProperty);

        String position = unfoldedSchemaProperty.getPosition();

        LongRandomSchemaPropertyRule longRandomRule = (LongRandomSchemaPropertyRule) unfoldedSchemaProperty.getProperty().getRule();
        long randomValue = MathUtil.randomLong(longRandomRule.getMin(), longRandomRule.getMax());

        context.getProcessingResults().put(position, randomValue);
    }
}