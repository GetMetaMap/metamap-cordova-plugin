var exec = require("cordova/exec");

exports.coolMethod = function(arg0, success, error) {
  exec(success, error, "MatiGlobalIDSDK", "coolMethod", [arg0]);
};

exports.init = function(arg0, success, error) {
  exec(success, error, "MatiGlobalIDSDK", "init", [arg0]);
};

exports.setMatiCallback = function(success, error) {
  exec(success, error, "MatiGlobalIDSDK", "setMatiCallback", []);
};

exports.metadata = function(arg0, success, error) {
  exec(success, error, "MatiGlobalIDSDK", "metadata", [arg0]);
};

exports.showMFKYC = function(success, error) {
  exec(success, error, "MatiGlobalIDSDK", "showMFKYC", []);
};

exports.setFlowId = function(arg0, success, error) {
  exec(success, error, "MatiGlobalIDSDK", "setFlowId", [arg0]);
};