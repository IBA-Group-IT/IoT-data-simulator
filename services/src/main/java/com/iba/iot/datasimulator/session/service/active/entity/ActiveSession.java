package com.iba.iot.datasimulator.session.service.active.entity;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionStatus;

/**
 *
 */
public interface ActiveSession {

    /**
     *
     */
    void start();

    /**
     *
     */
    void pause();

    /**
     *
     */
    void resume();

    /**
     *
     */
    void stop();

    /**
     *
     * @param error
     */
    void registerError(String error);

    /**
     *
     * @return
     */
    ActiveSessionStatus getStatus();

    /**
     *
     * @return
     */
    boolean isReplayLooped();

    /**
     *
     * @return
     */
    boolean isStopped();
}
