package com.iba.iot.datasimulator.session.service.active.watcher;

import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionErrorMessage;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionStatusMessage;

/**
 *
 */
public interface ActiveSessionWatcher {

    /**
     *
     * @param message
     */
    void processCompletedSessions(ActiveSessionStatusMessage message);

    /**
     *
     * @param message
     */
    void processSessionError(ActiveSessionErrorMessage message);

}
