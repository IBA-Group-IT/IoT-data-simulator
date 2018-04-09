package com.iba.iot.datasimulator.session.factory.session;

import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;

/**
 *
 */
public interface ActiveSessionFactory {

    /**
     *
     * @param sessionId
     * @return
     */
    ActiveSession build(String sessionId);

}
