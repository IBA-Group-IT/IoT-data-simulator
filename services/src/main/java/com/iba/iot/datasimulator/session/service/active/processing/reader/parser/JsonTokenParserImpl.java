package com.iba.iot.datasimulator.session.service.active.processing.reader.parser;


import com.iba.iot.datasimulator.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class JsonTokenParserImpl implements JsonTokenParser {

    private static final Logger logger = LoggerFactory.getLogger(JsonTokenParserImpl.class);

    /** **/
    private StringBuilder data;

    /** **/
    private int offset = -1;

    public JsonTokenParserImpl(StringBuilder data) {
        this.data = data;
    }

    @Override
    public String nextToken() {

        logger.debug(">>> Finding next json token.");

        while(offset < data.length() - 1) {

            offset++;
            char currentChar = data.charAt(offset);

            if (currentChar == StringUtil.OPENING_CURLY_BRACKET_CHAR ||
                currentChar == StringUtil.OPENING_SQUARE_BRACKET_CHAR ||
                currentChar == StringUtil.CLOSING_CURLY_BRACKET_CHAR ||
                currentChar == StringUtil.CLOSING_SQUARE_BRACKET_CHAR) {

                logger.debug(">>> Token {} was found at {} position.", currentChar, offset);
                return Character.toString(currentChar);
            }
        }

        logger.debug(">>> Provided data end has been reached without tokens.");
        return null;
    }

    @Override
    public int getCurrentOffset() {
        return offset;
    }
}
