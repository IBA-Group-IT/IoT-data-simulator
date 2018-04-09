package com.iba.iot.datasimulator.common.service.json.interop;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 *
 */
public interface JavascriptJSONWrapper {

    /**
     *
     * @param jsObject
     * @return
     */
    String stringify(ScriptObjectMirror jsObject);

    /**
     *
     * @param raw
     * @return
     */
    ScriptObjectMirror parse(String raw);

}
