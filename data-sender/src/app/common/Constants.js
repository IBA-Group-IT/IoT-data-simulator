let os = require('os');

module.exports = {

    URL_PROTOCOL_DELIMITER: "//",

    SESSION_RUNNING: "running",
    SESSION_COMPLETED: "completed",
    SESSION_FAILED: "failed",

    AMAZON_URL: "amazonaws.com",

    MQTT_PREFIX: "mqtt://",
    MQTTS_PREFIX: "mqtts://",
    MQTT_WSS_PREFIX: "mqtts://",
    WS_PREFIX: "ws://",
    WSS_PREFIX: "wss://",
    HTTPS_PREFIX: "https://",
    HTTP_PREFIX: "http://",
    AMQP_PREFIX: "amqp://",
    AMQP_TLS_PREFIX: "amqps://",
    KAFKA_PLAIN_PREFIX: "plaintext://",
    KAFKA_TLS_PREFIX: "ssl://",

    AWS_WS_PROTOCOL: "wss",

    AMQP_QUEUE: "senders.amqp",
    MQTT_QUEUE: "senders.mqtt",
    WEBSOCKET_QUEUE: "senders.ws",
    KAFKA_QUEUE: "senders.kafka",
    S3_QUEUE: "senders.s3",
    DUMMY_QUEUE: "senders.dummy",
    REST_QUEUE: "senders.rest",

    AMQP_TOPIC_EXCHANGE: "amq.topic",

    SECURITY_CREDENTIALS: "credentials",
    SECURITY_CERTIFICATE: "certificates",
    SECURITY_TOKEN: "access_token",
    SECURITY_ACCESS_KEYS: "access_keys",

    BROKER_TYPE_MQTT: "mqtt_broker",
    BROKER_TYPE_KAFKA: "kafka_broker",
    BROKER_TYPE_AMQP: "amqp_broker",
    BROKER_TYPE_DUMMY: "dummy",
    BROKER_TYPE_REST: "rest_endpoint",
    BROKER_TYPE_WS: "websocket_endpoint",

    CONFIRMATION_TYPE: "session_payload",
    ERROR_TYPE: "session_error",

    TARGET_CONNECTION_ERROR: "Cannot connect to target system - ",
    AMQP_DEFAULT_CREDS: "guest:guest",

    KAFKA_MESSAGE_KEY_PREFIX: "IoT_Sim_",

    HTTP_POST_METHOD: "POST",
    UNDEFINED_HOST: "undefined",

    SERIALIZER_TYPE_PROTOBUF: "protobuf",
    KEY_SERIALIZER: "keySerializer",
    MESSAGE_SERIALIZER: "messageSerializer",
    DATASETS_FOLDER : os.tmpdir() + '/sessions/',

    CERTS_FOLDER: "/usr/data-sender/certificates/",
    CA_FILENAME: "ca.crt",
    CERT_FILENAME: "server.crt",
    KEY_FILENAME: "key.pem"
};