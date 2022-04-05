   var exec = require("cordova/exec");

    exports.setMetaMapCallback = function(success, error) {
        exec(success, error, "MatiGlobalIDSDK", "setMetaMapCallback", []);
    }

    exports.showMetaMapFlow = function(params, success, error) {
      var { metadata } = params
      params = { ...params, metadata: {...metadata, sdkType: "cordova"}}
      exec(success, error, "MatiGlobalIDSDK", "showMetaMapFlow", [params]);
    }