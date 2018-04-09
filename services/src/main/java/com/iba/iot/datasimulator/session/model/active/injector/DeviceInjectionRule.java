package com.iba.iot.datasimulator.session.model.active.injector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 */
public enum DeviceInjectionRule {

    /** **/
    ALL("all"),

    /** **/
    SPECIFIC("specific"),

    /** **/
    RANDOM("random"),

    /** **/
    ROUND_ROBIN("round_robin");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    DeviceInjectionRule(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static DeviceInjectionRule fromString(String raw) {

        for (DeviceInjectionRule type : DeviceInjectionRule.values()) {
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
