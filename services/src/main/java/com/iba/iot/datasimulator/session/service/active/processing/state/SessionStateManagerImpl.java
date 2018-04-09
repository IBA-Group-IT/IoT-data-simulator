package com.iba.iot.datasimulator.session.service.active.processing.state;

import com.iba.iot.datasimulator.common.provider.ScriptEngineProvider;
import com.iba.iot.datasimulator.common.util.ScriptEngineUtil;
import com.iba.iot.datasimulator.session.service.active.processing.state.model.SessionPropertyState;
import com.iba.iot.datasimulator.session.service.active.processing.state.model.SessionPropertyStateKey;
import jdk.nashorn.api.scripting.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SessionStateManagerImpl implements SessionStateManager {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionStateManagerImpl.class);

    /** **/
    private Map<SessionPropertyStateKey, SessionPropertyState> sessionPropertyStates = new HashMap<>();

    /** **/
    private Map<String, JSObject> sessionStates = new HashMap<>();

    @Autowired
    private ScriptEngineProvider scriptEngineProvider;

    @Override
    public SessionPropertyState getSessionPropertyState(SessionPropertyStateKey key) {
        return sessionPropertyStates.get(key);
    }

    @Override
    public SessionPropertyState getSessionPropertyState(String sessionId, String propertyPath, String deviceName) {

        SessionPropertyStateKey key = new SessionPropertyStateKey(sessionId, propertyPath, deviceName);
        return getSessionPropertyState(key);
    }

    @Override
    public SessionPropertyState initSessionPropertyState(String sessionId, String propertyPath, String function, String deviceName) throws ScriptException {

        SessionPropertyStateKey key = new SessionPropertyStateKey(sessionId, propertyPath, deviceName);

        ScriptEngine scriptEngine = scriptEngineProvider.get();
        JSObject jsFunction = (JSObject) scriptEngine.eval(function);

        // get JS "Object" constructor object
        JSObject jsState = ScriptEngineUtil.getJsObject(scriptEngine);

        SessionPropertyState propertyState = new SessionPropertyState(jsFunction, jsState);
        setSessionPropertyState(key, propertyState);

        return propertyState;
    }

    @Override
    public void setSessionPropertyState(SessionPropertyStateKey key, SessionPropertyState state) {
        sessionPropertyStates.put(key, state);
    }

    @Override
    public void cleanupStateForSession(String sessionId) {

        logger.debug(">>> Cleaning up properties state for session {}", sessionId);
        sessionPropertyStates.keySet().removeIf(key -> key.getSessionId().equalsIgnoreCase(sessionId));

        logger.debug(">>> Cleaning state for session {}", sessionId);
        sessionStates.remove(sessionId);
    }

    @Override
    public JSObject getSessionState(String sessionId) throws ScriptException {

        JSObject state = sessionStates.get(sessionId);
        if (state == null) {

            logger.debug(">>> Initializing state for session {}", sessionId);
            ScriptEngine scriptEngine = scriptEngineProvider.get();
            state = ScriptEngineUtil.getJsObject(scriptEngine);
            sessionStates.put(sessionId, state);
        }

        return state;
    }
}
