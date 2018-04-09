package com.iba.iot.datasimulator.session.controller;

import com.iba.iot.datasimulator.common.util.ExceptionUtil;
import com.iba.iot.datasimulator.common.model.ErrorResponse;
import com.iba.iot.datasimulator.session.constant.SessionStompPath;
import com.iba.iot.datasimulator.session.model.active.command.ActiveSessionManagementCommand;
import com.iba.iot.datasimulator.session.model.active.command.CommandResult;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionManagementCommandResultMessage;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionsStatusCommandResultMessage;
import com.iba.iot.datasimulator.session.model.active.message.CommandResultMessage;
import com.iba.iot.datasimulator.session.service.active.manager.ActiveSessionManager;
import com.iba.iot.datasimulator.session.service.error.CommandErrorMessageBuilder;
import com.iba.iot.datasimulator.session.util.StompUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Map;

@Controller
@MessageMapping("sessions")
public class SessionWebsocketController {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionWebsocketController.class);

    /** **/
    private static final String DESTINATION_HEADER = "lookupDestination";

    /** **/
    @Autowired
    private ActiveSessionManager activeSessionManager;

    /** **/
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /** **/
    @Autowired
    private CommandErrorMessageBuilder commandErrorMessageBuilder;

    @MessageMapping
    public void getStatuses(@Headers Map<String, Object> headers,
                            Principal principal) throws Exception {

        logger.debug("Get all active sessions statuses.");

        ActiveSessionsStatusCommandResultMessage message =
                new ActiveSessionsStatusCommandResultMessage(activeSessionManager.getSessionStatuses(), CommandResult.SUCCESS);
        messagingTemplate.convertAndSendToUser(principal.getName(), SessionStompPath.SESSIONS_QUEUE, message, headers);
    }

    @MessageMapping("{sessionId}")
    public void manage(@DestinationVariable String sessionId,
                       @Headers Map<String, Object> headers,
                       Principal principal,
                       @Valid @NotNull ActiveSessionManagementCommand command) throws Exception {

        logger.debug("Performing command {} for session {}.", command.getCommand(), sessionId);
        ActiveSessionManagementCommandResultMessage message = activeSessionManager.manage(sessionId, command);

        messagingTemplate.convertAndSendToUser(principal.getName(),
                StompUtil.getSessionQueue(sessionId), message, headers);
    }

    @MessageExceptionHandler
    public void handleException(Exception exception,
                                @Headers Map<String, Object> headers,
                                Principal principal) {

        String errorMessage = ExceptionUtil.getErrorMessage(exception);
        logger.error("Unexpected error occurred: {}", errorMessage);

        String originalDestination = (String) headers.get(DESTINATION_HEADER);
        CommandResultMessage errorResponseMessage = commandErrorMessageBuilder.build(originalDestination, errorMessage);
        if (errorResponseMessage != null) {

            String destination = SessionStompPath.QUEUE + originalDestination;
            messagingTemplate.convertAndSendToUser(principal.getName(), destination, errorResponseMessage, headers);

        } else {

            // Registered unknown error. Sending it to default errors path
            messagingTemplate.convertAndSendToUser(principal.getName(), SessionStompPath.ERRORS_QUEUE, new ErrorResponse(errorMessage), headers);
        }
    }
}