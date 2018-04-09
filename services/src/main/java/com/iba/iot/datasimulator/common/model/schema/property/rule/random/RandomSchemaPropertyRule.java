package com.iba.iot.datasimulator.common.model.schema.property.rule.random;

import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRule;

/**
 *
 * @param <T>
 */
public interface RandomSchemaPropertyRule<T extends Number> extends SchemaPropertyRule {

    /**
     *
     * @return
     */
    T getMin();

    /**
     *
     * @return
     */
    T getMax();
}
