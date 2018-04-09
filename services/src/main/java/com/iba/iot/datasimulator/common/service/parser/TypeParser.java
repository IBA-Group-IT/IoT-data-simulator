package com.iba.iot.datasimulator.common.service.parser;

/**
 *
 */
public interface TypeParser {

    /**
     *
     * @param value
     * @return
     */
    TypeParsingResult parse(String value);

    /**
     *
     * @param value
     * @return
     */
    TypeParsingResult parseLongAsTimestamp(long value);

    /**
     *
     * @param value
     * @return
     */
    TypeParsingResult parseStringAsDate(String value);
}
