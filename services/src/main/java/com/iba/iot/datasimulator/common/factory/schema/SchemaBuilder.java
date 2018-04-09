package com.iba.iot.datasimulator.common.factory.schema;

import com.iba.iot.datasimulator.common.model.schema.ArraySchemaModel;
import com.iba.iot.datasimulator.common.model.schema.ObjectSchemaModel;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;

/**
 *
 */
public interface SchemaBuilder {

    /**
     *
     * @param type
     * @return
     */
    ArraySchemaModel buildArraySchema(SchemaRootMetadataType type);

    /**
     *
     * @param type
     * @return
     */
    ObjectSchemaModel buildObjectSchema(SchemaRootMetadataType type);

}
