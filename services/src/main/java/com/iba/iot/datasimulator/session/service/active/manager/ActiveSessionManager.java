package com.iba.iot.datasimulator.session.service.active.manager;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionStatus;
import com.iba.iot.datasimulator.session.model.active.command.ActiveSessionManagementCommand;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionManagementCommandResultMessage;

import java.util.Collection;

/**
 *
 */
public interface ActiveSessionManager {

    /**
     *
     * @param sessionId
     * @param command
     * @return
     */
    ActiveSessionManagementCommandResultMessage manage(String sessionId, ActiveSessionManagementCommand command);

    /**
     *
     * @return
     */
    Collection<ActiveSessionStatus> getSessionStatuses();

}
