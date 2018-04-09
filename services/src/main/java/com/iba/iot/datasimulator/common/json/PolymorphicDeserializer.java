package com.iba.iot.datasimulator.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 *
 * @param <T>
 */
public abstract class PolymorphicDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {


        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        Class<? extends T> concreteType = determineType(node);

        return jp.getCodec().treeToValue(node, concreteType);
    }

    /**
     *
     * @param node
     * @return
     */
    protected abstract Class<? extends T> determineType(JsonNode node);
}
