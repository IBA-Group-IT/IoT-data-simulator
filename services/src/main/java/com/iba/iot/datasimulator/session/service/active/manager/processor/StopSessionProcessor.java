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
public class StopSessionProcessor implements ActiveSessionProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(StopSessionProcessor.class);

    @Override
    public SessionManagementCommand getCommand() {
        return SessionManagementCommand.STOP;
    }

    @Override
    public ActiveSessionManagementCommandResultMessage process(String sessionId, Map<String, ActiveSession> sessions) {

        logger.debug("StopSessionProcessor#process: processing command STOP for session {}", sessionId);

        if (sessions.containsKey(sessionId)) {

            ActiveSession activeSession = sessions.get(sessionId);
            ActiveSessionState state = activeSession.getStatus().getState();
            if (state != ActiveSessionState.COMPLETED) {

                activeSession.stop();
                return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.SUCCESS);

            } else {

                logger.warn("Cannot stop not stop already completed session {}", sessionId);
                return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.FAILURE,
                        "Session " + sessionId + " is already completed.");
            }
        }

        logger.error("Cannot stop not active session {}", sessionId);
        return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.FAILURE,
                "Cannot stop not active session " + sessionId);
    }
}
