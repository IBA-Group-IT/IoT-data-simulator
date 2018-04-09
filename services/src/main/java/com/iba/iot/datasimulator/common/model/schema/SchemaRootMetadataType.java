package com.iba.iot.datasimulator.common.model.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 */
public enum SchemaRootMetadataType {

    /** **/
    CSV("csv"),

    /** **/
    JSON("json");


    /** **/
    private String value;

    /**
     *
     * @param value
     */
    SchemaRootMetadataType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static SchemaRootMetadataType fromString(String raw) {

        for (SchemaRootMetadataType type : SchemaRootMetadataType.values()) {
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
