package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.random;

import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.BooleanRandomSchemaPropertyRule;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.RuleProcessor;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BooleanRandomRuleProcessor implements RuleProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(BooleanRandomRuleProcessor.class);

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue,
                        RuleEngineContext context) throws Exception {

        logger.debug(">>> Processing random boolean rule for unfolded schema property: {}", unfoldedSchemaProperty);

        String position = unfoldedSchemaProperty.getPosition();
        BooleanRandomSchemaPropertyRule booleanRandomRule = (BooleanRandomSchemaPropertyRule) unfoldedSchemaProperty.getProperty().getRule();

        double successProbability = booleanRandomRule.getSuccessProbability();
        boolean result = successProbability >= RandomUtils.nextDouble(0, 1);
        context.getProcessingResults().put(position, result);
    }

    @Override
    public SchemaPropertyRuleType getType() {
        return SchemaPropertyRuleType.RANDOM_BOOLEAN;
    }
}
