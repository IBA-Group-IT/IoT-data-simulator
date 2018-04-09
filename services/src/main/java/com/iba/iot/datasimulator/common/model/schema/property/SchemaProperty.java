package com.iba.iot.datasimulator.common.model.schema.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.SessionSchemaPropertyDeserializer;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadata;

/**
 *
 */
@JsonDeserialize(using = SessionSchemaPropertyDeserializer.class)
public interface SchemaProperty {

    /**
     *
     * @return
     */
    SchemaPropertyType getType();

    /**
     *
     * @return
     */
    SchemaPropertyMetadata getMetadata();

}
