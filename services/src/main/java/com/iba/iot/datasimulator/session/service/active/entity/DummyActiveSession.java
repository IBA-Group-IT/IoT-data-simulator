package com.iba.iot.datasimulator.session.service.active.entity;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionState;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionStatus;
import com.iba.iot.datasimulator.session.model.active.message.*;
import com.iba.iot.datasimulator.session.util.StompUtil;
import com.iba.iot.datasimulator.common.util.TimeUtil;
import lombok.Data;
import lombok.ToString;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Random;

@Data
@ToString
public class DummyActiveSession implements ActiveSession {

    /** **/
    private final static double ERROR_PROBABILITY = 0.8d;

    /**
     *
     * @param sessionId
     * @param messagingTemplate
     */
    public DummyActiveSession(String sessionId, SimpMessagingTemplate messagingTemplate) {
        this.sessionId = sessionId;
        this.messagingTemplate = messagingTemplate;
    }

    /** **/
    private String sessionId;

    /** **/
    private SimpMessagingTemplate messagingTemplate;

    /** **/
    private ActiveSessionState state;

    @Override
    public void start() {

        state = ActiveSessionState.RUNNING;
        sendStatusUpdate();
        sendAnalyticsMessage(ActiveSessionAnalyticTag.SESSION_STARTED, "started.");

        Runnable task = () -> {

            Random random = new Random();

            while (state == ActiveSessionState.RUNNING) {

                try {
                    Thread.sleep(random.nextInt(5000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (state == ActiveSessionState.RUNNING) {

                    double dice = random.nextDouble();
                    if (dice <= ERROR_PROBABILITY) {
                        sendPayloadMessage(random);
                    } else {
                        sendErrorMessage();
                    }
                }
            }
        };

        new Thread(task).start();
    }

    /**
     *
     * @param random
     */
    private void sendPayloadMessage(Random random) {

        ActiveSessionPayloadMessage payloadMessage = new ActiveSessionPayloadMessage(sessionId,
                "Random generatedPayload: " + random.nextInt(1000),
                TimeUtil.getUnixTime());
        messagingTemplate.convertAndSend(StompUtil.getSessionPayloadTopic(sessionId), payloadMessage);
    }

    /**
     *
     */
    private void sendErrorMessage() {

        ActiveSessionErrorMessage errorMessage = new ActiveSessionErrorMessage(sessionId, "java.lang.RuntimeException: terrible error, dude !!!",
                TimeUtil.getUnixTime());
        messagingTemplate.convertAndSend(StompUtil.getSessionErrorsTopic(sessionId), errorMessage);
    }

    @Override
    public void pause() {
        state = ActiveSessionState.PAUSED;
        sendStatusUpdate();
        sendAnalyticsMessage(ActiveSessionAnalyticTag.SESSION_PAUSED, "paused.");
    }

    @Override
    public void resume() {
        start();
    }

    @Override
    public void stop() {
        state = ActiveSessionState.COMPLETED;
        sendStatusUpdate();
        sendAnalyticsMessage(ActiveSessionAnalyticTag.SESSION_COMPLETED, "completed.");
    }

    @Override
    public ActiveSessionStatus getStatus() {
        return new ActiveSessionStatus(sessionId, state, null);
    }

    @Override
    public void registerError(String error) {

    }

    /**
     *
     */
    private void sendStatusUpdate() {

        ActiveSessionStatusMessage message = new ActiveSessionStatusMessage(getStatus(), TimeUtil.getCurrentTime());
        messagingTemplate.convertAndSend(StompUtil.getSessionTopic(sessionId), message);
    }

    private void sendAnalyticsMessage(ActiveSessionAnalyticTag tag, String messagePostfix) {

        ActiveSessionAnalyticsMessage activeSessionAnalyticsMessage = new ActiveSessionAnalyticsMessage();
        activeSessionAnalyticsMessage.setSessionId(sessionId);
        activeSessionAnalyticsMessage.setTag(tag);
        activeSessionAnalyticsMessage.setTimestamp(TimeUtil.getUnixTime());
        activeSessionAnalyticsMessage.setMessage("Session " + sessionId + " has been " + messagePostfix);

        messagingTemplate.convertAndSend(StompUtil.getSessionAnalyticsTopic(sessionId), activeSessionAnalyticsMessage);
    }

    @Override
    public boolean isReplayLooped() {
        return false;
    }

    @Override
    public boolean isStopped() {
        return false;
    }
}
