//
//  URLUtil.h
//  PayTest
//
//  Created by js on 2018/11/25.
//  Copyright © 2018 js. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface URLUtil : NSObject
+ (NSDictionary *)getURLAndParameter:(NSString *)url;
+ (NSString *)generateURL:(NSString *)host params:(NSDictionary *)params;
@end

NS_ASSUME_NONNULL_END
