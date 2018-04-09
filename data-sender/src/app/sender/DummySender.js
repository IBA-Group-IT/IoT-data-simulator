let logger = require('../common/log');
let CommonSender = require('./CommonSender');
let Constants = require('../common/Constants');

class DummySender extends CommonSender {

    constructor() {
        super();
    }

    getAmqpQueue() {
        return Constants.DUMMY_QUEUE;
    }

    /**
     *
     * @param sessionId
     * @param target
     * @param payload
     * @returns {Promise.<void>}
     */
    async sendPayload(sessionId, target, payload) {
        logger.debug(">>> Processing dummy send payload.");
    }

}

module.exports = DummySender;