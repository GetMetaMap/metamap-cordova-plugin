package com.cordova.plugin.matiglobalidsdk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.matilock.mati_kyc_sdk.LoginError;
import com.matilock.mati_kyc_sdk.LoginResult;
import com.matilock.mati_kyc_sdk.Mati;
import com.matilock.mati_kyc_sdk.MatiCallback;
import com.matilock.mati_kyc_sdk.MatiCallbackManager;
import com.matilock.mati_kyc_sdk.MatiLoginButton;
import com.matilock.mati_kyc_sdk.MatiLoginManager;
import com.matilock.mati_kyc_sdk.Metadata;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Iterator;

/**
 * This class echoes a string called from JavaScript.
 */
public class MatiGlobalIDSDK extends CordovaPlugin implements MatiCallback {

    public static final String CANCEL = "Cancel";
    public static final String SHOW_MFKYC = "showMFKYC";
    public static final String METADATA = "metadata";
    public static final String SET_MATI_CALLBACK = "setMatiCallback";
    public static final String INIT = "init";
    public static final String COOL_METHOD = "coolMethod";
    private MatiCallbackManager mCallbackManager = MatiCallbackManager.createNew();
    CallbackContext mOnCallback;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
//        if (action.equals("coolMethod")) {
//            String message = args.getString(0);
//            this.coolMethod(message, callbackContext);
//            return true;
//        }
        switch (action){
            case COOL_METHOD:{
                String message = args.getString(0);
                this.coolMethod(message, callbackContext);
                return true;
            }
            case INIT:{
                String clientId = args.getString(0);
                this.init(clientId, callbackContext);
                return true;
            }
            case SET_MATI_CALLBACK:{
                this.setMatiCallback(callbackContext);
                return true;
            }
            case METADATA:{
                JSONObject metadata = args.getJSONObject(0);
                this.metadata(metadata, callbackContext);
                return true;
            }
            case SHOW_MFKYC:{
                this.showMFKYC(callbackContext);
                return true;
            }
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }


    private void init(final String clientId,  CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Mati.init(cordova.getActivity(), clientId);
                MatiLoginManager.getInstance().registerCallback(mCallbackManager, MatiGlobalIDSDK.this);

                callbackContext.success();
            }
        });
    }

    private void showMFKYC(CallbackContext callbackContext){

        MatiLoginButtonLauncher.weakReferenceCallbackManager = new WeakReference<>(mCallbackManager);
        Intent intent = new Intent(cordova.getActivity(), MatiGlobalIDSDK.MatiLoginButtonLauncher.class);
        cordova.getActivity().startActivity(intent);
        callbackContext.success();
    }

    private void  setMatiCallback(CallbackContext callbackContext) {
        mOnCallback = callbackContext;
    }

    public void metadata(final JSONObject metadata, final  CallbackContext callbackContext)
    {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Metadata.Builder metadataBuilder = new Metadata.Builder();
                Iterator<String> keys = metadata.keys();
                String key;
                while(keys.hasNext()) {
                     key = keys.next();
                    try {
                        metadataBuilder.with(key, metadata.get (key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Mati.getInstance().setMetadata(metadataBuilder.build());
                callbackContext.success();
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        mCallbackManager.onActivityResult(requestCode, resultCode, intent);
//    }


    @Override
    public void onSuccess(LoginResult pLoginResult) {
        mOnCallback.success(pLoginResult.getIdentityId());
    }

    @Override
    public void onCancel() {
        mOnCallback.error(CANCEL);
    }


    @Override
    public void onError(LoginError pLoginError) {
        mOnCallback.error(pLoginError.getMessage());
    }


    public static class MatiLoginButtonLauncher extends AppCompatActivity
    {
        public static WeakReference<MatiCallbackManager> weakReferenceCallbackManager;
        Handler mMainHandler ;
        MatiLoginButton mMatiLoginButton;
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(getView());
            mMainHandler = new Handler(this.getMainLooper());
            mMainHandler.post (new Runnable() {
                @Override
                public void run() {
                    mMatiLoginButton.performClick();
                }
            });

        }

        public View getView(){
            FrameLayout layout = new FrameLayout(this);
            layout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
            mMatiLoginButton = new MatiLoginButton(this);
            mMatiLoginButton.setVisibility(View.INVISIBLE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(100, 100);
            params.setMargins(-100, 0, 0, 0);
            mMatiLoginButton.setLayoutParams(params);
            layout.addView(mMatiLoginButton);

            return layout;
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(weakReferenceCallbackManager != null && weakReferenceCallbackManager.get() != null){
                if(weakReferenceCallbackManager.get().onActivityResult(requestCode, resultCode, data)){
                    this.finish();
                }
            }

        }
    }

}
