package com.iba.iot.datasimulator.session.model.active.message;

import com.iba.iot.datasimulator.session.model.active.command.CommandResult;

/**
 *
 */
public interface CommandResultMessage extends Message {

    /**
     *
     * @return
     */
    CommandResult getResult();

    /**
     *
     * @return
     */
    String getErrorMessage();
}
