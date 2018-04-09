package com.iba.iot.datasimulator.common.model.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SchemaType {

    /** **/
    OBJECT("object"),

    /** **/
    ARRAY("array");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    SchemaType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static SchemaType fromString(String raw) {

        for (SchemaType type : SchemaType.values()) {
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
