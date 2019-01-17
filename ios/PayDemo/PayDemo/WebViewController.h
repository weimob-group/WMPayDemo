//
//  WebViewController.h
//  PayTest
//
//  Created by js on 2018/11/23.
//  Copyright © 2018 js. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface WebViewController : UIViewController

@property (nonatomic, copy) NSString *webURL;
@property (nonatomic, copy) NSString *redirectURL;

@end

NS_ASSUME_NONNULL_END
