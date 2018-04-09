package com.iba.iot.datasimulator.session.service.active.processing.generator;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;

/**
 *
 */
public interface SessionPayloadGenerator {

    /**
     *
     * @param payload
     * @return
     */
    ActiveSessionPayload process(ActiveSessionPayload payload) throws Exception;

}
