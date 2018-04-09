package com.iba.iot.datasimulator.common.model.schema.property;

import java.util.Collection;

/**
 *
 */
public interface ArraySchemaProperty extends SchemaProperty {

    /**
     *
     * @return
     */
    Collection<SchemaProperty> getItems();
}
