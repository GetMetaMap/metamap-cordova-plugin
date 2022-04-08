---
title: "Ionic Cordova"
excerpt: "Add the MetaMap button to your Cordova app."
slug: "ionic-cordova-sdk"
category: 61ae8e8dba577a0010791480
---

# MetaMap for Ionic Cordova Usage Guide

This is a usage guide to implement MetaMap for [Ionic for Cordova framework](https://ionicframework.com/docs/v1/guide/preface.html) for the following platforms:

* [Cordova](#cordova)
* [Ionic](#ionic-cordova)
* [Android](#android)
* [iOS](#ios)

_**Note**_ This usage guide is for Cordova version 6.x+ with a recommendation of Cordova version 10.

## Cordova Demo App

You can go to GitHub to download the [MetaMap Cordova demo app](https://github.com/GetMati/mati-mobile-examples/tree/main/cordovaDemoApp) or the [Mati Ionic Cordova demo app](https://github.com/GetMati/mati-mobile-examples/tree/main/cordovaIonicDemoApp).

## Cordova

### Install MetaMap for Cordova

In a terminal, install MetaMap for Cordova:

```bash
cordova plugin add https://github.com/GetMati/mati-cordova-plugin.git
```

#### Upgrade MetaMap for Cordova
To upgrade to the latest version of MetaMap for Cordova, you will need first uninstall your current version:

```bash
cordova plugin remove mati-global-id-sdk-cordova-plugin
```

Then install the latest MetaMap version:
```bash
cordova plugin add https://github.com/GetMati/mati-cordova-plugin.git
```

### Add MetaMap to Your Cordova App

You will need to update your HTML and JavaScript files to add the MetaMap button to your Cordova application:

* [HTML](#cordova-html)
* [JavaScript](#cordova-javascript)

<a id="cordova-html"></a>
#### HTML

Add the MetaMap button to your HTML file:

**HTML Example for Cordova**
```
 <input
     class="metaMapButton"
     id="metaMapButton"
     type="button"
     value="show MetaMap Flow"
     />
 ```

<a id="cordova-javascript"></a>
#### JavaScript

Add the MetaMap button in your JavaScript file `index.js`:


**JavaScript Example for Cordova**

```js
function onDeviceReady() {

//trigger login on button click
var metaMapButton = document.getElementById("metaMapButton");

  metaMapButton.onclick = () => {
      //set 3 params clientId (cant be null), flowId, metadata
      var yourMetadata = { param1: "value1", param2: "value2" }
      var metaMapButtinParams = { clientId: "YOUR_CLIENT_ID", flowId: "YOUR_FLOW_ID", metadata: yourMetadata }
      cordova.plugins.MetaMapGlobalIDSDK.showMetaMapFlow(metaMapButtinParams)
    };

    //register to callback
    cordova.plugins.MetaMapGlobalIDSDK.setMetaMapCallback(
     identityId => {
       console.log("setMetaMapCallback success: " + identityId);
     },
     error => {
       console.log("setMetaMapCallback error: " + error);
     }
    );

}
 ```

## Ionic Cordova

### Install MetaMap for Ionic Cordova

In a terminal, install MetaMap for Ionic Cordova:

```bash
ionic cordova plugin add  https://github.com/GetMati/mati-cordova-plugin.git
```

#### Upgrade MetaMap for Ionic Cordova
To upgrade to the latest version of MetaMap for Cordova, you will need first uninstall your current version:

```bash
ionic cordova plugin remove Mati-global-id-sdk-cordova-plugin
```
Then install the latest MetaMap version:

```bash
ionic cordova plugin add  https://github.com/GetMati/mati-cordova-plugin.git
```

### Add MetaMap to Your Ionic Cordova App

You will need to update your HTML and TypeScript files to add the MetaMap button to your Cordova application:

* [HTML](#ionic-cordova-html)
* [Type Script](#ionic-cordova-typescript)

<a id="ionic-cordova-html"></a>
#### HTML

**HTML Example for Ionic Cordova**
```html
 <input
    class="metaMapButton"
    id="metaMapButton"
    type="button"
    value="show MetaMap Flow"
    ion-item (click)="showMetaMapFlow()"/>
 ```
<a id="ionic-cordova-typescript"></a>
#### TypeScript

**TypeScript Example for Ionic Cordova: home.page.ts**
```ts
import { Component } from '@angular/core';

//global instance of cordova
declare var cordova: any;

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  constructor() {}

  ionViewDidEnter() {
    //register to callback
    cordova.plugins.MetaMapGlobalIDSDK.setMetaMapCallback(
      identityId => {
        console.log("setMetaMapCallback success: " + identityId);
      },
      error => {
        console.log("setMetaMapCallback error: " + error);
      }
    );  
  }

  showMetaMapFlow() {
   //set 3 params clientId (cant be null), flowId, metadata
   var yourMetadata = { param1: "value1", param2: "value2" }
    var metaMapParams = { clientId: "YOUR_CLIENT_ID", flowId: "YOUR_FLOW_ID", metadata: yourMetadata }
    cordova.plugins.MetaMapGlobalIDSDK.showMetaMapFlow(metaMapParams);
  }

}
 ```

## Android

Download the latest version of MetaMap for Android [here](https://search.maven.org/artifact/com.getmati/mati-sdk).

1. Check that your project's Gradle file (`<YourProject>/platforms/android/mati-global-id-sdk-cordova-plugin/<demoCordovaMati-build>.gradle`) uses the latest version of Mati for Cordova:

    ```java
    dependencies {
      implementation 'com.getmati:mati-sdk:<LATEST_VERSION>'
    }
    ```

1. Enable AndroidX support in `config.xml`.

   ```xml
   <platform name="android">
   	<preference name="AndroidXEnabled" value="true" />
   </platform>
   ```

## iOS

The following instructions are for iOS version 12.0 or higher.
1.  The latest version of MetaMap for iOS: https://cocoapods.org/pods/MetaMap-Global-ID-SDK.
  You can also check your podfile for version information. For example:
    ```ruby
    platform :ios, '12.0'
    target 'demoCordovaMetaMap' do
        pod 'MetaMap-Global-ID-SDK'
    end
    ```
1. In the iOS directory find the podfile and install:
  `pod install`
1. In the `info.plist` file, add the following permissions to capture video, access the photo gallery, and capture audio for voiceliveness:
  **Info.plist**

    ```xml
    <key>NSCameraUsageDescription</key>
    <string>MetaMap needs access to your Camera</string>
    <key>NSPhotoLibraryUsageDescription</key>
    <string>MetaMap needs access to your media library</string>
    <key>NSMicrophoneUsageDescription</key>
    <string>MetaMap needs access to your Microphone</string>
    ```
