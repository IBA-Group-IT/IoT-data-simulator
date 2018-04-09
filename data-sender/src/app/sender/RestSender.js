let request = require('request-promise');
let logger = require('../common/log');
let CommonSender = require('./CommonSender');
let Constants = require('../common/Constants');
let SecurityBuilder = require('../security/SecurityBuilder');
let aws4 = require('aws4');
let url = require('url');

class RestSender extends CommonSender {

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
        return Constants.REST_QUEUE;
    }

    /**
     *
     * @param securityType
     */
    deriveTargetSystemProtocolPrefix(securityType) {

        switch (securityType) {

            case Constants.SECURITY_CERTIFICATE:
                return Constants.HTTPS_PREFIX;

            default:
                return Constants.HTTP_PREFIX;
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

        logger.debug(">>> REST send payload: processing REST send payload.");

        let headers = {};
        let options = {rejectUnauthorized: false};
        if (target.headers) {
            target.headers.forEach((entry) => {
                headers[entry.name] = entry.value;
            });
        }

        let securityData = await this.getSecurityData(target.security);
        Object.assign(options, securityData);
        Object.assign(options, this.getRequestOptions(target, headers, payload));

        if (target.security && target.security.type === Constants.SECURITY_CREDENTIALS) {

            options = {rejectUnauthorized: false};
            Object.assign(headers, securityData);
            Object.assign(options, this.getRequestOptions(target, headers, payload));
        }

        logger.debug(`>>> REST send payload: sending payload ${payload} for session ${sessionId} to the REST broker: ${JSON.stringify(target)}.`);
        await request(options);
    }

    /**
     *
     * @param target
     * @param headers
     * @param payload
     * @returns {{headers: *, method, uri, body}}
     */
    getRequestOptions(target, headers, payload) {

        return {
            headers: headers,
            method: target.method,
            uri: target.url,
            body: JSON.parse(payload),
            json: true
        };
    }

    /**
     *
     * @param security
     * @returns {*}
     */
    async getSecurityData(security = {}) {

        return new Promise((resolve, reject) => {

            /**
             * In case security isn't provided we will try to write using REST
             * without authorization
             */
            if (!security.type) {
                return resolve({});
            }

            switch(security.type) {

                case Constants.SECURITY_CREDENTIALS: {
                    resolve (SecurityBuilder.getAuthorizationHeader(security));
                    break;
                }

                case Constants.SECURITY_CERTIFICATE: {
                    resolve (SecurityBuilder.getCertificates(security));
                    break;
                }

                case Constants.SECURITY_TOKEN: {
                    resolve (SecurityBuilder.getSecurityTokenRest(security));
                    break;
                }

                default: {
                    reject(new Error(`${security.type} doesn't supported`));
                }
            }

        });
    }
}

module.exports = RestSender;