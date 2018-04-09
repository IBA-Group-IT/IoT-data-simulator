package com.iba.iot.datasimulator.common.model.security;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.SecurityDeserializer;
import com.iba.iot.datasimulator.common.model.TypedEntity;

import java.io.Serializable;

/**
 *
 */
@JsonDeserialize(using = SecurityDeserializer.class)
public interface Security extends TypedEntity<SecurityType>, Serializable {}
