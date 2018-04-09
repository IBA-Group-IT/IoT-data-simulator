package com.iba.iot.datasimulator.session.service.active.processing.timer;

import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;

/**
 *
 */
public interface DatePropertyValidator {

    /**
     *
     * @param datePropertyName
     * @param dateSchemaProperty
     */
    void validate(String datePropertyName, SchemaProperty dateSchemaProperty);
}
