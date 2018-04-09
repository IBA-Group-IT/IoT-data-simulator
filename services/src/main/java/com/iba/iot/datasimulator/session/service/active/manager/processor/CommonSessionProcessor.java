package com.iba.iot.datasimulator.session.service.active.manager.processor;

import com.iba.iot.datasimulator.common.util.ExceptionUtil;
import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;
import com.iba.iot.datasimulator.session.model.active.command.CommandResult;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionManagementCommandResultMessage;
import com.iba.iot.datasimulator.session.factory.session.ActiveSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class CommonSessionProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CommonSessionProcessor.class);

    /** **/
    @Autowired
    private ActiveSessionFactory sessionFactory;

    protected ActiveSessionManagementCommandResultMessage startNewSession(String sessionId,
                                                                          Map<String, ActiveSession> sessions) {

        try {

            ActiveSession session = sessionFactory.build(sessionId);
            session.start();
            sessions.put(sessionId, session);

            return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.SUCCESS);

        } catch (RuntimeException exception) {

            String errorMessage = ExceptionUtil.getErrorMessage(exception);
            logger.error("Session {} startup has been failed due to unexpected error: {}", sessionId, errorMessage);
            logger.error(exception.getMessage(), exception);
            return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.FAILURE, errorMessage);
        }
    }

}
