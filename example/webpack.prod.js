const common = require('./webpack.common.js');
const merge = require('webpack-merge');
const path = require('path');

const CleanWebpackPlugin = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");
//const UglifyEs = require("uglify-es");
const OptimizeCSSAssetsPlugin = require("optimize-css-assets-webpack-plugin");

module.exports = merge(common, {
  mode: 'production',
  output: {
    path: path.resolve(__dirname, "target", "production"),
    filename: "[name]-[contenthash].js"
  },
  plugins: [
    new CleanWebpackPlugin(['target/production']),
    new MiniCssExtractPlugin({
      filename: "[name]-[contenthash].css"
    }),
  ],
  optimization: {
    minimizer: [
      new UglifyJsPlugin({
        cache: true,
        parallel: true,
        //sourceMap: true // set to true if you want JS source maps
        extractComments: true
      }),
      new OptimizeCSSAssetsPlugin({})
    ]
  }
});