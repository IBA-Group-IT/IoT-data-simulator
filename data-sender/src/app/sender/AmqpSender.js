let amqp = require('amqplib');
let logger = require('../common/log');
let CommonSender = require('./CommonSender');
let Constants = require('../common/Constants');
let SecurityBuilder = require('../security/SecurityBuilder');
const { URL } = require('url');

class AmqpSender extends CommonSender {

    /**
     *
     */
    constructor() {

        super();
    }

    /**
     *
     * @returns {string}
     */
    getAmqpQueue() {
        return Constants.AMQP_QUEUE;
    }

    /**
     *
     * @param securityType
     * @returns {*}
     */
    deriveTargetSystemProtocolPrefix(securityType) {

        switch (securityType) {

            case Constants.SECURITY_CERTIFICATE:
                return Constants.AMQP_TLS_PREFIX;

            default:
                return Constants.AMQP_PREFIX;
        }
    }

    /**
     *
     * @param sessionId
     * @param target
     * @param payload
     * @returns {Promise.<void>}
     */
    async sendPayload(sessionId, target, payload) {

        logger.debug(">>> Amqp send payload: processing amqp send payload.");

        let connectionMapKey = JSON.stringify(target);
        let clientConnection = this.clientConnections.get(connectionMapKey);

        logger.debug(`>>> Amqp send payload: sending payload ${payload} for session ${sessionId} to the amqp broker: ${JSON.stringify(target)}.`);

        if (!clientConnection) {

            let options = this.getClientConnectionOptions(target);
            clientConnection = await amqp.connect(target.url, options);
            this.clientConnections.set(connectionMapKey, clientConnection);

            clientConnection.on('error', (error) => {

                logger.error(`>>> Amqp connection has been failed due to error: ${JSON.stringify(error.message)}`);
                this.clientConnections.delete(connectionMapKey);
            });

            clientConnection.on('close', (error) => {

                logger.debug(`>>> Amqp connection has been closed`);
                this.clientConnections.delete(connectionMapKey);
                if (error) {
                    logger.error(`>>> Amqp connection has been closed due to error: ${JSON.stringify(error.message)}`);
                }
            });
        }

        let channel = await clientConnection.createChannel();

        await channel.assertQueue(target.queue, {durable: true});

        //In case of payload serialization, we need to send raw payload, without convert to Buffer
        if (typeof payload !== "string") {
            await channel.sendToQueue(target.queue, payload);
        } else {
            await channel.sendToQueue(target.queue, Buffer.from(payload, 'utf8'));
        }
    }

    /**
     *
     * @param target
     * @returns {*}
     */
    getClientConnectionOptions(target) {

        logger.debug(`>>> Opening amqp connection to target: ${JSON.stringify(target)}`);

        let options = {rejectUnauthorized: false};
        let securityData = this.getSecurityData(target.security);

        if (target.security) {
            switch(target.security.type) {

                case Constants.SECURITY_CERTIFICATE: {
                    Object.assign(options, securityData);
                    break;
                }

                case Constants.SECURITY_CREDENTIALS: {

                    Object.assign(options, securityData);
                    target.url = this.generateAmqpUrl(target.url, options);
                    break;
                }
            }
        }

        return options;
    }

    /**
     *
     * @param security
     * @returns {*}
     */
    getSecurityData(security = {}) {

        if (!security.type) {
            return {};
        }

        switch(security.type) {

            case Constants.SECURITY_CREDENTIALS: {
                return SecurityBuilder.getCredentials(security);
            }

            case Constants.SECURITY_CERTIFICATE: {

                return SecurityBuilder.getCertificates(security);
            }

            default: {
                throw new Error(`${security.type} doesn't supported`);
            }
        }

    }

    /**
     *
     * @param targetUrl
     * @param options
     * @returns {string}
     */
    generateAmqpUrl(targetUrl, options) {

        logger.debug(`>>> Amqp send payload: generating consumer url.`);

        let urlObj = URL.parse(targetUrl);
        let protocol = urlObj.protocol;

        return `${protocol}//${options.username}:${options.password}@${urlObj.host}`;
    }
}

module.exports = AmqpSender;