package com.iba.iot.datasimulator.session.factory.timer;


import com.iba.iot.datasimulator.common.provider.ScriptEngineProvider;
import com.iba.iot.datasimulator.common.util.ScriptEngineUtil;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.timer.CustomFunctionTimer;
import com.iba.iot.datasimulator.session.model.active.timer.Timer;
import com.iba.iot.datasimulator.session.model.active.timer.TimerType;
import com.iba.iot.datasimulator.session.service.active.processing.timer.CustomFunctionTimerProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.timer.TimerProcessor;
import jdk.nashorn.api.scripting.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

@Component
public class CustomFunctionTimerProcessorFactory implements TimerProcessorFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CustomFunctionTimerProcessorFactory.class);

    @Autowired
    private ScriptEngineProvider scriptEngineProvider;

    @Override
    public TimerProcessor build(Session session) {

        Timer timer = session.getTimer();
        logger.debug(">>> Building custom function timer processor from timer: {}", timer);

        CustomFunctionTimer customFunctionTimer = (CustomFunctionTimer) timer;
        ScriptEngine scriptEngine = scriptEngineProvider.get();

        JSObject jsFunction;
        JSObject state;
        try {

            jsFunction = (JSObject) scriptEngine.eval(customFunctionTimer.getFunction());
            state = ScriptEngineUtil.getJsObject(scriptEngine);

        } catch (ScriptException exception) {

            logger.error("An error occurred during custom function timer processor building.", exception);
            throw new RuntimeException("Custom function timer processor building error.");
        }

        return new CustomFunctionTimerProcessor(jsFunction, state);
    }

    @Override
    public TimerType getType() {
        return TimerType.CUSTOM_FUNCTION;
    }
}
