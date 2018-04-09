package com.iba.iot.datasimulator.common.model.schema.property.rule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SchemaPropertyRuleType {

    /** **/
    AS_IS("as_is"),

    /** **/
    LITERAL_STRING("literal_string"),

    /** **/
    LITERAL_BOOLEAN("literal_boolean"),

    /** **/
    LITERAL_INTEGER("literal_integer"),

    /** **/
    LITERAL_LONG("literal_long"),

    /** **/
    LITERAL_DOUBLE("literal_double"),

    /** **/
    UUID("uuid"),

    /** **/
    RANDOM_INTEGER("random_integer"),

    /** **/
    RANDOM_LONG("random_long"),

    /** **/
    RANDOM_DOUBLE("random_double"),

    /** **/
    RANDOM_BOOLEAN("random_boolean"),

    /** **/
    DEVICE_PROPERTY("device_property"),

    /** **/
    CURRENT_TIME("current_time"),

    /** **/
    RELATIVE_TIME("relative_time"),

    /** **/
    CUSTOM_FUNCTION("custom_function");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    SchemaPropertyRuleType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static SchemaPropertyRuleType fromString(String raw) {

        for (SchemaPropertyRuleType type : SchemaPropertyRuleType.values()) {
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
