let logger = require('../common/log');
let CommonSender = require('./CommonSender');
let Constants = require('../common/Constants');
let SecurityBuilder = require('../security/SecurityBuilder');
let utils = require('../common/senderUtils');
let entitySerializer = require('../serializer/EntitySerializer');
let kafka = require('kafka-node');
let Producer = kafka.Producer;
let KeyedMessage = kafka.KeyedMessage;
let KafkaClient = kafka.KafkaClient;


class KafkaSender extends CommonSender {

    /**
     *
     */
    constructor() {
        super();
        this.keyFunctions = new Map();
    }

    /**
     *
     * @returns {string}
     */
    getAmqpQueue() {
        return Constants.KAFKA_QUEUE;
    }

    /**
     *
     * @param sessionId
     * @param target
     * @param payload
     * @returns {Promise.<void>}
     */
    async sendPayload(sessionId, target, payload) {

        logger.debug(">>> Kafka send payload: processing kafka send payload.");

        let connectionMapKey = JSON.stringify(target);
        let clientConnection = this.clientConnections.get(connectionMapKey);

        if (!clientConnection) {
            clientConnection = await this.getClientConnection(target);
            this.clientConnections.set(connectionMapKey, clientConnection);
        }

        logger.debug(`>>> Kafka send payload: sending payload ${payload} for session ${sessionId} to the kafka target system: ${JSON.stringify(target)}.`);
        await this.sendKafkaPayload(target, payload, clientConnection);
    }

    /**
     *
     * @param target
     * @returns {Promise.<void>}
     */
    async getClientConnection(target, connectionMapKey) {

        logger.debug(`>>> Opening kafka connection to target: ${JSON.stringify(target)}`);

        let client = await this.buildKafkaClient(target);
        let producer = new Producer(client, { requireAcks: 1 });

        return new Promise((resolve, reject) => {

            producer.on("ready", () => {
                logger.debug(`>>> Kafka producer connection has been opened.`);
                resolve(producer);
            });

            producer.on('error', (error) => {
                logger.error(`>>> Kafka producer connection has been failed due to error: ${JSON.stringify(error)}`);
                this.clientConnections.delete(connectionMapKey);
                reject(error);
            });
        });
    }

    /**
     *
     * @param target
     * @param security
     * @returns {Promise<KafkaClient>}
     */
    async buildKafkaClient(target) {

        let security = await this.getSecurityData(target.security);
        if (security) {
            return new KafkaClient({kafkaHost:target.url, connectTimeout: 5000, requestTimeout: 5000, sslOptions: security});
        } else {
            return new KafkaClient({kafkaHost:target.url, connectTimeout: 5000, requestTimeout: 5000});
        }
    }

    /**
     *
     * @param target
     * @param payload
     * @param clientConnection
     * @returns {Promise}
     */
    async sendKafkaPayload(target, payload, clientConnection) {

        let key = this.getKafkaKey(target, payload);
        let message = await this.buildKafkaMessage(target, payload, key);

        return new Promise((resolve, reject) => {

            clientConnection.send([{topic: target.topic, messages: message}],

                function(err, result) {
                    if(err){
                        reject(err);
                    } else {
                        resolve(result);
                    }
                }
            )
        })
    }

    /**
     *
     * @param target
     * @param payload
     * @param key
     * @returns {Promise<void>}
     */
    async buildKafkaMessage(target, payload, key) {

        if (key) {
            return await this.buildKeyedMessage(target, payload, key);
        } else {
            return this.buildMessage(target, payload);
        }
    }

    /**
     *
     * @param target
     * @param payload
     * @param key
     * @returns {Promise<*>}
     */
    async buildKeyedMessage(target, payload, key) {

        if (target.keySerializer) {
            return await this.getEncryptedKeyedMessage(target, payload, key);
        } else if (target.messageSerializer || utils.isStringValue(payload)) {
            return new KeyedMessage(key, payload);
        } else {
            return new KeyedMessage(key, JSON.stringify(payload));
        }
    }

    /**
     *
     * @param target
     * @param payload
     * @returns {Promise<*>}
     */
    buildMessage(target, payload) {

        if (target.messageSerializer || utils.isStringValue(payload)) {
            return payload;
        } else {
            return JSON.stringify(payload);
        }
    }

    /**
     *
     * @param target
     * @param payload
     * @returns {*}
     */
    getKafkaKey(target, payload) {

        if (target.keyFunction) {

            let keyFunction = this.keyFunctions.get(target.keyFunction);
            if (!keyFunction) {

                keyFunction = eval('(' + target.keyFunction + ')');
                this.keyFunctions.set(target.keyFunction, keyFunction);
            }

            return keyFunction(payload, target.topic);
        }
    }

    /**
     *
     * @param target
     * @param payload
     * @param kafkaKey
     * @returns {*}
     */
    async getEncryptedKeyedMessage(target, payload, kafkaKey) {

        logger.debug(">>> Getting keyed message using protobuf");
        let encryptedKey = await entitySerializer.serialize(target, Constants.KEY_SERIALIZER, kafkaKey);
        return new KeyedMessage(encryptedKey, payload);
    }


    /**
     *
     * @param security
     * @returns {*}
     */
    async getSecurityData(security = {}) {

        return new Promise((resolve, reject) => {

            if (!security.type) {
                return resolve(null);
            }

            switch(security.type) {

                case Constants.SECURITY_CERTIFICATE: {

                    resolve (SecurityBuilder.getCertificates(security));
                    break;
                }

                default: {
                    reject(new Error(`${security.type} doesn't supported`));
                }
            }
        });
    }

    /**
     *
     * @param sessionId
     * @param target
     */
    handleSessionFailure(sessionId, target) {
        this.closeConnection(sessionId, target);
    }

    /**
     *
     */
    closeConnection() {

        let connectionMapKey = JSON.stringify(target);
        let clientConnection = this.clientConnections.get(connectionMapKey);

        if (clientConnection) {

            clientConnection.client.close();
            logger.debug(`>>> Removing client connection for session ${sessionId} from cache.`);
            this.clientConnections.delete(connectionMapKey);
        }
    }
}

module.exports = KafkaSender;