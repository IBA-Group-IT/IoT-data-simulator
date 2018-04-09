var webpack = require('webpack');
var merge = require('webpack-merge');
var commonConfig = require('./webpack-common.config');
var argv = require('yargs').argv;

var envConfig;
if (argv.env === 'production') {
    envConfig = require('./webpack-prod.config');
} else {
    envConfig = require('./webpack-dev.config');
}
var config = merge(commonConfig, envConfig);

module.exports = config;