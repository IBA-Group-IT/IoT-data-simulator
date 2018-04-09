package com.iba.iot.datasimulator.common.model.schema.property;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SchemaPropertyType {

    STRING("string"),

    NUMBER("number"),

    BOOLEAN("boolean"),

    OBJECT("object"),

    ARRAY("array");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    SchemaPropertyType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static SchemaPropertyType fromString(String raw) {

        for (SchemaPropertyType type : SchemaPropertyType.values()) {
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
