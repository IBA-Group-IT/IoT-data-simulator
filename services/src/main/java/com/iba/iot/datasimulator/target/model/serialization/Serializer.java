package com.iba.iot.datasimulator.target.model.serialization;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.SerializerDeserializer;
import com.iba.iot.datasimulator.common.model.TypedEntity;

import java.io.Serializable;

/**
 *
 */
@JsonDeserialize(using = SerializerDeserializer.class)
public interface Serializer  extends TypedEntity<SerializerType>, Serializable {}
