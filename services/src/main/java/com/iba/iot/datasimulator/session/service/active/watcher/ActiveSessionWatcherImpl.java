package com.iba.iot.datasimulator.session.service.active.watcher;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionState;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionStatus;
import com.iba.iot.datasimulator.session.model.active.command.ActiveSessionManagementCommand;
import com.iba.iot.datasimulator.session.model.active.command.SessionManagementCommand;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionErrorMessage;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionStatusMessage;
import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;
import com.iba.iot.datasimulator.session.service.active.manager.ActiveSessionManager;
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

    @Autowired
    private ActiveSessionManager activeSessionManager;

    @RabbitListener(queues = "sessionStates")
    @Override
    public void processCompletedSessions(ActiveSessionStatusMessage message) {

        ActiveSessionStatus status = message.getStatus();
        if (status.getState() == ActiveSessionState.COMPLETED) {

            String sessionId = status.getSessionId();
            ActiveSession session = sessionStore.get(sessionId);
            if (ActiveSessionUtil.getSessionState(session) == ActiveSessionState.COMPLETED) {

                logger.debug("Removing session {} from session store as competed.", sessionId);
                sessionStore.remove(sessionId);

                if (session.isReplayLooped() && !session.isStopped()) {
                    restartSession(sessionId);
                }
            }
        }
    }

    /**
     *
     * @param sessionId
     */
    private void restartSession(String sessionId) {

        logger.debug(">>> Restarting session {} on completion due to looped session replay setting.");
        activeSessionManager.manage(sessionId, new ActiveSessionManagementCommand(SessionManagementCommand.START));
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
