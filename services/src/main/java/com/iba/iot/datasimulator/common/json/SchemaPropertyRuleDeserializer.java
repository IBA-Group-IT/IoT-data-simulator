package com.iba.iot.datasimulator.common.json;

import com.iba.iot.datasimulator.common.model.schema.property.rule.*;
import com.iba.iot.datasimulator.common.model.schema.property.rule.literal.*;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.BooleanRandomSchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.DoubleRandomSchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.IntegerRandomSchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.LongRandomSchemaPropertyRule;

/**
 *
 */
public class SchemaPropertyRuleDeserializer extends TypedPolymorphicDeserializer<SchemaPropertyRule, SchemaPropertyRuleType> {

    @Override
    protected SchemaPropertyRuleType parseType(String rawType) {
        return SchemaPropertyRuleType.fromString(rawType);
    }

    @Override
    protected Class<? extends SchemaPropertyRule> determineConcreteType(SchemaPropertyRuleType ruleType) {

        switch (ruleType) {

            case AS_IS:
                return AsIsSchemaPropertyRule.class;

            case LITERAL_STRING:
                return StringLiteralSchemaPropertyRule.class;

            case LITERAL_LONG:
                return LongLiteralSchemaPropertyRule.class;

            case LITERAL_DOUBLE:
                return DoubleLiteralSchemaPropertyRule.class;

            case LITERAL_BOOLEAN:
                return BooleanLiteralSchemaPropertyRule.class;

            case LITERAL_INTEGER:
                return IntegerLiteralSchemaPropertyRule.class;

            case UUID:
                return UuidSchemaPropertyRule.class;

            case RANDOM_DOUBLE:
                return DoubleRandomSchemaPropertyRule.class;

            case RANDOM_INTEGER:
                return IntegerRandomSchemaPropertyRule.class;

            case RANDOM_LONG:
                return LongRandomSchemaPropertyRule.class;

            case RANDOM_BOOLEAN:
                return BooleanRandomSchemaPropertyRule.class;

            case DEVICE_PROPERTY:
                return DevicePropertySchemaPropertyRule.class;

            case CURRENT_TIME:
                return CurrentTimeSchemaPropertyRule.class;

            case RELATIVE_TIME:
                return RelativeTimeSchemaPropertyRule.class;

            case CUSTOM_FUNCTION:
                return CustomFunctionSchemaPropertyRule.class;

            default:
                return null;
        }
    }
}


