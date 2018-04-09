
module.exports = {

    logOptions: {
        "levels": "debug",
        "date_time_format" : "YYYY-MM-DD HH:mm:ss:SSS",
        "custom_levels" : { "error": 0, "warn": 1, "info": 2, "debug": 3, "trace": 4 },
        "custom_colors" : { "error": "red", "warn": "yellow", "info": "green", "debug": "blue", "trace": "white" },
        "maxTemporaryBufferSize": 100,
        "maxPersistBufferSize": 100,
        "cronCleanTemporaryDb" : "0 23 * * 5",
        "maxDocsTemporaryDb" : 10000
    }

};