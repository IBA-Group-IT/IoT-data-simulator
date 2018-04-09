package com.iba.iot.datasimulator.session.factory;

import com.iba.iot.datasimulator.session.model.Session;

import javax.script.ScriptException;

/**
 *
 * @param <T>
 */
public interface SessionHandlerFactory<T> {

    /**
     *
     * @param session
     * @return
     */
    T build(Session session) throws ScriptException;
}
