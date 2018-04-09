package com.iba.iot.datasimulator.common.json;

import com.iba.iot.datasimulator.session.model.active.timer.*;

/**
 *
 */
public class TimerDeserializer extends TypedPolymorphicDeserializer<Timer, TimerType> {

    @Override
    protected TimerType parseType(String rawType) {
        return TimerType.fromString(rawType);
    }

    @Override
    protected Class<? extends Timer> determineConcreteType(TimerType timerType) {
        switch (timerType) {

            case INTERVAL:
                return IntervalTimer.class;

            case DATASET_PROVIDED:
                return DatasetTimer.class;

            case RANDOM:
                return RandomTimer.class;

            case CUSTOM_FUNCTION:
                return CustomFunctionTimer.class;

            default:
                return null;
        }
    }
}
