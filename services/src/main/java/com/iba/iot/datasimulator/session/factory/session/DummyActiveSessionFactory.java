package com.iba.iot.datasimulator.session.factory.session;

import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;
import com.iba.iot.datasimulator.session.service.active.entity.DummyActiveSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

//@Service
public class DummyActiveSessionFactory implements ActiveSessionFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DummyActiveSessionFactory.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public ActiveSession build(String sessionId) {

        logger.debug("Building new active session factory for provided id {}", sessionId);
        return new DummyActiveSession(sessionId, messagingTemplate);
    }
}
