package com.iba.iot.datasimulator.session.model.active.timer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TimerType {

    /** **/
    INTERVAL("interval"),

    /** **/
    DATASET_PROVIDED("dataset_provided"),

    /** **/
    RANDOM("random"),

    /** **/
    CUSTOM_FUNCTION("custom_function");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    TimerType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static TimerType fromString(String raw) {

        for (TimerType type : TimerType.values()) {
            if (type.value.equalsIgnoreCase(raw)) {
                return type;
            }
        }

        return null;
    }

    /**
     *
     * @return
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

}
