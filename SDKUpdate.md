# Mati Cordova plugin Update

## If you don't change the public interface of the sdk:

### Cordova IOS:

-Nothing to do the new sdk will be added via cocoapod.
-Run `pod update` in the IOS platform folder.

### Cordova Android:

-Nothing to do the new sdk will be added via gradle.
-Sync gradle to pull the latest version of the SDK

## If you need to update the sdk public interface:

### Cordova javascript:

You can edit the public interface of the javascript abstraction of the sdk by editing  
MatiGlobalIDSDKIonicCordova/MatiGlobalIDSDK/www/MatiGlobalIDSDK.js

Check link for more detail:
![alt text](https://cordova.apache.org/docs/en/latest/guide/hybrid/plugins)

### Ionic, Cordova IOS:

You can edit the plugin implementation by editing MatiGlobalIDSDKIonicCordova/MatiGlobalIDSDK/src/ios/MatiGlobalIDSDK.m

### Ionic, Cordova Android:

You can edit the plugin implementation by editing MatiGlobalIDSDKIonicCordova/MatiGlobalIDSDK/src/android/MatiGlobalIDSDK.java
