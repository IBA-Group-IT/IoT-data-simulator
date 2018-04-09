package com.iba.iot.datasimulator.session.service.active.processing.sender;

import com.iba.iot.datasimulator.common.model.TargetSystemType;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.constant.SessionStompPath;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionProcessedPayload;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionState;
import com.iba.iot.datasimulator.session.service.active.processing.sender.processor.TargetSystemProcessor;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayloadSenderImpl implements PayloadSender {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(PayloadSenderImpl.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private List<TargetSystemProcessor> targetSystemProcessors;

    @Override
    public void send(String sessionId, ActiveSessionPayload payload, TargetSystem targetSystem) {

        logger.debug(">>> Processing payload sender for payload {}", payload);

        Device device = payload.getDevice();
        targetSystemProcessors.forEach(processor -> processor.process(targetSystem, device));

        ActiveSessionProcessedPayload payloadToSend = buildPayload(sessionId, targetSystem,
                payload.getGeneratedPayload(), payload.getState());

        String targetQueueName = getTargetQueueName(targetSystem);
        messagingTemplate.convertAndSend(targetQueueName, payloadToSend);
    }

    /**
     *
     * @param sessionId
     * @param targetSystem
     * @param generatedPayload
     * @return
     */
    private ActiveSessionProcessedPayload buildPayload(String sessionId, TargetSystem targetSystem,
                                                       String generatedPayload, ActiveSessionState state) {

        ActiveSessionProcessedPayload payload = new ActiveSessionProcessedPayload();

        payload.setSessionId(sessionId);
        payload.setTarget(targetSystem);
        payload.setPayload(generatedPayload);
        payload.setState(state);

        return payload;
    }

    /**
     *
     * @param targetSystem
     * @return
     */
    private String getTargetQueueName(TargetSystem targetSystem) {

        TargetSystemType type = targetSystem.getType();
        switch (type) {

            case MQTT_BROKER:
                return SessionStompPath.MQTT_QUEUE;

            case AMQP_BROKER:
                return SessionStompPath.AMQP_QUEUE;

            case REST_ENDPOINT:
                return SessionStompPath.REST_QUEUE;

            case WEBSOCKET_ENDPOINT:
                return SessionStompPath.WEBSOCKET_QUEUE;

            case DUMMY:
                return SessionStompPath.DUMMY_QUEUE;

            case KAFKA_BROKER:
                return SessionStompPath.KAFKA_QUEUE;

            case S3:
                return SessionStompPath.S3_QUEUE;
        }

        logger.error(">>> Cannot find stomp queue for target system type: {}", type);
        throw new RuntimeException("Payload sender error: unsupported target system type.");
    }
}
