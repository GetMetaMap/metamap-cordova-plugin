# Mati Cordova plugin Android And IOS SDK documentation

Create a new Cordova project or Ionic project
Add the SDK plugin with the following command

cordova plugin add https://github.com/MatiFace/MatiGlobalIDSDKCordovaPlugin.git

## Add Android Platform

Add to Android platform to project

### ionic command

ionic cordova prepare android

### cordova command

cordova platform add android

## Add IOS Platform

Add to IOS platform to project

### ionic command

ionic cordova prepare ios

### cordova command

cordova platform add ios

## Create html

In your project add a button to trigger the login process.

## Mati SDK initialization

Initialize Mati by calling the following line of code:

### ionic sample component

    import { Component } from "@angular/core";
    import { Platform } from "@ionic/angular";

    //global instance of cordova
    declare var cordova: any;
    @Component({
      selector: "app-home",
      templateUrl: "home.page.html",
      styleUrls: ["home.page.scss"]
    })
    export class HomePage {
      constructor(public platform: Platform) {
        platform.ready().then(() => {

          //init sdk
          cordova.plugins.MatiGlobalIDSDK.init("5dc09bd3047ea0001c4b20ba");

          //Send metadata
          cordova.plugins.MatiGlobalIDSDK.metadata({ key: "value" });

          //register to login callback
          cordova.plugins.MatiGlobalIDSDK.setMatiCallback(
            identityId => {
              console.log("setMatiCallback success: " + identityId);
            },
            error => {
              console.log("setMatiCallback error: " + error);
            }
          );
        });
      }

      //trigger login on button click
      showMFKYC() {
        cordova.plugins.MatiGlobalIDSDK.showMFKYC();
      }
    }

### cordova in index.js

    cordova.plugins.MatiGlobalIDSDK.init("your client ID here");

    //Send metadata
    cordova.plugins.MatiGlobalIDSDK.metadata({ key: "value" });

    //register to login callback
    cordova.plugins.MatiGlobalIDSDK.setMatiCallback(
      identityId => {
        console.log("setMatiCallback success: " + identityId);
      },
      error => {
        console.log("setMatiCallback error: " + error);
      }
    );

    //trigger login on button click
    var matiButton = document.getElementById("your button id");
    matiButton.onclick = () => {
      cordova.plugins.MatiGlobalIDSDK.showMFKYC();
    };

##IOS build

In the IOS platform find the Podfile file. The targeted OS version should be a minimum of 9. Run "pod install" to fetch the project dependencies.

The following permissions are needed to capture video and access the photo gallery.

###Info.plist

```
<key>NSCameraUsageDescription</key>
<string>Mati needs access to your Camera</string>
<key>NSPhotoLibraryUsageDescription</key>
<string>Mati needs access to your media library</string>
```
