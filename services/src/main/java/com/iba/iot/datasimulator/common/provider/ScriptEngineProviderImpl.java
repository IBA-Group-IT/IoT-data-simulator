package com.iba.iot.datasimulator.common.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class ScriptEngineProviderImpl implements ScriptEngineProvider {

    /** **/
    private static final String MOMENT = "libs/moment.js";

    /** **/
    private static final String LODASH = "libs/lodash.js";

    /** **/
    private static final String NASHORN_ENGINE_NAME = "nashorn";

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ScriptEngineProviderImpl.class);

    /** **/
    private ScriptEngine scriptEngine;


    @PostConstruct
    private void init() throws ScriptException {

        logger.debug(">>> Initializing script engine.");

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        scriptEngine = scriptEngineManager.getEngineByName(NASHORN_ENGINE_NAME);

        InputStream moment = getClass().getClassLoader().getResourceAsStream(MOMENT);
        InputStream lodash = getClass().getClassLoader().getResourceAsStream(LODASH);

        scriptEngine.eval(new InputStreamReader(moment));
        scriptEngine.eval(new InputStreamReader(lodash));
    }

    @Override
    public ScriptEngine get() {
        return scriptEngine;
    }
}
