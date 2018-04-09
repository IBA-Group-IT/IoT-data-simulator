package com.iba.iot.datasimulator.common.model.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SecurityType {

    /** **/
    CREDENTIALS("credentials"),

    /** **/
    CERTIFICATES("certificates"),

    /** **/
    ACCESS_TOKEN("access_token"),

    /** **/
    ACCESS_KEYS("access_keys");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    SecurityType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static SecurityType fromString(String raw) {

        for (SecurityType type : SecurityType.values()) {
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
