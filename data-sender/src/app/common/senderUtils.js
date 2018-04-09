let Constants = require('./Constants');

module.exports = {

    /**
     *
     * @param url
     * @returns {string}
     */
    parseProtocol(url) {

        /**
         * Built in 'url' module for url 'localhost:8000' returns protocol 'localhost:'
         * but it is expected to get nothing in this case.
         * So, parsing protocol manually
         */

        if (url) {
            let delimiterIndex = url.indexOf(Constants.URL_PROTOCOL_DELIMITER);
            if (delimiterIndex >= 0) {
                return url.substring(0, delimiterIndex);
            }
        }
    },

    /**
     *
     * @param value
     * @returns {boolean}
     */
    isStringValue(value) {
        return typeof value === 'string' || value instanceof String;
    }
};