/********* MatiSDK.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import <MatiSDK/MatiSDK.h>

@interface MatiSDK : CDVPlugin <MatiButtonResultDelegate>{
    // Member variables go here.
}

- (void)coolMethod:(CDVInvokedUrlCommand*)command;
- (void)setParams:(CDVInvokedUrlCommand*)command;
- (void)setMatiCallback:(CDVInvokedUrlCommand*)command;
- (void)showFlow:(CDVInvokedUrlCommand*)command;
@end

@implementation MatiSDK {
    CDVInvokedUrlCommand* setMatiCallbackCDVInvokedUrlCommand;
    MatiButton* matiButton;
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

- (void)setParams:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* clientId = [command.arguments objectAtIndex:0];
    NSString* flowId = [command.arguments objectAtIndex:1];
    NSDictionary* metaData = [command.arguments objectAtIndex:2];

    self.matiButton = [[MatiButton alloc] init];
    [self.matiButton setParamsWithClientId: clientId flowId: flowId metadata: metaData];
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        self.matiButton.frame = CGRectMake(20, self.view.frame.size.height/2 - 25, self.view.frame.size.width - 40, 50);
    self.matiButton.center = self.view.center;
    self.matiButton.tag = 100;
    self.matiButton.title = @"Custom Title";
    [self.view addSubview:self.matiButton];
}

- (void)setMatiCallback:(CDVInvokedUrlCommand*)command
{
    setMatiCallbackCDVInvokedUrlCommand = command;
    [MatiButtonResult shared].delegate = self;
}

-(void)verificationSuccessWithIdentityId:(NSString *)identityId {
      if(setMatiCallbackCDVInvokedUrlCommand != nil){
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:identityId];
        [pluginResult setKeepCallbackAsBool:YES];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:setMatiCallbackCDVInvokedUrlCommand.callbackId];
    }
}

- (void)verificationCancelled {
     if(setMatiCallbackCDVInvokedUrlCommand != nil){
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString: @"Cancel"];
        [pluginResult setKeepCallbackAsBool:YES];
         [self.commandDelegate sendPluginResult:pluginResult callbackId:setMatiCallbackCDVInvokedUrlCommand.callbackId];
    }
}

- (void)showFlow:(CDVInvokedUrlCommand*)command
{
    if(matiButton != nil){
        [matiButton sendActionsForControlEvents:UIControlEventTouchUpInside];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
}

@end
