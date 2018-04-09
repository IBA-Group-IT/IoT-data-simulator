package com.iba.iot.datasimulator.common.util;

import jdk.nashorn.api.scripting.JSObject;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 *
 */
public class ScriptEngineUtil {

    /** **/
    private static final String OBJECT_LITERAL = "Object";

    /**
     *
     * @param scriptEngine
     * @return
     * @throws ScriptException
     */
    public static JSObject getJsObject(ScriptEngine scriptEngine) throws ScriptException {

        JSObject objConstructor = (JSObject)scriptEngine.eval(OBJECT_LITERAL);
        return (JSObject) objConstructor.newObject();
    }

}
