package com.iba.iot.datasimulator.common.model.schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.SchemaDeserializer;
import com.iba.iot.datasimulator.common.model.TypedEntity;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;

/**
 *
 */
@JsonDeserialize(using = SchemaDeserializer.class)
public interface Schema extends TypedEntity<SchemaType> {

    /**
     *
     * @return
     */
    String getSchemaKey();

    /**
     *
     * @return
     */
    SchemaRootMetadata getMetadata();

}
