package com.iba.iot.datasimulator.session.service.active.processing.reader.parser;

/**
 *
 */
public interface JsonTokenParser {

    /**
     *
     * @return
     */
    String nextToken();

    /**
     *
     * @return
     */
    int getCurrentOffset();
}
