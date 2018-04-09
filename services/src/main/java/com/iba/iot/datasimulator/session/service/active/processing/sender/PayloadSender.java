package com.iba.iot.datasimulator.session.service.active.processing.sender;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.target.model.TargetSystem;

/**
 *
 */
public interface PayloadSender {

    /**
     *  @param sessionId
     * @param payload
     * @param targetSystem
     */
    void send(String sessionId, ActiveSessionPayload payload, TargetSystem targetSystem);
}
