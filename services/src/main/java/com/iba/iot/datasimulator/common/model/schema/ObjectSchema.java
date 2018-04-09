package com.iba.iot.datasimulator.common.model.schema;

import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;

import java.util.Map;

/**
 *
 */
public interface ObjectSchema extends Schema {

    /**
     *
     * @return
     */
    Map<String, SchemaProperty> getProperties();
}
