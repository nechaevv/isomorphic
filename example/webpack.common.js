const path = require('path');

const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
  entry: {
    "example": ["./src/main/js/boot.js"]
  },
  module: {

    rules: [{
      test: /\.js$/,
      enforce: "pre",
      use: ["source-map-loader"]
    }, {
      test: /\.scss$/,
      use: [
        {loader: MiniCssExtractPlugin.loader},
        {loader: 'css-loader'},
        {
          loader: 'sass-loader',
          options: {
            includePaths: [
              'node_modules'
            ]
          }
        }
      ]
    }, {
      test: /\.(png|svg|jpg|gif|ico)$/,
      use: [{
        loader: 'file-loader',
        options: {
          context: path.resolve(__dirname, 'src', 'main', 'assets')
        }
      } ]
    } ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: "src/main/html/index.html"
    })
  ],
  resolve: {
    alias: {
      "target": path.resolve(__dirname, 'target'),
      "src": path.resolve(__dirname, 'src')
    }
  }
};
