var webpack = require('webpack');

module.exports = {
    devtool: 'source-map',
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': JSON.stringify('development')
            }
        }),
    ],
    devServer: {
        open: true,
        openPage: '',
        contentBase: __dirname + '/../public',
        port: 9100,
        proxy: {
            "/ws/*": {
                target: 'http://127.0.0.1:8090',
                ws: true
            },
            "/**": {
                target: 'http://127.0.0.1:8090'
            },
            "/api/**": {
                target: 'http://127.0.0.1:8090'
            },
        }
    }
};