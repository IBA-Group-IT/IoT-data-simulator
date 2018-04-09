package com.iba.iot.datasimulator.session.model.active.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CommandResult {

    SUCCESS("success"),

    FAILURE("failure");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    CommandResult(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static CommandResult fromString(String raw) {

        for (CommandResult type : CommandResult.values()) {
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
