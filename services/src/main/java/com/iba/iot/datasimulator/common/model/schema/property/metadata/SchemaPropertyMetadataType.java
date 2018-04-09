package com.iba.iot.datasimulator.common.model.schema.property.metadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SchemaPropertyMetadataType {

    INTEGER("integer"),

    DOUBLE("double"),

    LONG("long"),

    TIMESTAMP("timestamp"),

    DATE("date");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    SchemaPropertyMetadataType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static SchemaPropertyMetadataType fromString(String raw) {

        for (SchemaPropertyMetadataType type : SchemaPropertyMetadataType.values()) {
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
