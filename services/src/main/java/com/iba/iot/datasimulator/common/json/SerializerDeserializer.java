package com.iba.iot.datasimulator.common.json;

import com.iba.iot.datasimulator.target.model.serialization.ProtobufSerializer;
import com.iba.iot.datasimulator.target.model.serialization.Serializer;
import com.iba.iot.datasimulator.target.model.serialization.SerializerType;

/**
 *
 */
public class SerializerDeserializer extends TypedPolymorphicDeserializer<Serializer, SerializerType> {

    @Override
    protected SerializerType parseType(String rawType) {
        return SerializerType.fromString(rawType);
    }

    @Override
    protected Class<? extends Serializer> determineConcreteType(SerializerType serializerType) {

        switch (serializerType) {

            case PROTOBUF:
                return ProtobufSerializer.class;

            default:
                return null;
        }
    }
}
