package com.cordova.plugin.metaMapglobalidsdk;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.Exception;

import com.metamap.metamap_sdk.MetamapSdk;
import com.metamap.metamap_sdk.Metadata;

import android.graphics.Color;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import org.json.*;

import static android.app.Activity.RESULT_OK;

import java.util.HashMap;

/**
 * This class echoes a string called from JavaScript.
 */
public class MetaMapGlobalIDSDK extends CordovaPlugin {

    public static final String SHOW_METAMAPFLOW = "showMetaMapFlow";
    public static final String SET_CALLBACK = "setMetaMapCallback";
    public static final String SDK_TYPE = "sdkType";
    CallbackContext mOnCallback;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case SHOW_METAMAPFLOW:
                String clientId = null;
                String flowId = null;
                String configurationId = null;
                String encryptionConfigurationId = null;
                JSONObject metadata = (new JSONObject()).put("sdkType", "cordova");
                if (args != null) {
                    JSONObject params = args.getJSONObject(0);
                    clientId = params.getString("clientId");
                    flowId = params.optString("flowId");
                    if (params.has("configurationId")) {
                        if (params.getString("configurationId") != "" && params.getString("configurationId") != "null") {
                            configurationId = params.getString("configurationId");
                        }
                    }
                    if (params.has("encryptionConfigurationId")) {
                        if (params.getString("encryptionConfigurationId") != "" && params.getString("encryptionConfigurationId") != "null") {
                            encryptionConfigurationId = params.getString("encryptionConfigurationId");
                        }
                    }
                    metadata = params.optJSONObject("metadata");

                    this.showMetaMapFlow(clientId, flowId, configurationId, encryptionConfigurationId, metadata, callbackContext);
                } else {
                    Log.e("Integration error", "Please set yours MetaMap client ID");
                }
                return true;

            case SET_CALLBACK:
                mOnCallback = callbackContext;
                return true;

            default:
                return false;
        }
    }

    private void showMetaMapFlow(final String clientId, @Nullable final String flowId, @Nullable final String configurationId, @Nullable final String encryptionConfigurationId, @Nullable final JSONObject metadata, CallbackContext callbackContext) {
        cordova.setActivityResultCallback(this);
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                MetamapSdk.INSTANCE.startFlow(
                        cordova.getActivity(),
                        clientId,
                        flowId,
                        convertToMetadata(metadata),
                        2576,
                        configurationId,
                        encryptionConfigurationId,
                        new kotlin.jvm.functions.Function2<String, String, kotlin.Unit>() {
                            @Override
                            public kotlin.Unit invoke(String identityId, String verificationId) {
                                Log.d("MetaMap", "📦 verificationCreated triggered");
                                if (mOnCallback != null) {
                                    JSONObject json = new JSONObject();
                                    try {
                                        json.put("eventType", "created");
                                        json.put("identityId", identityId != null ? identityId : "");
                                        json.put("verificationID", verificationId != null ? verificationId : "");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, json);
                                    pluginResult.setKeepCallback(true);
                                    mOnCallback.sendPluginResult(pluginResult);
                                }
                                return kotlin.Unit.INSTANCE;
                            }
                        }
                );
                callbackContext.success();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MetamapSdk.DEFAULT_REQUEST_CODE) {
            String identityId = data != null ? data.getStringExtra(MetamapSdk.ARG_IDENTITY_ID) : "";
            String verificationId = data != null ? data.getStringExtra(MetamapSdk.ARG_VERIFICATION_ID) : "";

            JSONObject json = new JSONObject();
            try {
                json.put("identityId", identityId != null ? identityId : "");
                json.put("verificationID", verificationId != null ? verificationId : "");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PluginResult result;

            if (resultCode == RESULT_OK) {
                Log.d("MetaMap", "✅ verificationSuccess");
                try {
                    json.put("eventType", "success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result = new PluginResult(PluginResult.Status.OK, json);
            } else {
                Log.d("MetaMap", "❌ verificationCancelled");
                try {
                    json.put("eventType", "cancelled");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result = new PluginResult(PluginResult.Status.ERROR, json);
            }

            result.setKeepCallback(true);
            mOnCallback.sendPluginResult(result);
        }
    }

    public Metadata convertToMetadata(final JSONObject metadata) {
        if (metadata == null)
            return null;

        Metadata.Builder metadataBuilder = new Metadata.Builder();
        Iterator<String> keys = metadata.keys();
        String key;
        while (keys.hasNext()) {
            key = keys.next();
            if (key.toLowerCase().contains("color")) {
                String hexColor = null;
                try {
                    hexColor = (String) metadata.get(key);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                int color = Color.parseColor(hexColor);
                if (hexColor.length() == 9) {
                    color = Color.argb(Color.blue(color), Color.alpha(color), Color.red(color), Color.green(color));
                }
                try {
                    metadataBuilder.with(key, color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    metadataBuilder.with(key, metadata.get(key));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return metadataBuilder.build();
    }
}
