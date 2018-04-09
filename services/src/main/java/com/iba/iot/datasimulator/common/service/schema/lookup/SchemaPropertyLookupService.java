package com.iba.iot.datasimulator.common.service.schema.lookup;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;

/**
 *
 */
public interface SchemaPropertyLookupService {

    /**
     *
     * @param position
     * @param schema
     * @return
     */
    SchemaProperty findProperty(String position, Schema schema);
}
