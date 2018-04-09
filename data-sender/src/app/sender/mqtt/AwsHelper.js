let logger = require('../../common/log/index');
let CommonSender = require('../CommonSender');
let Constants = require('../../common/Constants');
let SecurityBuilder = require('../../security/SecurityBuilder');
let AwsDevice = require("aws-iot-device-sdk").device;
let url = require('url');

class AwsHelper extends CommonSender {

    /**
     *
     */
    constructor() {
        super();
    }

    /**
     *
     * @param sessionId
     * @param target
     * @param payload
     * @returns {Promise.<void>}
     */
    async sendPayload(sessionId, target, payload) {

        logger.debug(">>> Aws mqtt send payload: processing mqtt send payload.");

        let connectionMapKey = JSON.stringify(target);
        let clientConnection = this.clientConnections.get(connectionMapKey);

        if (!clientConnection) {
            clientConnection = this.getClientConnection(target, connectionMapKey);
            this.clientConnections.set(connectionMapKey, clientConnection);
        }

        logger.debug(`>>> Aws mqtt send payload: sending payload ${payload} for session ${sessionId} to the aws mqtt broker: ${JSON.stringify(target)}.`);

        let client = await this.getClient(clientConnection);
        client.publish(target.topic, payload);
    }

    /**
     *
     * @param clientConnection
     * @returns {Promise}
     */
    async getClient(clientConnection) {
        return await clientConnection;
    }

    /**
     *
     * @param target
     * @returns {*}
     */
    async getClientConnection(target, connectionMapKey) {

        logger.debug(`>>> Opening aws mqtt connection to target: ${JSON.stringify(target)}`);

        let options = await this.getSecurityData(target.security);
        let urlObj = url.parse(target.url);
        options.host = urlObj.host ? urlObj.host : urlObj.path;

        if (options.accessKeyId) {
            options.protocol = this.getProtocol(urlObj);
        }

        return new Promise((resolve, reject) => {

            let client = AwsDevice(options);

            client.on('connect', () => {

                logger.debug(">>> AWS mqtt connection has been opened.");
                resolve(client);
            });

            client.on('error', (error) => {

                logger.error(`>>> AWS mqtt connection has been failed due to error: ${JSON.stringify(error)}`);
                this.clientConnections.delete(connectionMapKey);
                reject(error);
            });

            client.on('close', () => {

                logger.error(`>>> AWS mqtt connection has been closed`);
                this.clientConnections.delete(connectionMapKey);
                resolve();
            });
        });
    }

    /**
     *
     * @param security
     * @returns {*}
     */
    async getSecurityData(security = {}) {

        return new Promise((resolve, reject) => {

            switch(security.type) {

                case Constants.SECURITY_CERTIFICATE: {
                    resolve(SecurityBuilder.getAwsCertificates(security));
                    break;
                }

                case Constants.SECURITY_ACCESS_KEYS: {
                    resolve(SecurityBuilder.getAccessKeysForMqtt(security));
                    break;
                }

                default: {
                    reject(new Error(`${security.type} doesn't supported`));
                }
            }
        })
    }

    /**
     *
     * @param urlObj
     * @returns {string}
     */
    getProtocol(urlObj) {
        //After url parsing, we need ro remove : sign from protocol string
        return urlObj.protocol.substring(0, urlObj.protocol.length -1);
    }


}

module.exports = AwsHelper;