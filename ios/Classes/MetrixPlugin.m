#import "MetrixPlugin.h"

@implementation MetrixPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"metrix"
                                     binaryMessenger:[registrar messenger]];
    MetrixPlugin* instance = [[MetrixPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"initialize" isEqualToString:call.method]) {
        
        NSString *yourAppId = call.arguments[@"appId"];
        NSString *environment = MXEnvironmentProduction;
        MXConfig *metrixConfig = [MXConfig configWithAppId:yourAppId
                                               environment:environment];
        
        [Metrix appDidLaunch:metrixConfig];
        result(nil);
    } else if([@"newEvent" isEqualToString:call.method]){
        NSString *slug = call.arguments[@"slug"];
        
        NSDictionary *myAttributes = [[NSMutableDictionary alloc] init];
        NSDictionary *myMetrics =[[NSMutableDictionary alloc] init];
        if (call.arguments[@"attributes"]) {
            myAttributes = call.arguments[@"attributes"];
        }
        if (call.arguments[@"metrics"]) {
            myMetrics = call.arguments[@"metrics"];
        }
        
        MXCustomEvent *event = [MXCustomEvent newEvent:slug attributes:myAttributes metrics:myMetrics];
        [Metrix trackCustomEvent:event];
        result(nil);
    }
    
    else {
        result(FlutterMethodNotImplemented);
    }
}

@end
