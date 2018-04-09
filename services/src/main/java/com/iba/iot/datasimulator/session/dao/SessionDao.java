package com.iba.iot.datasimulator.session.dao;

import com.iba.iot.datasimulator.session.model.Session;

import java.util.Collection;

/**
 *
 */
public interface SessionDao {

    /**
     *
     * @param session
     */
    Session save(Session session);

    /**
     *
     * @return
     */
    Collection<Session> get();

    /**
     *
     * @param sessionId
     * @return
     */
    Session get(String sessionId);

    /**
     *
     * @param session
     * @return
     */
    Session update(Session session);

    /**
     *
     * @param sessionId
     */
    void remove(String sessionId);

    /**
     *
     * @param dataDefinitionId
     * @return
     */
    Collection<Session> getByDataDefinitionId(String dataDefinitionId);
}
