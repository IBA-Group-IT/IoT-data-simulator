package com.iba.iot.datasimulator.session.model.active.message;

/**
 *
 */
public interface ActiveSessionMessage extends Message {

    /**
     *
     * @return
     */
    String getSessionId();

    /**
     *
     * @return
     */
    String getMessage();

    /**
     *
     * @return
     */
    long getTimestamp();
}
