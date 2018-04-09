package com.iba.iot.datasimulator.session.util;

import com.iba.iot.datasimulator.session.constant.SessionStompPath;

import java.text.MessageFormat;

public class StompUtil {

    /**
     *
     * @param sessionId
     * @return
     */
    public static String getSessionTopic(String sessionId) {
        return MessageFormat.format(SessionStompPath.SESSION_TOPIC_TEMPLATE, sessionId);
    }

    /**
     *
     * @param sessionId
     * @return
     */
    public static String getSessionQueue(String sessionId) {
        return MessageFormat.format(SessionStompPath.SESSION_QUEUE_TEMPLATE, sessionId);
    }

    /**
     *
     * @param sessionId
     * @return
     */
    public static String getSessionPayloadTopic(String sessionId) {
        return MessageFormat.format(SessionStompPath.SESSION_PAYLOAD_TOPIC_TEMPLATE, sessionId);
    }

    /**
     *
     * @param sessionId
     * @return
     */
    public static String getSessionAnalyticsTopic(String sessionId) {
        return MessageFormat.format(SessionStompPath.SESSION_ANALYTICS_TOPIC_TEMPLATE, sessionId);
    }

    /**
     *
     * @param sessionId
     * @return
     */
    public static String getSessionErrorsTopic(String sessionId) {
        return MessageFormat.format(SessionStompPath.SESSION_ERRORS_TOPIC_TEMPLATE, sessionId);
    }

}
