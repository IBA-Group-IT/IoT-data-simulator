package com.iba.iot.datasimulator.session.constant;

/**
 *
 */
public interface SessionStompPath {

    /** **/
    String TOPIC = "/topic/";

    /** **/
    String QUEUE = "/queue/";

    /** **/
    String ERRORS_QUEUE = QUEUE + "error";

    /** **/
    String SESSIONS_QUEUE = QUEUE + "sessions";

    /** **/
    String SESSION_QUEUE_TEMPLATE = SESSIONS_QUEUE + ".{0}";

    /** **/
    String SESSIONS_TOPIC = TOPIC + "sessions";

    /** **/
    String SESSION_TOPIC_TEMPLATE = SESSIONS_TOPIC + ".{0}";

    /** **/
    String SESSION_PAYLOAD_TOPIC_TEMPLATE = SESSIONS_TOPIC + ".{0}.payload";

    /** **/
    String SESSION_ANALYTICS_TOPIC_TEMPLATE = SESSIONS_TOPIC + ".{0}.analytics";

    /** **/
    String SESSION_ERRORS_TOPIC_TEMPLATE = SESSIONS_TOPIC + ".{0}.errors";

    /** **/
    String SENDERS_PREFIX = "senders.";

    /** **/
    String MQTT_QUEUE = SessionStompPath.QUEUE + SENDERS_PREFIX + "mqtt";

    /** **/
    String AMQP_QUEUE = SessionStompPath.QUEUE + SENDERS_PREFIX + "amqp";

    /** **/
    String S3_QUEUE = SessionStompPath.QUEUE + SENDERS_PREFIX + "s3";

    /** **/
    String REST_QUEUE = SessionStompPath.QUEUE + SENDERS_PREFIX + "rest";

    /** **/
    String WEBSOCKET_QUEUE = SessionStompPath.QUEUE + SENDERS_PREFIX + "ws";

    /** **/
    String DUMMY_QUEUE = SessionStompPath.QUEUE + SENDERS_PREFIX + "dummy";

    /** **/
    String KAFKA_QUEUE = SessionStompPath.QUEUE + SENDERS_PREFIX + "kafka";
}
