var webpack = require("webpack");
var HtmlWebpackPlugin = require("html-webpack-plugin");
var PreloadWebpackPlugin = require('preload-webpack-plugin');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var importer = require("node-sass-import-once");
var path = require("path");
var argv = require("yargs").argv;

var hash = argv.env === "production" ? ".[chunkhash]" : "";

var commonConfig = {
    entry: {
        app: "./src/app/app.js",
        vendor: ["react", "mobx", "react-dom"]
    },

    output: {
        filename: "bundle" + hash + ".js",
        path: path.resolve(__dirname, "../public"),
        publicPath: "/"
            //sourceMapFilename: "[file].map",
    },

    resolve: {
        modules: [
            path.resolve(__dirname, "./node_modules"),
            path.resolve(__dirname, "./src/app/shared"),
            path.resolve(__dirname, "./src/assets")
        ]
    },

    module: {
        rules: [{
                test: /\.(js|jsx)$/,
                exclude: [/tmp/, /node_modules/, /\.spec\.js/],
                use: [{
                    loader: "babel-loader",
                    options: {
                        plugins: [
                            [
                                "transform-runtime",
                                {
                                    polyfill: true,
                                    regenerator: false
                                }
                            ],
                            ["transform-decorators-legacy"],
                            ["glamorous-displayname"]
                        ],
                        presets: [
                            ["es2015", { modules: false }],
                            ["react"],
                            ["stage-0"]
                        ]
                    }
                }]
            },
            {
                test: /\.html$/,
                use: ["raw-loader"]
            },
            {
                test: /\.scss$/,
                loader: ExtractTextPlugin.extract({
                    fallback: "style-loader",
                    use: [{
                            loader: "css-loader",
                            options: {
                                minimize: true
                            }
                        },
                        {
                            loader: "sass-loader"
                        }
                    ]
                })
            },
            {
                test: /\.css/,
                loader: ExtractTextPlugin.extract({
                    fallback: "style-loader",
                    use: "css-loader"
                })
            },
            {
                test: /\.(jpg|jpeg|ico|gif)$/,
                use: ["file-loader"]
            },
            {
                test: /\.svg$/,
                use: ["raw-loader"]
            },
            {
                test: /\.(png)$/,
                exclude: [/\.responsive\.png/],
                use: [{
                    loader: "url-loader",
                    options: {
                        limit: 100000
                    }
                }]
            },
            {
                test: /\.responsive.png$/,
                use: [{
                    loader: "responsive-loader"
                }]
            },
            {
                test: /\.json$/,
                use: [{
                    loader: "json-loader"
                }]
            },
            {
                test: /\.(woff|woff2)(\?v=\d+\.\d+\.\d+)?$/,
                use: [{
                    loader: "file-loader",
                    options: {
                        name: "[name].[ext]"
                    }
                }]
            }
        ]
    },

    plugins: [
        new webpack.ProvidePlugin({
            d3: "d3",
            "window.d3": "d3"
        }),
        new webpack.LoaderOptionsPlugin({
            options: {
                sassLoader: {
                    importer: importer
                },
                responsiveLoader: {
                    sizes: [300, 600, 1200, 1600],
                    placeholder: true,
                    placeholderSize: 50
                },
                postcss: [
                    require("autoprefixer")({
                        browsers: "defaults"
                    }),
                    require("postcss-reporter")()
                ],
                context: __dirname
            }
        }),
        new ExtractTextPlugin({
            filename: "style" + hash + ".css",
            allChunks: true
        }),
        new webpack.optimize.CommonsChunkPlugin({
            name: "vendor",
            minChunks: Infinity,
            filename: "vendor" + hash + ".js"
        }),
        new HtmlWebpackPlugin({
            filename: "index.html",
            template: "./src/index.html",
            favicon: './src/favicon.png'
        }),
        new PreloadWebpackPlugin({
            include: 'all'
        })
    ],

    node: {
        net: 'empty',
    }
};

module.exports = commonConfig;