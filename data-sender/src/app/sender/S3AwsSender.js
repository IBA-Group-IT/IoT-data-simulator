let logger = require('../common/log/index');
let CommonSender = require('./CommonSender');
let Constants = require('../common/Constants');
let SecurityBuilder = require('../security/SecurityBuilder');
let fsExtra = require("fs-extra");
let os = require('os');
let url = require('url');
let Minio = require('minio');

const END_OF_LINE = os.EOL;
fsExtra.ensureDir(Constants.DATASETS_FOLDER);

class S3AwsSender extends CommonSender {

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
        return Constants.S3_QUEUE;
    }

    /**
     *
     * @param sessionId
     * @param target
     * @param payload
     * @returns {Promise.<void>}
     */
    async sendPayload(sessionId, target, payload) {

        logger.debug(">>> S3aws send payload: processing mqtt send payload.");

        let connectionMapKey = JSON.stringify(target);
        let clientConnection = this.clientConnections.get(connectionMapKey);

        if (!clientConnection) {
            clientConnection = this.getClientConnection(target);
            this.clientConnections.set(connectionMapKey, clientConnection);
        }

        logger.debug(`>>> S3aws write payload: writing payload ${payload} for session ${sessionId} to the filesystem`);

        await this.storePayloadToFS(sessionId, target, payload);
    }

    /**
     *
     * @param sessionId
     * @param target
     * @returns {Promise.<void>}
     */
    async handleSessionCompletion(sessionId, target) {

        let connectionMapKey = JSON.stringify(target);
        let clientConnection = this.clientConnections.get(connectionMapKey);

        if (await this.isFileExists(sessionId)) {

            let postfix = Date.now();
            let newFilePath = Constants.DATASETS_FOLDER + sessionId + '_' + postfix;

            await fsExtra.move(Constants.DATASETS_FOLDER + sessionId, newFilePath);
            await this.writeToS3(clientConnection, target, newFilePath);
            await fsExtra.unlink(newFilePath);

        } else {
            logger.error(`>>> File ${sessionId} doesn't exists on session completion handling.`);
        }
    }

    /**
     *
     * @param sessionId
     * @returns {Promise.<void>}
     */
    async handleSessionFailure(sessionId) {

        if (await this.isFileExists(sessionId)) {
            await fsExtra.unlink(Constants.DATASETS_FOLDER + sessionId);
        } else {
            logger.debug(`>>> File ${sessionId} doesn't exists on session failure handling.`);
        }
    }

    /**
     *
     * @param target
     * @returns {Client}
     */
    getClientConnection(target) {

        logger.debug(`>>> Get s3aws connection to target: ${JSON.stringify(target)}`);

        let security = this.getSecurityData(target.security);
        let urlObj = url.parse(target.url);

        let options = {
            endPoint: urlObj.hostname,
            accessKey: security.accessKey,
            secretKey: security.secretKey
        };

        if (urlObj.port) {

            options.port = parseInt(urlObj.port);

            if (urlObj.protocol === 'http:') {
                options.secure = false;
            }
        }

        return new Minio.Client(options);
    }

    /**
     *
     * @param security
     * @returns {*}
     */
    async getSecurityData(security = {}) {

        return new Promise((resolve, reject) => {

            switch (security.type) {

                case Constants.SECURITY_ACCESS_KEYS: {
                    resolve(SecurityBuilder.getAccessKeysForMinio(security));
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
     * @param sessionId
     * @param target
     * @param payload
     * @returns {Promise.<void>}
     */
    async storePayloadToFS(sessionId, target, payload) {

        if (await this.isFileExists(sessionId)) {
            await fsExtra.appendFile(Constants.DATASETS_FOLDER + sessionId, END_OF_LINE + payload);
        } else {
            await fsExtra.writeFile(Constants.DATASETS_FOLDER + sessionId, payload);
        }
    }

    /**
     *
     * @param sessionId
     * @returns {Promise.<boolean>}
     */
    async isFileExists(sessionId) {

        try {

            await fsExtra.stat(Constants.DATASETS_FOLDER + sessionId);
            return true;

        } catch (err) {

            logger.debug(`>>> File ${sessionId} doesn't exists`);
            return false;
        }
    }

    /**
     *
     * @param clientConnection
     * @param target
     * @param path
     * @returns {Promise}
     */
    async writeToS3(clientConnection, target, path){

        return new Promise((resolve, reject) => {
            clientConnection.fPutObject(target.bucket, this.getDatasetName(target), path, function(err, result) {
                if(err){
                    logger.error(`>>> Error occured during payload file sending`);
                    reject(err)
                } else {
                    logger.debug(`>>> Payload file was sent to target: ${JSON.stringify(target)}`);
                    resolve(result)
                }
            })
        })
    }

    /**
     *
     * @param target
     * @returns {string}
     */
    getDatasetName(target) {

        let nameArray = target.dataset.split(".");

        if (nameArray.length > 1) {

            let concated = nameArray[nameArray.length - 2] + '_' + Date.now();
            nameArray.splice(nameArray.length - 2, 1, concated);
            return nameArray.join(".");

        } else {

            return target.dataset + '_' + Date.now();
        }
    }
}

module.exports = S3AwsSender;