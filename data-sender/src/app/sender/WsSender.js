let logger = require('../common/log');
let CommonSender = require('./CommonSender');
let Constants = require('../common/Constants');
let WebSocketClient = require('websocket').client;
let SecurityBuilder = require('../security/SecurityBuilder');

class WsSender extends CommonSender {

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
        return Constants.WEBSOCKET_QUEUE;
    }

    /**
     *
     * @param securityType
     */
    deriveTargetSystemProtocolPrefix(securityType) {

        switch (securityType) {

            case Constants.SECURITY_CERTIFICATE:
                return Constants.WSS_PREFIX;

            default:
                return Constants.WS_PREFIX;
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

        logger.debug(">>> Websocket send payload: processing ws send payload.");

        let connectionMapKey = JSON.stringify(target);
        let clientConnection = this.clientConnections.get(connectionMapKey);

        if (!clientConnection) {
            clientConnection = await this.getClientConnection(target, connectionMapKey);
            this.clientConnections.set(connectionMapKey, clientConnection);
        }

        logger.debug(`>>> Websocket send payload: sending payload ${payload} for session ${sessionId} to the websocket target system: ${JSON.stringify(target)}.`);
        await this.sendWsPayload(payload, clientConnection);
    }

    /**
     *
     * @param target
     * @returns {Promise.<void>}
     */
    async getClientConnection(target, connectionMapKey) {

        logger.debug(`>>> Opening websocket connection to target: ${JSON.stringify(target)}`);

        let clientConnection = {};
        let headers = {};
        if (target.headers) {
            target.headers.forEach((entry) => {
                headers[entry.name] = entry.value;
            });
        }

        let securityData = await this.getSecurityData(target.security);
        securityData.rejectUnauthorized = false;

        if (this.isCredentialsSecurity(securityData)) {

            headers.authorization = securityData;
            clientConnection = new WebSocketClient();

        } else {

            let config = {};
            config.tlsOptions = securityData;
            clientConnection = new WebSocketClient(config);
        }

        return new Promise((resolve, reject) => {

            clientConnection.on("connect", (connection) => {
                logger.debug(`>>> Websocket connection has been opened.`);

                connection.on('error', (error) => {

                    logger.error(`>>> Websocket connection has been failed due to error: ${JSON.stringify(error.message)}`);
                    this.clientConnections.delete(connectionMapKey);
                });
                connection.on('close', (error) => {

                    if (error) {
                        let message = error.message ? error.message : error;
                        logger.error(`>>> Websocket connection has been closed due to error: ${JSON.stringify(message)}`);
                    } else {
                        logger.debug(`>>> Websocket connection has been closed`);
                    }

                    this.clientConnections.delete(connectionMapKey);
                });

                resolve(connection);
            });

            clientConnection.on("connectFailed", (error) => {
                logger.error(`>>> Websocket connection has been failed due to error: ${JSON.stringify(error)}`);
                reject(error);
            });

            clientConnection.connect(target.url, null, ["ws:", "wss:"], headers);
        });
    }

    /**
     *
     * @param payload
     * @param client
     * @returns {Promise.<void>}
     */
    async sendWsPayload(payload, clientConnection) {

        return new Promise((resolve, reject) => {

            clientConnection.on("error", (error) => {
                logger.error(`>>> Websocket payload sending has been failed due to error: ${JSON.stringify(error)}`);
                reject(error);
            });

            clientConnection.send(payload);
            resolve(payload);
        });
    }

    /**
     *
     * @param securityData
     */
    isCredentialsSecurity(securityData) {

        return securityData.username;
    }

    /**
     *
     * @param security
     * @returns {*}
     */
    async getSecurityData(security = {}) {

        return new Promise((resolve, reject) => {

            /**
             * In case security isn't provided we will try to use ws protocol
             * without authorization
             */
            if (!security.type) {
                return resolve({});
            }

            switch(security.type) {

                case Constants.SECURITY_CREDENTIALS: {
                    resolve(SecurityBuilder.getAuthorizationHeader(security));
                    break;
                }

                case Constants.SECURITY_CERTIFICATE: {
                    resolve(SecurityBuilder.getCertificates(security));
                    break;
                }

                default: {
                    reject(new Error(`${security.type} doesn't supported`));
                }
            }
        })
    }
}

module.exports = WsSender;