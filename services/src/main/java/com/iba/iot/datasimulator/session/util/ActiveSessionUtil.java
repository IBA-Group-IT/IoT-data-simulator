package com.iba.iot.datasimulator.session.util;

import com.iba.iot.datasimulator.common.util.StringUtil;
import com.iba.iot.datasimulator.common.util.TimeUtil;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionState;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionStatus;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionAnalyticTag;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionAnalyticsMessage;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionErrorMessage;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionStatusMessage;
import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 *
 */
public class ActiveSessionUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ActiveSessionUtil.class);

    /**
     *
     * @param sessionId
     * @param status
     * @param messagingTemplate
     */
    public static void sendStatusUpdate(String sessionId, ActiveSessionStatus status, SimpMessagingTemplate messagingTemplate) {

        ActiveSessionStatusMessage message = new ActiveSessionStatusMessage(status, TimeUtil.getCurrentTime());

        logger.debug(">>> Sending status update message: {}", message);
        messagingTemplate.convertAndSend(StompUtil.getSessionTopic(sessionId), message);
    }

    /**
     *
     * @param sessionId
     * @param tag
     * @param messagingTemplate
     */
    public static void sendAnalyticsMessage(String sessionId, ActiveSessionAnalyticTag tag, SimpMessagingTemplate messagingTemplate) {

        ActiveSessionAnalyticsMessage analyticsMessage = new ActiveSessionAnalyticsMessage();

        analyticsMessage.setMessage(StringUtil.EMPTY_STRING);
        analyticsMessage.setTimestamp(TimeUtil.getCurrentTime());
        analyticsMessage.setTag(tag);
        analyticsMessage.setSessionId(sessionId);

        logger.debug(">>> Sending analytics message: {}", analyticsMessage);
        messagingTemplate.convertAndSend(StompUtil.getSessionAnalyticsTopic(sessionId), analyticsMessage);
    }

    /**
     *
     * @param sessionId
     * @param errorMessage
     * @param messagingTemplate
     */
    public static void sendErrorMessage(String sessionId, String errorMessage, SimpMessagingTemplate messagingTemplate) {

        ActiveSessionErrorMessage message = new ActiveSessionErrorMessage(sessionId, errorMessage, TimeUtil.getCurrentTime());

        logger.debug(">>> Sending error message: {}", message);
        messagingTemplate.convertAndSend(StompUtil.getSessionErrorsTopic(sessionId), message);
    }

    /**
     *
     * @param session
     * @return
     */
    public static ActiveSessionState getSessionState(ActiveSession session) {

        if (session != null) {
            return session.getStatus().getState();
        }

        return null;
    }

}
