package com.iba.iot.datasimulator.definition.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 */
public enum DatasetType {

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
    DatasetType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static DatasetType fromString(String raw) {

        for (DatasetType type : DatasetType.values()) {
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
