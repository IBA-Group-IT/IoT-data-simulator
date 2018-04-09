package com.iba.iot.datasimulator.common.json;

import com.iba.iot.datasimulator.common.model.schema.ArraySchemaModel;
import com.iba.iot.datasimulator.common.model.schema.ObjectSchemaModel;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.SchemaType;

/**
 *
 */
public class SchemaDeserializer extends TypedPolymorphicDeserializer<Schema, SchemaType> {

    @Override
    protected SchemaType parseType(String rawType) {
        return SchemaType.fromString(rawType);
    }

    @Override
    protected Class<? extends Schema> determineConcreteType(SchemaType schemaType) {

        switch (schemaType) {

            case OBJECT:
                return ObjectSchemaModel.class;

            case ARRAY:
                return ArraySchemaModel.class;

            default:
                return null;
        }
    }
}
