package com.iba.iot.datasimulator.session.service.active.entity;

import com.iba.iot.datasimulator.common.util.ExceptionUtil;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionState;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionStatus;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionAnalyticTag;
import com.iba.iot.datasimulator.session.service.active.processing.producer.DataProducer;
import com.iba.iot.datasimulator.session.service.active.processing.sender.PayloadSender;
import com.iba.iot.datasimulator.session.service.active.processing.state.SessionStateManager;
import com.iba.iot.datasimulator.session.util.ActiveSessionUtil;
import io.reactivex.Observable;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ActiveSessionEntity implements ActiveSession {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ActiveSessionEntity.class);

    @Value("${active.session.fail.errors.counter}")
    private int errorsThreshold;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private PayloadSender payloadSender;

    @Autowired
    private SessionStateManager sessionStateManager;

    /** **/
    private Session session;

    /** **/
    private String sessionId;

    /** **/
    private DataProducer dataProducer;

    /** **/
    private Observable<ActiveSessionPayload> processingPipeline;

    /** **/
    private ActiveSessionState state;

    /** **/
    private Collection<String> errors = new ArrayList<>();

    /** **/
    private boolean isStopped;

    /**
     *
     * @param session
     * @param dataProducer
     * @param processingPipeline
     */
    public ActiveSessionEntity(Session session, DataProducer dataProducer, Observable<ActiveSessionPayload> processingPipeline) {

        this.session = session;
        this.sessionId = session.getId().toString();
        this.dataProducer = dataProducer;
        this.processingPipeline = processingPipeline;
    }

    @Override
    public void start() {

        logger.info(">>> Session {} has been started.", sessionId);

        processingPipeline.subscribe(
            this::processPayload,
            this::processError,
            this::processCompletion
        );

        updateState(ActiveSessionState.RUNNING);
        sendAnalyticsMessage(ActiveSessionAnalyticTag.SESSION_STARTED);
    }

    @Override
    public void pause() {

        logger.info(">>> Session {} has been paused", sessionId);

        dataProducer.pause();
        updateState(ActiveSessionState.PAUSED);
        sendAnalyticsMessage(ActiveSessionAnalyticTag.SESSION_PAUSED);
    }

    @Override
    public void resume() {

        logger.info(">>> Session {} has been resumed", sessionId);

        dataProducer.resume();
        updateState(ActiveSessionState.RUNNING);
        sendAnalyticsMessage(ActiveSessionAnalyticTag.SESSION_RESUMED);
    }

    @Override
    public void stop() {

        logger.info(">>> Session {} has been stopped", sessionId);
        isStopped = true;
        dataProducer.stop();
    }

    /**
     *
     */
    private void processCompletion() {

        if (state != ActiveSessionState.FAILED && state != ActiveSessionState.COMPLETED) {

            logger.info(">>> Session {} has been completed", sessionId);
            updateState(ActiveSessionState.COMPLETED);
            sendSessionCompletedPayload();
            sendAnalyticsMessage(ActiveSessionAnalyticTag.SESSION_COMPLETED);
            sessionStateManager.cleanupStateForSession(sessionId);
        }
    }

    /**
     *
     * @param payload
     */
    private void processPayload(ActiveSessionPayload payload) {

        try{
            payloadSender.send(sessionId, payload, SerializationUtils.clone(session.getTargetSystem()));
        } catch (Exception exception) {
            processError(exception);
        }
    }

    /**
     *
     * @param exception
     */
    private void processError(Throwable exception) {

        String errorMessage = ExceptionUtil.getErrorMessage(exception);
        logger.error(">>> An error occurred during session {} payload sending: {}", sessionId, errorMessage);
        logger.error("Original error", exception);

        registerError(errorMessage);
        ActiveSessionUtil.sendErrorMessage(sessionId, errorMessage, messagingTemplate);
        updateState(ActiveSessionState.FAILED);
        sendAnalyticsMessage(ActiveSessionAnalyticTag.SESSION_FAILED);
        sendSessionFailedPayload();
        dataProducer.stop();
        sessionStateManager.cleanupStateForSession(sessionId);
    }

    @Override
    public void registerError(String error) {

        errors.add(error);
        if (errors.size() >= errorsThreshold && state != ActiveSessionState.FAILED) {

            logger.error(">>> Session {} has been failed as exceeded max errors number.", sessionId);
            updateState(ActiveSessionState.FAILED);
            dataProducer.stop();
            sendAnalyticsMessage(ActiveSessionAnalyticTag.SESSION_FAILED);
            sendSessionFailedPayload();
        }
    }

    @Override
    public ActiveSessionStatus getStatus() {

        ActiveSessionStatus activeSessionStatus = new ActiveSessionStatus(sessionId, state);
        if (state == ActiveSessionState.FAILED) {
            activeSessionStatus.setErrors(errors);
        }

        logger.debug(">>> Providing session {} status: {}", sessionId, activeSessionStatus);
        return activeSessionStatus;
    }

    /**
     *
     * @param state
     */
    private void updateState(ActiveSessionState state) {

        logger.debug(">>> Session {} state has been updated to: {}", state);
        this.state = state;
        ActiveSessionUtil.sendStatusUpdate(sessionId, getStatus(), messagingTemplate);
    }

    /**
     *
     * @param tag
     */
    private void sendAnalyticsMessage(ActiveSessionAnalyticTag tag) {
        ActiveSessionUtil.sendAnalyticsMessage(sessionId, tag, messagingTemplate);
    }

    /**
     *
     */
    private void sendSessionFailedPayload() {
        payloadSender.send(sessionId, new ActiveSessionPayload(ActiveSessionState.FAILED), session.getTargetSystem());
    }

    /**
     *
     */
    private void sendSessionCompletedPayload() {
        payloadSender.send(sessionId, new ActiveSessionPayload(ActiveSessionState.COMPLETED), session.getTargetSystem());
    }

    @Override
    public boolean isReplayLooped() {
        return session.isReplayLooped();
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }
}
