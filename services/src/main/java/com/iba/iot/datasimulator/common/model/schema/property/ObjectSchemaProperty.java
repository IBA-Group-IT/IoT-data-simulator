package com.iba.iot.datasimulator.common.model.schema.property;

import java.util.Map;

/**
 *
 */
public interface ObjectSchemaProperty extends SchemaProperty{

    /**
     *
     * @return
     */
    Map<String, SchemaProperty> getProperties();
}
