package com.iba.iot.datasimulator.session.model.active.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SessionManagementCommand {

    START("start"),

    PAUSE("pause"),

    STOP("stop");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    SessionManagementCommand(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static SessionManagementCommand fromString(String raw) {

        for (SessionManagementCommand type : SessionManagementCommand.values()) {
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
