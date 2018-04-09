package com.iba.iot.datasimulator.session.model.active;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSessionStatus {

    /**
     *
     * @param sessionId
     * @param state
     */
    public ActiveSessionStatus(String sessionId, ActiveSessionState state) {
        this.sessionId = sessionId;
        this.state = state;
    }

    private String sessionId;

    private ActiveSessionState state;

    private Collection<String> errors;
}
