package com.iba.iot.datasimulator.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TargetSystemType {

    /** **/
    DUMMY("dummy"),

    /** **/
    MQTT_BROKER("mqtt_broker"),

    /** **/
    AMQP_BROKER("amqp_broker"),

    /** **/
    REST_ENDPOINT("rest_endpoint"),

    /** **/
    WEBSOCKET_ENDPOINT("websocket_endpoint"),

    /** **/
    KAFKA_BROKER("kafka_broker"),

    /** **/
    S3("s3");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    TargetSystemType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static TargetSystemType fromString(String raw) {

        for (TargetSystemType type : TargetSystemType.values()) {
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
