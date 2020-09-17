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
## Here is two examples for 1) Ionic and for 2) Cordova.

Initialize Mati by calling the following line of code:

## 1) Ionic
### example of html for ionic

    <input
     class="matiButton"
     id="matiButton"
     type="button"
     value="show MFKYC"
     flowId="5e962a23728ddc001b5937aa"
     />

### ionic sample component

    import { Component, NgZone } from "@angular/core";
    import { Platform } from "@ionic/angular";

    //global instance of cordova
    declare var cordova: any;

    @Component({
      selector: "app-home",
      templateUrl: "home.page.html",
      styleUrls: ["home.page.scss"]
    })
    export class HomePage {
      yourLabelVariable: string = "No Status";

      constructor(public platform: Platform, zone: NgZone) {
        platform.ready().then(() => {
          //init sdk
          cordova.plugins.MatiGlobalIDSDK.init("5dc09bd3047ea0001c4b20ba");

          //Send metadata
          cordova.plugins.MatiGlobalIDSDK.metadata({ key: "value" });

          //register to login callback
          cordova.plugins.MatiGlobalIDSDK.setMatiCallback(
            identityId => {
              console.log("setMatiCallback success: " + identityId);

              zone.run(() => {
                this.yourLabelVariable = "setMatiCallback success: " + identityId;
              });
            },
            error => {
              console.log("setMatiCallback error: " + error);

              //Run code on Angular zone to update UI
              zone.run(() => {
                this.yourLabelVariable = "setMatiCallback error: " + error;
              });
            }
          );
        });
      }

      //trigger login on button click
      showMFKYC() {
	    cordova.plugins.MatiGlobalIDSDK.setFlowId("5eff264acd3000fc");
        cordova.plugins.MatiGlobalIDSDK.showMFKYC();
      }
    }
    
## 2) Cordova

### example of html for cordova

<input
 class="matiButton"
 id="matiButton"
 type="button"
 value="show MFKYC"
 ion-item (click)="showMFKYC()"/>
 
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
    
    //if you need specify flow set flowId attribute to button in html
    var flowId = matiButton.getAttribute("flowId");
    cordova.plugins.MatiGlobalIDSDK.setFlowId(flowId);
    
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
##Make sure that you are using the latest version our sdk

#Android

You have to check your project: YourProject/platforms/android/mati-global-id-sdk-cordova-plugin/demoCordovaMati-build.gradle

```
dependencies {
implementation 'com.matilock:mati-global-id-sdk:HERE_IS_LATEST_VERSION'
}
```
Check this for latest version: 
https://bintray.com/matibiometricskyc/maven/mati-global-id-sdk

#iOS

You have to check your project: YourProject/platforms/ios/Podfile

```
platform :ios, '9.0'
target 'demoCordovaMati' do
    pod 'Mati-Global-ID-SDK'
end
```
Check this for latest version: 
https://cocoapods.org/pods/Mati-Global-ID-SDK




