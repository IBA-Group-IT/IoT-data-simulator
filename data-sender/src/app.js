let express = require('express');

let AmqpListener = require('./app/listener');
let MqttSender = require('./app/sender/mqtt/index');
let AmqpSender = require('./app/sender/AmqpSender');
let KafkaSender = require('./app/sender/KafkaSender');
let S3AwsSender = require('./app/sender/S3AwsSender');
let DummySender = require('./app/sender/DummySender');
let RestSender = require('./app/sender/RestSender');
let WsSender = require('./app/sender/WsSender');
let app = express();

let senders = [
    new AmqpSender(),
    new MqttSender(),
    new KafkaSender(),
    new S3AwsSender(),
    new DummySender(),
    new RestSender(),
    new WsSender()
];

new AmqpListener(senders).run();

module.exports = app;