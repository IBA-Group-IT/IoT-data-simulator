package com.iba.iot.datasimulator.common.factory.schema.property.metadata;

import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadataModel;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataModel;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.TypedSchemaPropertyMetadataModel;

/**
 *
 */
public interface SchemaPropertyMetadataBuilder {

    /**
     *
     * @param position
     * @return
     */
    SchemaPropertyMetadataModel buildMetadata(String position);

    /**
     *
     * @param position
     * @return
     */
    SchemaPropertyMetadataModel buildArrayMetadata(String position);

    /**
     *
     * @param position
     * @return
     */
    SchemaPropertyMetadataModel buildObjectMetadata(String position);

    /**
     *
     * @param position
     * @param type
     * @return
     */
    TypedSchemaPropertyMetadataModel buildTypedMetadata(String position, SchemaPropertyMetadataType type);

    /**
     *
     * @param position
     * @param type
     * @param format
     * @return
     */
    FormattedTypedSchemaPropertyMetadataModel buildTypedFormattedMetadata(String position, SchemaPropertyMetadataType type, String format);
}
