package com.anvay.twiliovideocall;

import org.apache.cordova.BuildHelper;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.LOG;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class VideoConversationPlugin extends CordovaPlugin {


    public CallbackContext callbackContext;
    private CordovaInterface cordova;
    private String roomId;
    private String token;
    private String remoteParticipantName;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.cordova = cordova;
        // your init code here
    }

    
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;
		if (action.equals("open")) {
		   	this.openRoom(args);
		}
        return true;
	}

	public void openRoom(final JSONArray args) {
        try {
            this.roomId = args.getString(0);
            this.token = args.getString(1);
            this.remoteParticipantName = args.getString(2);
            final CordovaPlugin that = this;
            final String token = this.token;
            final String roomId = this.roomId;
            final String remoteParticipantName = this.remoteParticipantName;

            LOG.d("TOKEN", token);
            LOG.d("ROOMID", roomId);
            LOG.d("REMOTE PARTICIPANT", remoteParticipantName);
     		cordova.getThreadPool().execute(new Runnable() {
                public void run() {

                    Intent intentTwilioVideo = new Intent(that.cordova.getActivity().getBaseContext(), ConversationActivity.class);
                    intentTwilioVideo.putExtra("token", token);
                    intentTwilioVideo.putExtra("roomId", roomId);
                    intentTwilioVideo.putExtra("remoteName", remoteParticipantName);
                    that.cordova.startActivityForResult(that, intentTwilioVideo, 0);
                }
                    
            });
        } catch (JSONException e) {
            //Log.e(TAG, "Invalid JSON string: " + json, e);
            //return null;
        }
    }

    public Bundle onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putString("token", this.token);
        state.putString("roomId", this.roomId);
        state.putString("remoteName", this.remoteParticipantName);
        return state;
    }

    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
        this.token = state.getString("token");
        this.roomId = state.getString("roomId");
        this.remoteParticipantName = state.getString("remoteName");
        this.callbackContext = callbackContext;
    }
}