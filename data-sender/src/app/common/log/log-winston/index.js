var Winston = require('winston');
var WinstonConfig = require('winston/lib/winston/config');
var loggerUtil = require('./logger');

// Get config data
var Config = require('./config');
var logOptions = Config.logOptions;

// Define winston's transports
var transportConsole = new Winston.transports.Console({
    name: 'console',
    level: logOptions.level,
    prettyPrint: true,
    colorize: true,
    silent: false,
    json: false,
    formatter: function(options){
        var message = '[' + loggerUtil.getTimeStamp() + '] ' + options.meta.module + '\n['+options.level.toUpperCase()+']\t' + options.message;
        return WinstonConfig.colorize(options.level, message);
    }
});

var winston = new (Winston.Logger)({
    levels: logOptions.custom_levels,
    colors: logOptions.custom_colors,
    transports: [
        transportConsole
    ],
    // exceptionHandlers: [
    //     transportConsole
    // ],
    handleExceptions: true,
    exitOnError: false
});

function error(arguments, filename) {
    winston.level = 'error';
    winston.error(arguments, {module: loggerUtil.getModuleName(filename)});
}

function warn(arguments, filename) {
    winston.level = 'warn';
    winston.warn(arguments, {module: loggerUtil.getModuleName(filename)});
}

function info(arguments, filename) {
    winston.level = 'info';
    winston.info(arguments, {module: loggerUtil.getModuleName(filename)});
}

function debug(arguments, filename) {
    winston.level = 'debug';
    winston.debug(arguments, {module: loggerUtil.getModuleName(filename)});
}

function trace(arguments, filename) {
    winston.level = 'trace';
    winston.trace(arguments, {module: loggerUtil.getModuleName(filename)});
}

module.exports = {
    error : error,
    warn : warn,
    info : info,
    debug : debug,
    trace : trace
};