package com.iba.iot.datasimulator.session.model.active.timer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.TimerDeserializer;
import com.iba.iot.datasimulator.common.model.TypedEntity;

/**
 *
 */
@JsonDeserialize(using = TimerDeserializer.class)
public interface Timer extends TypedEntity<TimerType> {}
