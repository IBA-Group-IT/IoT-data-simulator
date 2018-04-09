let Base64 = require("js-base64").Base64;
let logger = require('../common/log');

module.exports = {

    /**
     *
     * @param security
     * @returns {{Authorization: string}}
     */
    getAuthorizationHeader(security) {
        return {Authorization : 'Basic ' + Base64.encode(security.username + ':' + security.password)};
    },

    /**
     *
     * @param security
     * @returns {{username, password: (*|spec.definitions.User.properties.password|{type, xml}|string|password|{})}}
     */
    getCredentials(security) {
      return {
          username: security.username,
          password: security.password
      }
    },

    /**
     *
     * @param security
     * @returns {{ca: *, cert: *, key: *}}
     */
    getCertificates(security) {

        if (security.ca) {

            return {
                ca: Base64.decode(security.ca),
                cert: Base64.decode(security.deviceCertificate),
                key: Base64.decode(security.privateKey),
                rejectUnauthorized: false
            };
        } else {

            return {
                cert: Base64.decode(security.deviceCertificate),
                key: Base64.decode(security.privateKey),
                rejectUnauthorized: false
            };
        }
    },

    /**
     *
     * @param security
     * @returns {{caCert: Buffer2, clientCert: Buffer2, privateKey: Buffer2}}
     */
    getAwsCertificates(security) {

        if (security.ca) {

            return {
                caCert: Buffer.from(Base64.decode(security.ca)),
                clientCert: Buffer.from(Base64.decode(security.deviceCertificate)),
                privateKey: Buffer.from(Base64.decode(security.privateKey)),
            };
        } else {

            return {
                clientCert: Buffer.from(Base64.decode(security.deviceCertificate)),
                privateKey: Buffer.from(Base64.decode(security.privateKey))
            };
        }
    },

    /**
     *
     * @param security
     * @returns {{accessKeyId: string, secretKey: (*|string)}}
     */
    getAccessKeysForMqtt(security) {
        return {
            accessKeyId: security.accessKey,
            secretKey: security.secretKey
        };
    },

    /**
     *
     * @param security
     * @returns {{accessKeyId: string, secretAccessKey: (*|string)}}
     */
    getAccessKeysForRest(security) {
        return {
            accessKeyId: security.accessKey,
            secretAccessKey: security.secretKey
        };
    },


    getAccessKeysForMinio(security) {
        return {
            accessKey: security.accessKey,
            secretKey: security.secretKey
        };
    },

    /**
     *
     * @param security
     * @returns {{username: (*|SalesforceToken|ActionExecutionToken|SNS.token|null|ApprovalToken)}}
     */
    getSecurityTokenMqtt(security) {
        return {username: security.token};
    },

    /**
     *
     * @param security
     * @returns {{token: (*|SalesforceToken|ApprovalToken|ActionExecutionToken|SNS.token|null)}}
     */
    getSecurityTokenRest(security) {
        return {token: security.token};
    }
};
