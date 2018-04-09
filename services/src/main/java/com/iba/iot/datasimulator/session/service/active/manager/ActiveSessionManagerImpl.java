package com.iba.iot.datasimulator.session.service.active.manager;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionStatus;
import com.iba.iot.datasimulator.session.model.active.command.ActiveSessionManagementCommand;
import com.iba.iot.datasimulator.session.model.active.command.SessionManagementCommand;
import com.iba.iot.datasimulator.session.model.active.command.CommandResult;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionManagementCommandResultMessage;
import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;
import com.iba.iot.datasimulator.session.service.active.manager.processor.ActiveSessionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class ActiveSessionManagerImpl implements ActiveSessionManager {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ActiveSessionManagerImpl.class);

    /** **/
    @Autowired
    @Qualifier("sessionStore")
    private Map<String, ActiveSession> sessionsStore;

    /** **/
    @Autowired
    private Map<SessionManagementCommand, ActiveSessionProcessor> sessionProcessors;

    @Override
    public ActiveSessionManagementCommandResultMessage manage(String sessionId, ActiveSessionManagementCommand command) {

        SessionManagementCommand managementCommand = command.getCommand();
        if (sessionProcessors.containsKey(managementCommand)) {
            return sessionProcessors.get(managementCommand).process(sessionId, sessionsStore);
        }

        logger.error("Command {} for session {} isn't supported.", managementCommand, sessionId);
        return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.FAILURE,
                "Command " + managementCommand + " isn't supported.");
    }

    @Override
    public Collection<ActiveSessionStatus> getSessionStatuses() {

        logger.debug("Processing ActiveSessionManagerImpl#getSessionStatuses");

        return sessionsStore.values()
                       .stream()
                       .map(ActiveSession::getStatus)
                       .collect(Collectors.toList());
    }

}
