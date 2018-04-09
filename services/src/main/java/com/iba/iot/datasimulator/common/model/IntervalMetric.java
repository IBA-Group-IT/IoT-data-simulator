package com.iba.iot.datasimulator.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 */
public enum IntervalMetric {

    /** **/
    MILLISECONDS("milliseconds"),

    /** **/
    SECONDS("seconds"),

    /** **/
    MINUTES("minutes"),

    /** **/
    HOURS("hours");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    IntervalMetric(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static IntervalMetric fromString(String raw) {

        for (IntervalMetric type : IntervalMetric.values()) {
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
