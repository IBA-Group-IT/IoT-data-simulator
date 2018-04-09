package com.iba.iot.datasimulator.session.model.active.generator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.GeneratorDeserializer;
import com.iba.iot.datasimulator.common.model.TypedEntity;

@JsonDeserialize(using = GeneratorDeserializer.class)
public interface Generator extends TypedEntity<GeneratorType> {}
