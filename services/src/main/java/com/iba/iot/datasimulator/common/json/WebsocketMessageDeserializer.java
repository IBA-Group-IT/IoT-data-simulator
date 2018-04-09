package com.iba.iot.datasimulator.common.json;

import com.iba.iot.datasimulator.session.model.active.message.*;

/**
 *
 */
public class WebsocketMessageDeserializer extends TypedPolymorphicDeserializer<Message, MessageType> {

    @Override
    protected MessageType parseType(String rawType) {
        return MessageType.fromString(rawType);
    }

    @Override
    protected Class<? extends Message> determineConcreteType(MessageType messageType) {

        switch (messageType) {

            case SESSIONS_STATUS_COMMAND_RESULT:
                return ActiveSessionsStatusCommandResultMessage.class;

            case SESSION_STATUS:
                return ActiveSessionStatusMessage.class;

            case SESSION_ERROR:
                return ActiveSessionErrorMessage.class;

            default:
                return null;
        }
    }
}
