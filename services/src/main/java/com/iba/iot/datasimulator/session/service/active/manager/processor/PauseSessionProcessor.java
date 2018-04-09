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
public class PauseSessionProcessor implements ActiveSessionProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(PauseSessionProcessor.class);

    @Override
    public SessionManagementCommand getCommand() {
        return SessionManagementCommand.PAUSE;
    }

    @Override
    public ActiveSessionManagementCommandResultMessage process(String sessionId, Map<String, ActiveSession> sessions) {

        logger.debug("PauseSessionProcessor#process: processing command PAUSE for session {}", sessionId);

        if (sessions.containsKey(sessionId)) {

            ActiveSession activeSession = sessions.get(sessionId);
            ActiveSessionState state = activeSession.getStatus().getState();

            if (state == ActiveSessionState.RUNNING) {

                activeSession.pause();
                return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.SUCCESS);

            } else {

                logger.warn("Cannot pause session {} with state {}", sessionId, state);
                return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.FAILURE,
                        "Cannot pause session " + sessionId + " with state " + state);
            }
        }

        logger.error("Cannot pause non running session {}", sessionId);
        return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.FAILURE,
                "Cannot pause not running session " + sessionId);
    }
}
