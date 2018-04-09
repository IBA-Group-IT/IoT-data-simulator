package com.iba.iot.datasimulator.session.model.active.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 */
public enum DatasetFilterType {

    /** **/
    DATASET_ENTRY_POSITION("dataset_entry_position"),

    /** **/
    CUSTOM_FUNCTION("custom_function");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    DatasetFilterType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static DatasetFilterType fromString(String raw) {

        for (DatasetFilterType type : DatasetFilterType.values()) {
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
