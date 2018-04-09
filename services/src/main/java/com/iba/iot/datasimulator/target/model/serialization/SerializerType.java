package com.iba.iot.datasimulator.target.model.serialization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 */
public enum SerializerType {

    /** **/
    PROTOBUF("protobuf");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    SerializerType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static SerializerType fromString(String raw) {

        for (SerializerType type : SerializerType.values()) {
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
