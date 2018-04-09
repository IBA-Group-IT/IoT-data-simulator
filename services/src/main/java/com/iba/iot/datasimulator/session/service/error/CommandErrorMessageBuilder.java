package com.iba.iot.datasimulator.session.service.error;

import com.iba.iot.datasimulator.session.model.active.message.CommandResultMessage;

/**
 *
 */
public interface CommandErrorMessageBuilder {

    /**
     *
     * @param originalDestination
     * @param errorMessage
     * @return
     */
    CommandResultMessage build(String originalDestination, String errorMessage);

}
