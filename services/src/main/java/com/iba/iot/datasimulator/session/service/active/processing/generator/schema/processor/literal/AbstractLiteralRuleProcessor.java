package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.literal;

import com.iba.iot.datasimulator.common.model.schema.property.rule.literal.LiteralSchemaPropertyRule;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.RuleProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class AbstractLiteralRuleProcessor implements RuleProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(AbstractLiteralRuleProcessor.class);

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue, RuleEngineContext context) throws Exception {

        logger.debug(">>> Processing literal rule for unfolded schema property: {}", unfoldedSchemaProperty);

        LiteralSchemaPropertyRule literalRule = (LiteralSchemaPropertyRule) unfoldedSchemaProperty.getProperty().getRule();
        String position = unfoldedSchemaProperty.getPosition();
        context.getProcessingResults().put(position, literalRule.getValue());
    }
}
