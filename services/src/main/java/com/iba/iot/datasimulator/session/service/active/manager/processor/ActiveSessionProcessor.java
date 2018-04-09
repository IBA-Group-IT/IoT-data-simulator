package com.iba.iot.datasimulator.session.service.active.manager.processor;

import com.iba.iot.datasimulator.session.model.active.command.SessionManagementCommand;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionManagementCommandResultMessage;
import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;

import java.util.Map;

public interface ActiveSessionProcessor {

    SessionManagementCommand getCommand();

    ActiveSessionManagementCommandResultMessage process(String sessionId, Map<String, ActiveSession> sessions);

}
