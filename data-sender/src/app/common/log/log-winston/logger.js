let Path = require("path");
let Moment = require('moment');
let Config = require('./config');
let logOptions = Config.logOptions;

module.exports = {

    getModuleName : function (path) {
        if (path) {
            var dirName = Path.dirname(path).split(Path.sep);
            var moduleName = '..'+Path.sep + dirName[dirName.length-1] + Path.sep + Path.basename(path);

            return moduleName;
        }

        return "";
    },

    getTimeStamp : function () {
        return Moment().format(logOptions.date_time_format);
    }
};