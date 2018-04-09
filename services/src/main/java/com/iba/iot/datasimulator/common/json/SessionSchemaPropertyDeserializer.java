package com.iba.iot.datasimulator.common.json;

import com.iba.iot.datasimulator.common.model.schema.property.*;

/**
 *
 */
public class SessionSchemaPropertyDeserializer extends TypedPolymorphicDeserializer<SchemaProperty, SchemaPropertyType> {

    @Override
    protected SchemaPropertyType parseType(String rawType) {
        return SchemaPropertyType.fromString(rawType);
    }

    @Override
    protected Class<? extends SchemaProperty> determineConcreteType(SchemaPropertyType schemaPropertyType) {

        switch (schemaPropertyType) {

            case OBJECT:
                return SessionObjectSchemaPropertyModel.class;

            case ARRAY:
                return SessionArraySchemaPropertyModel.class;

            default:
                return SessionSchemaPropertyModel.class;
        }
    }
}
