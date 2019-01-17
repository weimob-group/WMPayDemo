//
//  URLUtil.m
//  PayTest
//
//  Created by js on 2018/11/25.
//  Copyright © 2018 js. All rights reserved.
//

#import "URLUtil.h"

@implementation URLUtil
+ (NSDictionary *)getURLAndParameter:(NSString *)url{
    NSArray *components = [url componentsSeparatedByString:@"?"];
    NSString *path = components.firstObject;
    if (path.length > 0){
        NSMutableDictionary *mutDict = [NSMutableDictionary dictionary];
        mutDict[@"URL"] = path;
        NSString *dataStr = [url substringFromIndex:path.length];
        if (dataStr.length > 0){
            //去掉?
            dataStr = [dataStr substringFromIndex:1];
            
            NSMutableDictionary *params = [NSMutableDictionary dictionary];
            NSArray* paramParis = [dataStr componentsSeparatedByString:@"&"];
            for (NSString *param in paramParis){
                NSArray *kv = [param componentsSeparatedByString:@"="];
                [params setObject:kv.lastObject forKey:kv.firstObject];
            }
            mutDict[@"PARAMS"] = params;
            
        }
        
        return mutDict;
    }
    return nil;
}

+ (NSString *)generateURL:(NSString *)host params:(NSDictionary *)params{
    NSMutableString *mutString = [NSMutableString string];
    [mutString appendString:host];
    NSArray *allKeys = params.allKeys;;
    if (allKeys.count > 0){
        [mutString appendString:@"?"];
        for (int i = 0; i < allKeys.count; i++){
            id key = allKeys[i];
            id value = params[key];
            [mutString appendString:[NSString stringWithFormat:@"%@=%@",key,value]];
            if (i < params.count - 1){
                [mutString appendString:@"&"];
            }
            
        }
    }
    return mutString;
}
@end
