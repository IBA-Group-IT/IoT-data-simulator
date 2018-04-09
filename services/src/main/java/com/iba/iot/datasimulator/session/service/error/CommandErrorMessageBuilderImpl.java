package com.iba.iot.datasimulator.session.service.error;

import com.iba.iot.datasimulator.session.controller.SessionWebsocketController;
import com.iba.iot.datasimulator.session.model.active.command.CommandResult;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionManagementCommandResultMessage;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionsStatusCommandResultMessage;
import com.iba.iot.datasimulator.session.model.active.message.CommandResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CommandErrorMessageBuilderImpl implements CommandErrorMessageBuilder {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionWebsocketController.class);

    /** **/
    private final static Pattern STATUSES_PATH = Pattern.compile("sessions$");

    /** **/
    private final static Pattern MANAGEMENT_PATH = Pattern.compile("sessions.[^.]+$");

    @Override
    public CommandResultMessage build(String originalDestination, String errorMessage) {

        logger.error("Building command error message for original destination {} and error message: {}.");

        if (STATUSES_PATH.matcher(originalDestination).find()) {

            return new ActiveSessionsStatusCommandResultMessage(null, CommandResult.FAILURE, errorMessage);

        } else if (MANAGEMENT_PATH.matcher(originalDestination).find()) {

            String sessionId = originalDestination.split("\\.")[1];
            return new ActiveSessionManagementCommandResultMessage(sessionId, CommandResult.FAILURE, errorMessage);
        }

        return null;
    }
}
