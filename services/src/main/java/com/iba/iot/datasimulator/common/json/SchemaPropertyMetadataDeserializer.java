package com.iba.iot.datasimulator.common.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadataModel;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataModel;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.TypedSchemaPropertyMetadataModel;

/**
 *
 */
public class SchemaPropertyMetadataDeserializer extends PolymorphicDeserializer<SchemaPropertyMetadata> {

    /** **/
    public static final String FORMATTED_TYPED_PROPERTY_METADATA_UNIQUE_PROPERTY = "format";

    /** **/
    public static final String TYPED_PROPERTY_METADATA_UNIQUE_PROPERTY = "type";

    /**
     *
     * @param node
     * @return
     */
    protected Class<? extends SchemaPropertyMetadata> determineType(JsonNode node) {

        if (node.has(FORMATTED_TYPED_PROPERTY_METADATA_UNIQUE_PROPERTY)) {
            return FormattedTypedSchemaPropertyMetadataModel.class;
        } else if (node.has(TYPED_PROPERTY_METADATA_UNIQUE_PROPERTY)) {
            return TypedSchemaPropertyMetadataModel.class;
        }

        return SchemaPropertyMetadataModel.class;
    }
}
