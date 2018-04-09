let logger = require('../../common/log/index');
let CommonSender = require('../CommonSender');
let Constants = require('../../common/Constants');
let SecurityBuilder = require('../../security/SecurityBuilder');
let Mqtt = require("async-mqtt");

class MqttHelper extends CommonSender {

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

        logger.debug(">>> Mqtt send payload: processing mqtt send payload.");

        let connectionMapKey = JSON.stringify(target);
        let clientConnection = this.clientConnections.get(connectionMapKey);

        if (!clientConnection) {
            clientConnection = await this.getClientConnection(target, connectionMapKey);
            this.clientConnections.set(connectionMapKey, clientConnection);
        }

        logger.debug(`>>> Mqtt send payload: sending payload ${payload} for session ${sessionId} to the mqtt broker: ${JSON.stringify(target)}.`);
        await clientConnection.publish(target.topic, payload);
    }

    /**
     *
     * @param target
     * @returns {*}
     */
    async getClientConnection(target, connectionMapKey) {

        logger.debug(`>>> Opening mqtt connection to target: ${JSON.stringify(target)}`);

        let options = await this.getSecurityData(target.security);
        options.rejectUnauthorized = false;
        let mqttUrl = this.getMqttUrl(target, options);

        return new Promise((resolve, reject) => {

            let connection = Mqtt.connect(mqttUrl, options);

            connection.on('connect', () => {

                logger.debug(">>> Mqtt connection has been opened.");
                resolve(connection);
            });

            connection.on('error', (error) => {

                logger.error(`>>> Mqtt connection has been failed due to error: ${JSON.stringify(error.message)}`);
                this.clientConnections.delete(connectionMapKey);
                connection.end();
                reject(error);
            });

            connection.on('close', (error) => {

                logger.debug(`>>> Mqtt connection has been closed`);
                this.clientConnections.delete(connectionMapKey);
                connection.end();
                if (error) {
                    logger.error(error);
                    reject(error);
                }
            })
        });
    }

    /**
     *
     * @param target
     * @param options
     */
    getMqttUrl(target, options) {
        
        if (options.url) {
            return options.url
        }

        return target.url
    }

    /**
     *
     * @param security
     * @returns {*}
     */
    async getSecurityData(security = {}) {

        return new Promise((resolve, reject) => {

            /**
             * In case security isn't provided we will try to write into mqtt
             * topic without authorization
             */
            if (!security.type) {
                return resolve({});
            }

            switch(security.type) {

                case Constants.SECURITY_CREDENTIALS: {
                    resolve(SecurityBuilder.getCredentials(security));
                    break;
                }

                case Constants.SECURITY_CERTIFICATE: {
                    resolve(SecurityBuilder.getCertificates(security));
                    break;
                }

                case Constants.SECURITY_TOKEN: {
                    resolve(SecurityBuilder.getSecurityTokenMqtt(security));
                    break;
                }

                default: {
                    reject(new Error(`${security.type} doesn't supported`));
                }
            }
        })
    }
}

module.exports = MqttHelper;