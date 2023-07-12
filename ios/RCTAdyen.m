#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RCTAdyen, NSObject)

RCT_EXTERN_METHOD(encrypt:(NSString)publicKey
                  data:(NSDictionary)data
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(handleComponent:(NSString)clientKey
                  environment:(NSString)environment
                  data:(NSDictionary)data
                  amount:(NSDictionary)amount
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(handleAction:(NSString)clientKey
                  environment:(NSString)environment
                  data:(NSDictionary)data
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

@end
