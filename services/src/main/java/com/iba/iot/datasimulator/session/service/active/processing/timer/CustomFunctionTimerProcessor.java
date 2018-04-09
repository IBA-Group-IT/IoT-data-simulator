package com.iba.iot.datasimulator.session.service.active.processing.timer;

import jdk.nashorn.api.scripting.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;

/**
 *
 */
public class CustomFunctionTimerProcessor implements TimerProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CustomFunctionTimerProcessor.class);

    /** **/
    private JSObject function;

    /** **/
    private JSObject state;

    /**
     *
     * @param function
     * @param state
     */
    public CustomFunctionTimerProcessor(JSObject function, JSObject state) {
        this.function = function;
        this.state = state;
    }

    @Override
    public long getWaitInterval(String previousDatasetEntry, String currentDatasetEntry) throws ParseException, IOException {

        long result = parseResult(function.call(null, state, currentDatasetEntry, previousDatasetEntry));

        logger.debug(">>> Custom function timer processor calculated interval: {}", result);
        return result;
    }

    /**
     *
     * @param value
     * @return
     */
    private long parseResult(Object value) {

        if (value != null) {

            String raw = value.toString();
            try {
                return Double.valueOf(raw).longValue();
            } catch (NumberFormatException exception) {
                logger.error(">>> An error occurred during '" + raw + "' value parsing as milliseconds", exception);
            }
        }

        throw new RuntimeException("Custom function timer processing error.");
    }
}
