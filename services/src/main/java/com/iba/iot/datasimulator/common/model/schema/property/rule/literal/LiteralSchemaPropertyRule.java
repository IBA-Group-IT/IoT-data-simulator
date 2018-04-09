package com.iba.iot.datasimulator.common.model.schema.property.rule.literal;

import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRule;

/**
 *
 * @param <T>
 */
public interface LiteralSchemaPropertyRule<T> extends SchemaPropertyRule {

    /**
     *
     * @return
     */
    T getValue();
}
