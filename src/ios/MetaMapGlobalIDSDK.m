/********* MetaMapGlobalIDSDK.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import <MetaMapSDK/MetaMapSDK.h>

@interface MetaMapGlobalIDSDK : CDVPlugin <MetaMapButtonResultDelegate>
@end

@implementation MetaMapGlobalIDSDK {
    CDVInvokedUrlCommand *setMetaMapCallbackCDVInvokedUrlCommand;
}

#define isNull(value) (value == nil || [value isKindOfClass:[NSNull class]])

- (void)coolMethod:(CDVInvokedUrlCommand *)command {
    NSString *echo = [command.arguments objectAtIndex:0];
    CDVPluginResult *pluginResult = (echo != nil && [echo length] > 0)
                                    ? [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo]
                                    : [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)showMetaMapFlow:(CDVInvokedUrlCommand *)command {
    NSLog(@"[MetaMap] showMetaMapFlow called");

    NSString *clientId = nil;
    NSString *flowId = nil;
    NSString *configurationId = nil;
    NSString *encryptionConfigurationId = nil;
    NSDictionary *metadata = @{@"sdkType": @"cordova"};

    if (command.arguments.count > 0) {
        NSDictionary *options = [command argumentAtIndex:0];

        clientId = isNull(options[@"clientId"]) ? nil : options[@"clientId"];
        flowId = isNull(options[@"flowId"]) ? nil : options[@"flowId"];
        metadata = isNull(options[@"metadata"]) ? metadata : options[@"metadata"];
        configurationId = isNull(options[@"configurationId"]) ? nil : options[@"configurationId"];
        encryptionConfigurationId = isNull(options[@"encryptionConfigurationId"]) ? nil : options[@"encryptionConfigurationId"];

        if (!clientId) {
            NSLog(@"[MetaMap] ❌ Client ID is missing");
            CDVPluginResult *errorResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Client ID is required"];
            [self.commandDelegate sendPluginResult:errorResult callbackId:command.callbackId];
            return;
        }

        dispatch_async(dispatch_get_main_queue(), ^{
            NSLog(@"[MetaMap] 🚀 Starting flow with clientId: %@, flowId: %@", clientId, flowId);
            [MetaMap.shared showMetaMapFlowWithClientId:clientId
                                                 flowId:flowId
                                        configurationId:configurationId
                              encryptionConfigurationId:encryptionConfigurationId
                                               metadata:metadata];

            CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        });

    } else {
        NSLog(@"[MetaMap] ❌ No arguments provided for showMetaMapFlow");
        CDVPluginResult *errorResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Missing arguments"];
        [self.commandDelegate sendPluginResult:errorResult callbackId:command.callbackId];
    }
}

- (void)setMetaMapCallback:(CDVInvokedUrlCommand *)command {
    NSLog(@"[MetaMap] ✅ Callback registered");
    setMetaMapCallbackCDVInvokedUrlCommand = command;
    [MetaMapButtonResult shared].delegate = self;
}

#pragma mark - MetaMapButtonResultDelegate
- (void)verificationSuccessWithIdentityId:(NSString *)identityId verificationID:(nullable NSString *)verificationID {
    NSLog(@"[MetaMap] ✅ Verification success: identityId=%@, verificationID=%@", identityId, verificationID);
    if (setMetaMapCallbackCDVInvokedUrlCommand) {
        NSDictionary *dict = @{
                @"eventType": @"success",
                @"identityId": identityId ?: @"",
                @"verificationID": verificationID ?: @""
        };
        CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dict];
        [result setKeepCallbackAsBool:YES];
        [self.commandDelegate sendPluginResult:result callbackId:setMetaMapCallbackCDVInvokedUrlCommand.callbackId];
    }
}

- (void)verificationCreatedWithIdentityId:(nullable NSString *)identityId verificationID:(nullable NSString *)verificationID {
    NSLog(@"[MetaMap] 📦 Verification created: identityId=%@, verificationID=%@", identityId, verificationID);
    if (setMetaMapCallbackCDVInvokedUrlCommand) {
        NSDictionary *dict = @{
                @"eventType": @"created",
                @"identityId": identityId ?: @"",
                @"verificationID": verificationID ?: @""
        };
        CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dict];
        [result setKeepCallbackAsBool:YES];
        [self.commandDelegate sendPluginResult:result callbackId:setMetaMapCallbackCDVInvokedUrlCommand.callbackId];
    }
}

- (void)verificationCancelledWithIdentityId:(NSString *)identityId verificationID:(nullable NSString *)verificationID {
    NSLog(@"[MetaMap] ❌ Verification cancelled: identityId=%@, verificationID=%@", identityId, verificationID);
    if (setMetaMapCallbackCDVInvokedUrlCommand) {
        NSDictionary *dict = @{
                @"eventType": @"cancelled",
                @"identityId": identityId ?: @"",
                @"verificationID": verificationID ?: @""
        };
        CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:dict];
        [result setKeepCallbackAsBool:YES];
        [self.commandDelegate sendPluginResult:result callbackId:setMetaMapCallbackCDVInvokedUrlCommand.callbackId];
    }
}

@end