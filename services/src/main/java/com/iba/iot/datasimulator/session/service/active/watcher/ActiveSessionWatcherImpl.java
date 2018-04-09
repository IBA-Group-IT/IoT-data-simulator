package com.iba.iot.datasimulator.session.service.active.watcher;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionState;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionStatus;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionErrorMessage;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionStatusMessage;
import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;
import com.iba.iot.datasimulator.session.util.ActiveSessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *
 */
@Service
public class ActiveSessionWatcherImpl implements ActiveSessionWatcher {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ActiveSessionWatcherImpl.class);

    /** **/
    @Autowired
    @Qualifier("sessionStore")
    private Map<String, ActiveSession> sessionStore;

    @RabbitListener(queues = "sessionStates")
    @Override
    public void processCompletedSessions(ActiveSessionStatusMessage message) {

        ActiveSessionStatus status = message.getStatus();
        if (status.getState() == ActiveSessionState.COMPLETED) {

            String sessionId = status.getSessionId();
            if (ActiveSessionUtil.getSessionState(sessionStore.get(sessionId)) == ActiveSessionState.COMPLETED) {

                logger.debug("Removing session {} from session store as competed.", sessionId);
                sessionStore.remove(sessionId);
            }
        }
    }

    @RabbitListener(queues = "sessionErrors")
    @Override
    public void processSessionError(ActiveSessionErrorMessage message) {

        ActiveSession activeSession = sessionStore.get(message.getSessionId());
        if (activeSession != null) {

            logger.warn("Session runtime error registration: ", message);
            activeSession.registerError(message.getMessage());
        }
    }

}
