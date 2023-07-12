"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.handleComponent = exports.handleAction = exports.encrypt = void 0;

var _reactNative = require("react-native");

const Adyen = _reactNative.NativeModules.Adyen;

const encrypt = (publicKey, data) => {
  return Adyen.encrypt(publicKey, data);
};

exports.encrypt = encrypt;

const handleComponent = (clientKey, environment, data, amount) => {
  return Adyen.handleComponent(clientKey, environment, data, amount);
};

exports.handleComponent = handleComponent;

const handleAction = (clientKey, environment, data) => {
  return Adyen.handleAction(clientKey, environment, data);
};

exports.handleAction = handleAction;
//# sourceMappingURL=index.js.map