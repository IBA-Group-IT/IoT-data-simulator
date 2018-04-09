let CommonSender = require('../CommonSender');
let Constants = require('../../common/Constants');
let AwsHelper = require('./AwsHelper');
let MqttHelper = require('./MqttHelper');

class MqttSender extends CommonSender {

    /**
     *
     */
    constructor() {
        super();
        this.awsHelper = new AwsHelper();
        this.mqttHelper = new MqttHelper();
    }

    /**
     *
     * @returns {string}
     */
    getAmqpQueue() {
        return Constants.MQTT_QUEUE;
    }

    /**
     *
     * @param securityType
     */
    deriveTargetSystemProtocolPrefix(securityType) {

        switch (securityType) {

            case Constants.SECURITY_CERTIFICATE:
                return Constants.MQTTS_PREFIX;

            case Constants.SECURITY_ACCESS_KEYS:
                return Constants.MQTT_WSS_PREFIX;

            default:
                return Constants.MQTT_PREFIX;
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

        let helper = this.getMqttHelper(target.url);
        await helper.sendPayload(sessionId, target, payload);
    }

    /**
     *
     * @param mqttUrl
     * @returns {*}
     */
    getMqttHelper(mqttUrl) {

        if (mqttUrl.search(Constants.AMAZON_URL) > -1) {

            return this.awsHelper;
        } else {

            return this.mqttHelper;
        }
    }
}

module.exports = MqttSender;