package com.iba.iot.datasimulator.session.model.active.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {

    SESSIONS_STATUS_COMMAND_RESULT("sessions_status_command_result"),

    SESSION_MANAGEMENT_COMMAND_RESULT("session_management_command_result"),

    SESSION_STATUS("session_status"),

    SESSION_PAYLOAD("session_payload"),

    SESSION_ANALYTICS("session_analytics"),

    SESSION_ERROR("session_error");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    MessageType(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static MessageType fromString(String raw) {

        for (MessageType type : MessageType.values()) {
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
