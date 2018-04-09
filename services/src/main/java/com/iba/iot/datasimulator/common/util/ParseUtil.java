package com.iba.iot.datasimulator.common.util;

import java.text.MessageFormat;

/**
 *
 */
public class ParseUtil {

    /** **/
    private static final String ARRAY_PREFIX_TEMPLATE = "{0}[{1}]";

    /**
     *
     * @param arrayPath
     * @param index
     * @return
     */
    public static String buildArrayPath(String arrayPath, String index) {
        return  MessageFormat.format(ARRAY_PREFIX_TEMPLATE, arrayPath, index);
    }

    /**
     *
     * @param arrayPath
     * @param index
     * @return
     */
    public static String buildArrayPath(String arrayPath, int index) {
        return buildArrayPath(arrayPath, String.valueOf(index));
    }

}
