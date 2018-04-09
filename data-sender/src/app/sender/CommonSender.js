let logger = require('../common/log');
let Constants = require('../common/Constants');
let senderUtils = require('../common/senderUtils');
let fsExtra = require("fs-extra");
let entitySerializer = require('../serializer/EntitySerializer');

fsExtra.ensureDir(Constants.DATASETS_FOLDER);

class CommonSender {

    constructor() {
        this.instance = this;
        this.channel = null;
        this.clientConnections = new Map();
        this.sessionStates = new Map();
    }

    /**
     *
     * @param channel
     * @returns {Promise.<void>}
     */
    async init(channel) {

        logger.debug(">>> Common sender initializing.");

        this.channel = channel;
        await this.listenSenderPayloadQueue();
    }

    /**
     *
     * @returns {Promise.<void>}
     */
    async listenSenderPayloadQueue() {

        let amqpQueue = this.getAmqpQueue();
        logger.debug(`>>> Getting and listening ${amqpQueue} AMQP queue.`);

        await this.channel.assertQueue(amqpQueue, {durable: true});
        this.channel.consume(amqpQueue, (message) => this.consumeMessage(message), {noAck: true});
    }

    /**
     *
     * @param message
     */
    async consumeMessage(message) {

        let rawData = message.content.toString();
        let data = JSON.parse(rawData);
        logger.debug(`>>> Consuming message ${rawData} from topic: ${this.getAmqpQueue()}`);

        let {sessionId, target, payload, state} = data;
        this.processTargetSystemProtocol(target);

        await this.processMessage(sessionId, target, payload, state);
    }

    /**
     *
     */
    processTargetSystemProtocol(target) {

        let targetSystemProtocol = senderUtils.parseProtocol(target.url);
        if (!targetSystemProtocol && this.deriveTargetSystemProtocolPrefix) {

            let securityType = this.getTargetSystemSecurityType(target);
            let protocolPrefix = this.deriveTargetSystemProtocolPrefix(securityType);
            logger.debug(`>>> Injecting ${protocolPrefix} protocol prefix into target url: ${target.url}.`);
            target.url = protocolPrefix + target.url;

        } else {
            logger.debug(`>>> Skipping protocol deriving for the target system url: ${target.url}`);
        }
    }

    /**
     *
     * @param target
     */
    getTargetSystemSecurityType(target) {
        if (target && target.security) {
            return target.security.type;
        }
    }

    /**
     *
     * @param sessionId
     * @param target
     * @param payload
     * @param state
     * @returns {Promise.<void>}
     */
    async processMessage(sessionId, target, payload, state) {

        try {

            switch (state) {

                case Constants.SESSION_COMPLETED:
                    this.sessionStates.set(sessionId, Constants.SESSION_COMPLETED);
                    await this.processSessionCompletedMessage(sessionId, target);
                    break;

                case Constants.SESSION_FAILED:
                    this.sessionStates.set(sessionId, Constants.SESSION_FAILED);
                    await this.processSessionFailedMessage(sessionId, target);
                    break;

                default:
                    this.sessionStates.set(sessionId, Constants.SESSION_RUNNING);
                    await this.processSessionPayloadMessage(sessionId, target, payload);

            }

        } catch (error) {

            logger.error(`>>> An error '${error.message}' occurred on sending session ${sessionId} payload 
                                       '${JSON.stringify(payload)}' to the target system '${JSON.stringify(target)}'`);

            // Sending errors back to UI only in case if session is still running
            if (this.sessionStates.get(sessionId) === Constants.SESSION_RUNNING) {
                await this.sendError(sessionId, error.message);
            }
        }
    }

    /**
     *
     * @param sessionId
     * @param target
     * @returns {Promise.<void>}
     */
    async processSessionCompletedMessage(sessionId, target) {

        if (this.handleSessionCompletion) {

            logger.debug(`>>> Invoking completion handler for session ${sessionId}`);
            await this.handleSessionCompletion(sessionId, target);
        }
    }

    /**
     *
     * @param sessionId
     * @param target
     * @returns {Promise.<void>}
     */
    async processSessionFailedMessage(sessionId, target) {

        if (this.handleSessionFailure) {

            logger.debug(`>>> Invoking failure handler for session ${sessionId}`);
            await this.handleSessionFailure(sessionId, target);
        }
    }

    /**
     *
     * @param sessionId
     * @param target
     * @param payload
     * @returns {Promise.<void>}
     */
    async processSessionPayloadMessage(sessionId, target, payload) {

        logger.debug(`>>> Sending session ${sessionId} payload message ${JSON.stringify(payload)} to target system ${JSON.stringify(target)}.`);

        if (target[Constants.MESSAGE_SERIALIZER]) {
            let serializedPayload = await entitySerializer.serialize(target, Constants.MESSAGE_SERIALIZER, payload);
            await this.sendPayload(sessionId, target, serializedPayload);
        } else {
            await this.sendPayload(sessionId, target, payload);
        }

        logger.debug(`>>> Sending confirmation message for session ${sessionId}  and payload ${JSON.stringify(payload)}.`);
        await this.sendConfirmation(sessionId, payload);
    }

    /**
     *
     * @param sessionId
     * @param target
     * @param payload
     */
    sendPayload(sessionId, target, payload) {}

    /**
     *
     * @param sessionId
     * @param payload
     * @returns {Promise.<void>}
     */
    async sendConfirmation(sessionId, payload) {

        let topic = `sessions.${sessionId}.payload`;
        let generatedPayload = this.getConfirmationPayload(sessionId, payload);

        await this.publishMessage(topic, generatedPayload)
    }

    /**
     *
     * @param sessionId
     * @param error
     * @returns {Promise.<void>}
     */
    async sendError(sessionId, error) {

        let topic = `sessions.${sessionId}.errors`;
        let generatedPayload = this.getErrorPayload(sessionId, error);
        await this.publishMessage(topic, generatedPayload)
    }

    /**
     *
     * @param sessionId
     * @param payload
     */
    getConfirmationPayload(sessionId, payload) {

        let generatedPayload = {
            type: Constants.CONFIRMATION_TYPE,
            sessionId: sessionId,
            message: payload,
            timestamp: Date.now()
        };

        let message = JSON.stringify(generatedPayload);
        logger.debug(`>>> Sending session ${sessionId} confirmation payload message ${message}.`);
        return message;
    }

    /**
     *
     * @param sessionId
     * @param error
     */
    getErrorPayload(sessionId, error) {

        let generatedPayload = {
            type: Constants.ERROR_TYPE,
            sessionId: sessionId,
            message: error,
            timestamp: Date.now()
        };

        let errorMessage = JSON.stringify(generatedPayload);
        logger.debug(`>>> Sending session ${sessionId} error payload message ${errorMessage}.`);
        return errorMessage;
    }

    /**
     *
     * @param topic
     * @param payload
     * @returns {Promise.<void>}
     */
    async publishMessage(topic, payload) {

        await this.channel.assertExchange(Constants.AMQP_TOPIC_EXCHANGE, "topic", {durable: true});
        this.channel.publish(Constants.AMQP_TOPIC_EXCHANGE, topic, Buffer.from(payload, 'utf8'));
    }
}

module.exports = CommonSender;