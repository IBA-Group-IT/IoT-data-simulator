package com.iba.iot.datasimulator.common.service.parser.processor;

import com.iba.iot.datasimulator.common.service.parser.TypeParsingResult;

/**
 *
 */
public interface TypeParsingProcessor {

    /**
     *
     * @param value
     * @return
     */
    TypeParsingResult parse(String value);

}
