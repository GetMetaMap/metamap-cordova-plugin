/********* MatiGlobalIDSDK.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import <MatiGlobalIDSDK/MatiGlobalIDSDK.h>

@interface MatiGlobalIDSDK : CDVPlugin <MFKYCDelegate>{
    // Member variables go here.
}

- (void)coolMethod:(CDVInvokedUrlCommand*)command;
- (void)init:(CDVInvokedUrlCommand*)command;
- (void)setMatiCallback:(CDVInvokedUrlCommand*)command;
- (void)metadata:(CDVInvokedUrlCommand*)command;
- (void)showMFKYC:(CDVInvokedUrlCommand*)command;
- (void)setFlowId:(CDVInvokedUrlCommand*)command;
@end

@implementation MatiGlobalIDSDK{
    CDVInvokedUrlCommand* setMatiCallbackCDVInvokedUrlCommand;
    MFKYCButton* matiButton;
}

- (void)coolMethod:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];
    
    if (echo != nil && [echo length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)init:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* clientId = [command.arguments objectAtIndex:0];
    [MFKYC registerWithClientId:clientId metadata:nil];
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    
    matiButton = [[MFKYCButton alloc] init];
    matiButton.frame = CGRectMake(-320, 20, 320, 60);//you can change position,width an height
  
    matiButton.title = @"Custom Title";
    matiButton.tag = 100;
    [self.webView addSubview:matiButton];
    
}

- (void)setFlowId:(CDVInvokedUrlCommand*)command
{
    NSString* flowId = [command.arguments objectAtIndex:0];
    matiButton.flowId = flowId;
}

- (void)setMatiCallback:(CDVInvokedUrlCommand*)command
{
    setMatiCallbackCDVInvokedUrlCommand = command;
    [MFKYC instance].delegate = self;
}

- (void)mfKYCLoginSuccessWithIdentityId:(NSString *)identityId {
    if(setMatiCallbackCDVInvokedUrlCommand != nil){
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:identityId];
        [pluginResult setKeepCallbackAsBool:YES];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:setMatiCallbackCDVInvokedUrlCommand.callbackId];
    }
}

- (void)mfKYCLoginCancelled {
    if(setMatiCallbackCDVInvokedUrlCommand != nil){
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString: @"Cancel"];
        [pluginResult setKeepCallbackAsBool:YES];
         [self.commandDelegate sendPluginResult:pluginResult callbackId:setMatiCallbackCDVInvokedUrlCommand.callbackId];
    }
}

- (void)metadata:(CDVInvokedUrlCommand*)command
{
    NSDictionary* metadata = [command.arguments objectAtIndex:0];
    [MFKYC instance].metadata = metadata;
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)showMFKYC:(CDVInvokedUrlCommand*)command
{
    if(matiButton != nil){
        [matiButton sendActionsForControlEvents:UIControlEventTouchUpInside];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
}

@end
