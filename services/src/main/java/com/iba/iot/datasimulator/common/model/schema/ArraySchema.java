package com.iba.iot.datasimulator.common.model.schema;

import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;

import java.util.Collection;

/**
 *
 */
public interface ArraySchema extends Schema {

    /**
     *
     * @return
     */
    Collection<SchemaProperty> getItems();
}
