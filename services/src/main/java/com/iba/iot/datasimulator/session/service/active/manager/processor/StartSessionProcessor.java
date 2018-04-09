package com.iba.iot.datasimulator.session.service.active.manager.processor;

import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionState;
import com.iba.iot.datasimulator.session.model.active.command.SessionManagementCommand;
import com.iba.iot.datasimulator.session.model.active.command.CommandResult;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionManagementCommandResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 */
@Component
public class StartSessionProcessor extends CommonSessionProcessor implements ActiveSessionProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(StartSessionProcessor.class);

    @Override
    public SessionManagementCommand getCommand() {
        return SessionManagementCommand.START;
    }

    @Override
    public ActiveSessionManagementCommandResultMessage process(String sessionId, Map<String, ActiveSession> sessions) {

        logger.debug("StartSessionProcessor#process: processing command START for session {}", sessionId);

        /**
         * First handle existing sessions in RUNNING | PAUSED states
         */
        if (sessions.containsKey(sessionId)) {

            ActiveSession activeSession = sessions.get(sessionId);
            ActiveSessionState sessionState = activeSession.getStatus().getState();
            if (sessionState == ActiveSessionState.RUNNING) {

                logger.warn("Couldn't start session {} as it is already running", sessionId);
                return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.FAILURE,
                        "Session " + sessionId + " is already running.");

            } else if (sessionState == ActiveSessionState.PAUSED) {

                activeSession.resume();
                return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.SUCCESS);
            }
        }

        /**
         * Session isn't created yet or in COMPLETED | FAILED states
         */
        return startNewSession(sessionId, sessions);
    }
}
