package com.iba.iot.datasimulator.target.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 */
public enum RestMethod {

    /** **/
    GET("get"),

    /** **/
    POST("post"),

    /** **/
    PUT("put");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    RestMethod(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static RestMethod fromString(String raw) {

        for (RestMethod type : RestMethod.values()) {
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
