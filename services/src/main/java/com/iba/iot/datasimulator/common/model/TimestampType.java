package com.iba.iot.datasimulator.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TimestampType {

    /** **/
    SECONDS("seconds"),

    /** **/
    MILLISECONDS("milliseconds");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    TimestampType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static TimestampType fromString(String raw) {

        for (TimestampType type : TimestampType.values()) {
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
