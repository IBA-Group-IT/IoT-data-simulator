package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.literal;

import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import org.springframework.stereotype.Component;

@Component
public class StringLiteralRuleProcessor extends AbstractLiteralRuleProcessor {

    @Override
    public SchemaPropertyRuleType getType() {
        return SchemaPropertyRuleType.LITERAL_STRING;
    }
}
