package com.iba.iot.datasimulator.session.service.active.processing.reader.filter;

import com.iba.iot.datasimulator.common.provider.ScriptEngineProvider;
import com.iba.iot.datasimulator.common.util.ScriptEngineUtil;
import com.iba.iot.datasimulator.session.model.active.filter.CustomFunctionFilter;
import jdk.nashorn.api.scripting.JSObject;
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
public class CustomFunctionDatasetFilterProcessor implements DatasetFilterProcessor<String> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CustomFunctionDatasetFilterProcessor.class);

    @Autowired
    private ScriptEngineProvider scriptEngineProvider;

    /** **/
    private CustomFunctionFilter customFunctionFilter;

    /** **/
    private JSObject jsFunction;

    /** **/
    private JSObject jsState;

    /**
     *
     */
    public CustomFunctionDatasetFilterProcessor(CustomFunctionFilter customFunctionFilter) {
        this.customFunctionFilter = customFunctionFilter;
    }

    @PostConstruct
    private void init() throws ScriptException {

        ScriptEngine scriptEngine = scriptEngineProvider.get();
        this.jsFunction = (JSObject) scriptEngine.eval(customFunctionFilter.getJsFunction());
        this.jsState = ScriptEngineUtil.getJsObject(scriptEngine);
    }

    @Override
    public boolean filter(String datasetEntry) {

        logger.debug(">>> Processing dataset custom function filter for dataset entry: {}", datasetEntry);
        Object raw = jsFunction.call(null, jsState, datasetEntry);

        if (raw instanceof Boolean) {
            return (Boolean) raw;
        }

        return false;
    }
}
