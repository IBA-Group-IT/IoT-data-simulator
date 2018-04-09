var cfenv = require('cfenv');
var appEnv = cfenv.getAppEnv();
var server = require('../server');

var port = process.env.VIZ_PORT || 8090;
console.log('port is', port);

server.listen(port, '0.0.0.0', function() {
    console.log("server starting on " + appEnv.bind + ':' + port);
});