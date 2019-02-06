const common = require('./webpack.common.js');
const merge = require('webpack-merge');
const path = require('path');

const CleanWebpackPlugin = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
//const proxyConfig = require('./proxy.conf.js');

module.exports = merge(common, {
  mode: 'development',
  output: {
    path: path.resolve(__dirname, "target", "development"),
    filename: "[name].js"
  },
  devtool: 'source-map',
  devServer: {
    https: true,
    contentBase: './src/main/assets',
    historyApiFallback: true
    //proxy: proxyConfig
  },
  plugins: [
    new CleanWebpackPlugin(['target/development']),
    new MiniCssExtractPlugin({
      filename: "[name].css"
    })
  ]
});
