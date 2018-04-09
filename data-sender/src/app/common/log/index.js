
let logConsole = require ('./log-winston/index');

module.exports = {

    error: function() {
        logConsole.error(arguments, module.filename);
    },

    warn: function() {
        logConsole.warn(arguments, module.filename);
    },

    info: function() {
        logConsole.info(arguments, module.filename);
    },

    debug: function() {
        logConsole.debug(arguments, module.filename);
    },

    trace: function() {
        logConsole.trace(arguments, module.filename);
    }
}