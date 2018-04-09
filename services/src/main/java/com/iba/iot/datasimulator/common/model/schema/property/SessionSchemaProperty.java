package com.iba.iot.datasimulator.common.model.schema.property;

import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRule;

/**
 *
 */
public interface SessionSchemaProperty extends SchemaProperty {

    /**
     *
     * @return
     */
    SchemaPropertyRule getRule();

    /**
     *
     * @param rule
     */
    void setRule(SchemaPropertyRule rule);
}
