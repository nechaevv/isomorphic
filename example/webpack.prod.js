const common = require('./webpack.common.js');
const merge = require('webpack-merge');
const path = require('path');

const CleanWebpackPlugin = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const TerserPlugin = require('terser-webpack-plugin');
const OptimizeCSSAssetsPlugin = require("optimize-css-assets-webpack-plugin");

module.exports = merge(common, {
  mode: 'production',
  entry: {
    "example": ["./src/main/js/boot-prod.js"]
  },
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
      new TerserPlugin({
        parallel: true,
        extractComments: 'all',
        terserOptions: {
          ecma: 6,
          toplevel: true,
          unsafe: true,
          unsafe_methods: true,
          unsafe_proto: true,
          unsafe_regexp: true,
          unsafe_undefined: true
        }
      }),
      new OptimizeCSSAssetsPlugin({})
    ]
  }
});
