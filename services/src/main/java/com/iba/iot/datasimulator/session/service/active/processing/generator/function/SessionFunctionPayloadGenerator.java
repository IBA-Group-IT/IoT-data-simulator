package com.iba.iot.datasimulator.session.service.active.processing.generator.function;

import com.iba.iot.datasimulator.common.provider.ScriptEngineProvider;
import com.iba.iot.datasimulator.common.service.json.interop.JavascriptJSONWrapper;
import com.iba.iot.datasimulator.common.util.ScriptEngineUtil;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.session.service.active.processing.generator.SessionPayloadGenerator;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.SessionSchemaPayloadGenerator;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Undefined;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SessionFunctionPayloadGenerator implements SessionPayloadGenerator {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionSchemaPayloadGenerator.class);

    @Autowired
    private ScriptEngineProvider scriptEngineProvider;

    @Autowired
    private JavascriptJSONWrapper jsonWrapper;

    /** **/
    private String sessionId;

    /** **/
    private String function;

    /** **/
    private JSObject jsFunction;

    /** **/
    private JSObject jsState;

    /**
     *
     * @param sessionId
     * @param function
     * @throws ScriptException
     */
    public SessionFunctionPayloadGenerator(String sessionId, String function) {

        this.sessionId = sessionId;
        this.function = function;
    }

    @PostConstruct
    private void init() throws ScriptException {

        ScriptEngine scriptEngine = scriptEngineProvider.get();
        this.jsFunction = (JSObject) scriptEngine.eval(function);
        this.jsState = ScriptEngineUtil.getJsObject(scriptEngine);
    }

    @Override
    public ActiveSessionPayload process(ActiveSessionPayload payload) {

        logger.debug(">>> Processing session function payload generator for session: {} and payload: {}", sessionId, payload);

        Object rawResult = invokeFunction(payload);
        payload.setGeneratedPayload(serialize(rawResult));

        return payload;
    }

    /**
     *
     * @param payload
     * @return
     */
    private Object invokeFunction(ActiveSessionPayload payload) {

        String deviceName = getDeviceName(payload.getDevice());
        String datasetEntry = getDatasetEntry(payload);

        return jsFunction.call(null, jsState, datasetEntry, deviceName);
    }

    /**
     *
     * @param device
     * @return
     */
    private String getDeviceName(Device device) {

        if (device != null) {
            return device.getName();
        }

        return null;
    }

    /**
     *
     * @param payload
     * @return
     */
    private String getDatasetEntry(ActiveSessionPayload payload) {

        if (payload.isDatasetProvided()) {
            return  payload.getDatasetEntry();
        }

        return null;
    }

    /**
     *
     * @param raw
     * @return
     */
    private String serialize(Object raw) {

        if (raw == null || raw instanceof Undefined) {
            return null;
        }

        if (raw instanceof ScriptObjectMirror) {
            return jsonWrapper.stringify((ScriptObjectMirror) raw);
        }

        return raw.toString();
    }
}
