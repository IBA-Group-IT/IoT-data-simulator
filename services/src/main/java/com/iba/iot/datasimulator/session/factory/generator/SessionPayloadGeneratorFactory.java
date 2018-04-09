package com.iba.iot.datasimulator.session.factory.generator;

import com.iba.iot.datasimulator.session.factory.SessionHandlerFactory;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.service.active.processing.generator.SessionPayloadGenerator;

/**
 *
 */
public interface SessionPayloadGeneratorFactory extends SessionHandlerFactory<SessionPayloadGenerator> {

    /**
     *
     * @param session
     * @return
     */
    SessionPayloadGenerator build(Session session);
}
