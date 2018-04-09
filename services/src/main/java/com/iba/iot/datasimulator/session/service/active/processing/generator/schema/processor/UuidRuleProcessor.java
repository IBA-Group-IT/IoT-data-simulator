package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor;

import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.UuidSchemaPropertyRule;
import com.iba.iot.datasimulator.common.util.StringUtil;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidRuleProcessor implements RuleProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(UuidRuleProcessor.class);

    @Override
    public SchemaPropertyRuleType getType() {
        return SchemaPropertyRuleType.UUID;
    }

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue, RuleEngineContext context) throws Exception {

        logger.debug(">>> Processing UUID rule for unfolded schema property {} and dataset value {}",
                unfoldedSchemaProperty, datasetEntryValue);

        String position = unfoldedSchemaProperty.getPosition();
        UuidSchemaPropertyRule uuidSchemaPropertyRule = (UuidSchemaPropertyRule) unfoldedSchemaProperty.getProperty().getRule();

        String prefix = uuidSchemaPropertyRule.getPrefix() != null ? uuidSchemaPropertyRule.getPrefix() : StringUtil.EMPTY_STRING;
        String postfix = uuidSchemaPropertyRule.getPostfix() != null ? uuidSchemaPropertyRule.getPostfix() : StringUtil.EMPTY_STRING;

        UUID uuid = UUID.randomUUID();

        context.getProcessingResults().put(position, prefix + uuid.toString() + postfix);
    }
}
