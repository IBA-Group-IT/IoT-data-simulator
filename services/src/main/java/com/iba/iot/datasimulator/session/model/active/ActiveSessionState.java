package com.iba.iot.datasimulator.session.model.active;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActiveSessionState {

    RUNNING("running"),

    PAUSED("paused"),

    COMPLETED("completed"),

    STOPPED("stopped"),

    FAILED("failed");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    ActiveSessionState(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static ActiveSessionState fromString(String raw) {

        for (ActiveSessionState type : ActiveSessionState.values()) {
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
