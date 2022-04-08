## About
  This is a cordova plugin built using Twilio Video API v2.0.0preview6. Main reason for making the plugin is to support H.264 codec on Android Marshmallow+ devices.


## Platforms
- Android
- iOS (uses TwilioVideo 1.0 since H.264 codec works fine)


## Usage
**Using the cordova plugin**

1. Add to the project
    - `ionic cordova plugin add cordova-plugin-twilio-video-v2preview`
    - `npm install --save cordova-plugin-twilio-video-v2preview`
  
2. Implement the source code
    - Get token And Call the API
    `cordova.videoconversation.open( RoomName: string, Token: string, RemoteParticipantName: string);`


## Notes
- Android uses gradle to fetch TwilioVideo as a dependency.
  
- iOS code of this plugin needs TwilioVideo.framework. This framework, along with its binaries, gets embedded into your project.


## Credit
  This plugin was developed using [Price F.'s](https://github.com/PriceFallin) plugin [cordova-plugin-twilio-video](https://github.com/PriceFallin/cordova-plugin-twilio-video) as a starting point. I merely modified the code to use Twilio Video API 2.0.0preview6 with some UI changes.
