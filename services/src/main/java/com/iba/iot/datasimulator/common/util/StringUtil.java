package com.iba.iot.datasimulator.common.util;

import org.apache.commons.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 *
 */
public class StringUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /** **/
    public static final String EMPTY_STRING = "";

    /** **/
    public static final String COMMA = ",";

    /** **/
    public static final String COMMA_WITH_SPACE = ", ";

    /** **/
    public static final char OPENING_SQUARE_BRACKET_CHAR = '[';

    /** **/
    public static final char CLOSING_SQUARE_BRACKET_CHAR = ']';

    /** **/
    public static final char OPENING_CURLY_BRACKET_CHAR = '{';

    /** **/
    public static final char CLOSING_CURLY_BRACKET_CHAR = '}';

    /** **/
    public static final String OPENING_SQUARE_BRACKET = "[";

    /** **/
    public static final String CLOSING_SQUARE_BRACKET = "]";

    /** **/
    public static final String OPENING_CURLY_BRACKET = "{";

    /** **/
    public static final String CLOSING_CURLY_BRACKET = "}";

    /** **/
    public static final String COLON = ":";

    /** **/
    public static final String SPACE = " ";

    /** **/
    public static final String UNDERSCORE = "_";

    /**
     *
     * @param bytes
     * @return
     */
    public static String buildString(byte[] bytes) {

        try {

            return new String(bytes, StandardCharsets.UTF_8.name());

        } catch (UnsupportedEncodingException exception) {

            logger.error("An error occurred during string building: {}", ExceptionUtil.getErrorMessage(exception));
            throw new RuntimeException(exception);
        }
    }

    /**
     *
     * @param template
     * @param values
     * @return
     */
    public static String replacePlaceholders(String template, Map<String, String> values) {

        StrSubstitutor sub = new StrSubstitutor(values);
        return sub.replace(template);
    }

}
