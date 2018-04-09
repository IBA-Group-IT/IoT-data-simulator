package com.iba.iot.datasimulator.common.model.schema.property.metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.SchemaPropertyMetadataDeserializer;

/**
 *
 */
@JsonDeserialize(using = SchemaPropertyMetadataDeserializer.class)
public interface SchemaPropertyMetadata {

    /**
     *
     * @return
     */
    String getName();

    /**
     *
     * @return
     */
    void setName(String name);

    /**
     *
     * @return
     */
    String getPosition();

    /**
     *
     * @param position
     */
    void setPosition(String position);

    /**
     *
     * @return
     */
    String getDescription();

    /**
     *
     * @param description
     */
    void setDescription(String description);
}
