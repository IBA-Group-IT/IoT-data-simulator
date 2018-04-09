package com.iba.iot.datasimulator.session.model.active.generator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 */
public enum GeneratorType {

    /** **/
    JS_FUNCTION("js_function"),

    /** **/
    SCHEMA_BASED("schema_based");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    GeneratorType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static GeneratorType fromString(String raw) {

        for (GeneratorType type : GeneratorType.values()) {
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
