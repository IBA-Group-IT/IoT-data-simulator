package com.iba.iot.datasimulator.session.service.active.processing.state;

import com.iba.iot.datasimulator.session.service.active.processing.state.model.SessionPropertyState;
import com.iba.iot.datasimulator.session.service.active.processing.state.model.SessionPropertyStateKey;
import jdk.nashorn.api.scripting.JSObject;

import javax.script.ScriptException;

/**
 *
 */
public interface SessionStateManager {


    /**
     *
     * @param key
     * @return
     */
    SessionPropertyState getSessionPropertyState(SessionPropertyStateKey key);

    /**
     *
     * @param sessionId
     * @param propertyPath
     * @param deviceName
     * @return
     */
    SessionPropertyState getSessionPropertyState(String sessionId, String propertyPath, String deviceName);

    /**
     * @param sessionId
     * @param propertyPath
     * @param function
     * @param deviceName
     */
    SessionPropertyState initSessionPropertyState(String sessionId, String propertyPath, String function, String deviceName) throws ScriptException;

    /**
     *
     * @param key
     * @param state
     */
    void setSessionPropertyState(SessionPropertyStateKey key, SessionPropertyState state);


    /**
     *
     * @param sessionId
     * @return
     */
    JSObject getSessionState(String sessionId) throws ScriptException;

    /**
     *
     * @param sessionId
     */
    void cleanupStateForSession(String sessionId);
}
