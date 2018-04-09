package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iba.iot.datasimulator.common.model.schema.property.SessionSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.rule.CustomFunctionSchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.common.service.json.interop.JavascriptJSONWrapper;
import com.iba.iot.datasimulator.common.util.SchemaPropertyUtil;
import com.iba.iot.datasimulator.common.util.StringUtil;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.convertor.DatasetEntryValueConverter;
import com.iba.iot.datasimulator.session.service.active.processing.state.SessionStateManager;
import com.iba.iot.datasimulator.session.service.active.processing.state.model.SessionPropertyState;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Undefined;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.script.ScriptException;
import java.io.IOException;

@Component
public class CustomFunctionRuleProcessor implements RuleProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CustomFunctionRuleProcessor.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JavascriptJSONWrapper jsonWrapper;

    @Autowired
    private DatasetEntryValueConverter datasetEntryValueConverter;

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue, RuleEngineContext context) throws Exception {

        logger.debug(">>> Processing custom function rule for unfolded schema property {} and dataset result {}",
                unfoldedSchemaProperty, datasetEntryValue);

        String position = unfoldedSchemaProperty.getPosition();
        SessionSchemaProperty property = unfoldedSchemaProperty.getProperty();
        CustomFunctionSchemaPropertyRule customFunctionRule = (CustomFunctionSchemaPropertyRule) property.getRule();
        String rawFunction = customFunctionRule.getFunction();
        String deviceName = getDeviceName(device);

        SessionPropertyState propertyState = getSessionPropertyState(context, position, rawFunction, deviceName);
        JSObject function = propertyState.getFunction();
        JSObject state = propertyState.getState();

        Object rawResult = getRawResult(unfoldedSchemaProperty, datasetEntryValue, function, state, context, deviceName);
        Object result = convertToRequiredType(rawResult, property);

        context.getProcessingResults().put(position, result);
    }

    /**
     *
     * @param unfoldedSchemaProperty
     * @param datasetEntryValue
     * @param function
     * @param propertyState
     * @param context
     * @return
     * @throws ScriptException
     */
    private Object getRawResult(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, String datasetEntryValue,
                                JSObject function, JSObject propertyState, RuleEngineContext context, String deviceName) throws ScriptException, IOException {

        String datasetEntry = context.getDatasetEntry();
        JSObject sessionState = getSessionState(context);

        if (!StringUtils.isEmpty(datasetEntryValue)) {

            Object originalDatasetEntryValue = datasetEntryValueConverter.convertToOriginalType(unfoldedSchemaProperty, datasetEntryValue);
            return function.call(null, propertyState, sessionState, deviceName, originalDatasetEntryValue, datasetEntry);
        }

        logger.debug(">>> Processing custom function rule with empty dataset");
        return function.call(null, propertyState, sessionState, deviceName);
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
     * @param context
     * @param position
     * @param rawFunction
     * @param deviceName
     * @return
     * @throws ScriptException
     */
    private SessionPropertyState getSessionPropertyState(RuleEngineContext context, String position, String rawFunction, String deviceName) throws ScriptException {

        SessionStateManager sessionPropertyStateManager = context.getSessionStateManager();
        String sessionId = context.getSessionId();

        SessionPropertyState propertyState = sessionPropertyStateManager.getSessionPropertyState(sessionId, position, deviceName);
        if (propertyState == null) {
            return sessionPropertyStateManager.initSessionPropertyState(sessionId, position, rawFunction, deviceName);
        }

        return propertyState;
    }

    /**
     *
     * @param context
     * @return
     * @throws ScriptException
     */
    private JSObject getSessionState(RuleEngineContext context) throws ScriptException {
        return context.getSessionStateManager().getSessionState(context.getSessionId());
    }

    @Override
    public SchemaPropertyRuleType getType() {
        return SchemaPropertyRuleType.CUSTOM_FUNCTION;
    }

    /**
     *
     * @param value
     * @param property
     * @return
     */
    private Object convertToRequiredType(Object value, SessionSchemaProperty property) throws IOException {

        if (value == null || value instanceof Undefined) {
            return null;
        }

        if (value instanceof ScriptObjectMirror) {
            String rawJson = jsonWrapper.stringify((ScriptObjectMirror) value);
            return mapper.readValue(rawJson, JsonNode.class);
        }

        String raw = value.toString();
        if (SchemaPropertyUtil.isBooleanProperty(property)) {
            return Boolean.valueOf(raw);
        } else if (SchemaPropertyUtil.isDoubleProperty(property)) {
            return Double.valueOf(raw);
        } else if (SchemaPropertyUtil.isLongProperty(property) || SchemaPropertyUtil.isTimestampProperty(property)) {
            return Double.valueOf(raw).longValue();
        } else if (SchemaPropertyUtil.isIntegerProperty(property)) {
            return Double.valueOf(raw).intValue();
        } else {
            return raw;
        }

    }
}
