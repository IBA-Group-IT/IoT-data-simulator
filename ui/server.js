'use strict'
// Read environment variables from .env file
require('dotenv').config();

var express = require("express");
var path = require("path");
var http = require("http");
var bodyParser = require("body-parser");
var compression = require("compression");
var proxy = require("http-proxy-middleware");

var app = express();
var config = require('./config');

app.use('/api/config', function(req, res) {
    res.json(config);
});

// Websocket proxy configuration
var wsBaseUrl = config.websocketUrl;
app.use('/ws', proxy(wsBaseUrl, { ws: true }));

// Rest proxy configuration
app.use('/api/*', proxy({ 
    target: config.restUrl,
    onProxyReq: function onProxyReq(proxyReq, req, res) {
        // Log outbound request to remote target
        console.log('-->  ', req.method, req.path, '->', proxyReq.baseUrl + proxyReq.path);
    },
    pathRewrite: {'^/api' : '/v1'}
}));

// enabling compression for all responses
app.use(compression());
// enabling JSON response parsing
app.use(bodyParser.json());

// serve the files out of ./public as our main files
app.use(express.static(__dirname + "/public"));
app.use("*", function(req, res) {
    res.sendFile(path.resolve(__dirname, "public", "index.html"));
});

// global hook for error logging
app.use(function(error, request, response, next) {
    console.error(error);
});

process.on("uncaughtException", function(exception) {
    console.log(exception);
});

var server = http.createServer(app);
module.exports = server;