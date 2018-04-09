package com.iba.iot.datasimulator.common.json;

import com.iba.iot.datasimulator.common.model.TargetSystemType;
import com.iba.iot.datasimulator.target.model.*;

/**
 *
 */
public class TargetSystemDeserializer extends TypedPolymorphicDeserializer<TargetSystem, TargetSystemType> {

    @Override
    protected TargetSystemType parseType(String rawType) {
        return TargetSystemType.fromString(rawType);
    }

    @Override
    protected Class<? extends TargetSystem> determineConcreteType(TargetSystemType targetSystemType) {

        switch (targetSystemType) {

            case MQTT_BROKER:
                return MqttTargetSystem.class;

            case WEBSOCKET_ENDPOINT:
                return WebsocketTargetSystem.class;

            case REST_ENDPOINT:
                return RestTargetSystem.class;

            case DUMMY:
                return DummyTargetSystem.class;

            case AMQP_BROKER:
                return AmqpTargetSystem.class;

            case KAFKA_BROKER:
                return KafkaTargetSystem.class;

            case S3:
                return S3TargetSystem.class;

            default:
                return null;
        }

    }
}
