package com.iba.iot.datasimulator.common.service.json.interop;

import com.iba.iot.datasimulator.common.provider.ScriptEngineProvider;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptException;

@Component
public class JavascriptJSONWrapperImpl implements JavascriptJSONWrapper {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(JavascriptJSONWrapperImpl.class);

    /** **/
    private static final String JSON_OBJECT_LITERAL = "JSON";

    /** **/
    private static final String STRINGIFY_FUNCTION_NAME = "stringify";

    /** **/
    private static final String PARSE_FUNCTION_NAME = "parse";

    @Autowired
    private ScriptEngineProvider scriptEngineProvider;

    /** **/
    private ScriptObjectMirror json;

    @PostConstruct
    private void init() throws ScriptException {
        json = (ScriptObjectMirror) scriptEngineProvider.get().eval(JSON_OBJECT_LITERAL);
    }

    @Override
    public String stringify(ScriptObjectMirror jsObject) {

        if (jsObject != null) {
            String result = (String) json.callMember(STRINGIFY_FUNCTION_NAME, jsObject);
            logger.debug(">>> JSON.stringify function result: {}", result);
            return result;
        }

        return null;
    }

    @Override
    public ScriptObjectMirror parse(String raw) {

        if (StringUtils.isNoneEmpty(raw)) {
            logger.debug(">>> Invoking JSON.parse function for string: {}", raw);
            return (ScriptObjectMirror) json.callMember(PARSE_FUNCTION_NAME, raw);
        }

        return null;
    }
}
