package com.iba.iot.datasimulator.common.util;

/**
 *
 */
public class ExceptionUtil {

    /**
     *
     * @param exception
     * @return
     */
    public static String getErrorMessage(Throwable exception) {
        return exception.getClass().getName() + ": " + exception.getMessage();
    }

}
