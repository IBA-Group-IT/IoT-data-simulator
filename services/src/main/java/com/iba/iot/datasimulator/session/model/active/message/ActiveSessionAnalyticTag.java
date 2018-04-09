package com.iba.iot.datasimulator.session.model.active.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActiveSessionAnalyticTag {

    SESSION_STARTED("session_started"),

    SESSION_COMPLETED("session_completed"),

    SESSION_PAUSED("session_paused"),

    SESSION_RESUMED("session_resumed"),

    SESSION_FAILED("session_failed");

    /** **/
    private String value;

    /**
     *
     * @param value
     */
    ActiveSessionAnalyticTag(String value) {
        this.value = value;
    }

    /**
     *
     * @param raw
     * @return
     */
    @JsonCreator
    public static ActiveSessionAnalyticTag fromString(String raw) {

        for (ActiveSessionAnalyticTag type : ActiveSessionAnalyticTag.values()) {
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
