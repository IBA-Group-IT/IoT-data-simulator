package com.iba.iot.datasimulator.session.model.active.injector;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.DeviceInjectorDeserializer;
import com.iba.iot.datasimulator.common.model.RuledEntity;

/**
 *
 */
@JsonDeserialize(using = DeviceInjectorDeserializer.class)
public interface DeviceInjector extends RuledEntity<DeviceInjectionRule> {}
