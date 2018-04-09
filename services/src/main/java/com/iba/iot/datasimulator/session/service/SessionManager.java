package com.iba.iot.datasimulator.session.service;

import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.SessionCreateUpdateRequest;

import java.io.IOException;
import java.util.Collection;

/**
 *
 */
public interface SessionManager {

    /**
     *
     * @param sessionCreateUpdateRequest
     * @return
     */
    Session create(SessionCreateUpdateRequest sessionCreateUpdateRequest);

    /**
     *
     * @param session
     * @return
     */
    void create(Session session);

    /**
     *
     * @param sessionId
     * @param sessionCreateUpdateRequest
     * @return
     */
    Session update(String sessionId, SessionCreateUpdateRequest sessionCreateUpdateRequest);

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
     * @param sessionId
     */
    void remove(String sessionId);

    /**
     *
     * @param dataDefinition
     */
    void updateSessionsSchema(DataDefinition dataDefinition) throws IOException;

    /**
     *
     * @param session
     */
    void importSession(Session session) throws IOException;
}
