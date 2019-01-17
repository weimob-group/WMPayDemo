//
//  WebViewController.m
//  PayTest
//
//  Created by js on 2018/11/23.
//  Copyright © 2018 js. All rights reserved.
//

#import "WebViewController.h"
#import <WebKit/WebKit.h>
#import "URLUtil.h"
@interface WebViewController ()<WKNavigationDelegate,UIWebViewDelegate>

@property (nonatomic,strong) UIWebView *webview;

@end

@implementation WebViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    UIBarButtonItem *leftBarButton = [[UIBarButtonItem alloc] initWithTitle:@"返回" style:UIBarButtonItemStylePlain target:self action:@selector(backAction:)];
    
    UIBarButtonItem *closeBarButton = [[UIBarButtonItem alloc] initWithTitle:@"关闭" style:UIBarButtonItemStylePlain target:self action:@selector(closeAction:)];
    self.navigationItem.leftBarButtonItems = @[leftBarButton,closeBarButton];
    
    [self.view addSubview:self.webview];
    [self.webview loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:self.webURL]]];

    //    [self.view addSubview:self.wkWebView];
    
    //    [self.wkWebView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:self.webURL]]];
}


- (IBAction)backAction:(id)sender{
    [self.webview goBack];
}

- (IBAction)closeAction:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
}

- (UIWebView *)webview{
    if (!_webview){
        CGFloat originY = 64;
        CGRect frame = CGRectMake(0, originY, [UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height - originY);
        _webview = [[UIWebView alloc] initWithFrame:frame];
        _webview.delegate = self;
    }
    return _webview;
}


+ (NSString *)urlEncode:(NSString *)url{
    NSString *encodedURL = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
    return encodedURL;
}

+ (NSString *)decodeURL:(NSString *)url{
    NSString *decodeURL = [url stringByRemovingPercentEncoding];
    return decodeURL;
}

//- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler {
//    NSURLRequest *request = navigationAction.request;
//    NSString *wxPre = @"https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb";
//    if ([request.URL.absoluteString hasPrefix:wxPre] && ![request.URL.absoluteString containsString:@"?redirect_url="]) {
//        NSMutableURLRequest *newRequest = [[NSMutableURLRequest alloc] init];
//        newRequest.allHTTPHeaderFields = request.allHTTPHeaderFields;
//        NSString *newURLStr = [NSString stringWithFormat:@"%@?redirect_url=%@",wxPre,[[self class] urlEncode:self.redirectURL]];
//        //TODO: 对newURLStr追加或修改参数redirect_url=URLEncode(A.company.com://)
//        newRequest.URL = [NSURL URLWithString:newURLStr];
//        [webView loadRequest:newRequest];
//        decisionHandler(WKNavigationActionPolicyCancel);
//    }
//    else {
//        decisionHandler(WKNavigationActionPolicyAllow);
//    }
//}


- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType{
    NSString *reqUrl = request.URL.absoluteString;
    NSLog(@"reqUrl ===== %@",reqUrl);
    NSString *wxPre = @"https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb";
    NSString *redirectURLKey = @"redirect_url";
    
    if ([request.URL.absoluteString hasPrefix:wxPre] && ![request.URL.absoluteString containsString:@"wmreqmodify=1"]) {
        
        NSDictionary *parseDict = [URLUtil getURLAndParameter:reqUrl];
        
        NSMutableDictionary *pathParams = [parseDict[@"PARAMS"] mutableCopy];
        pathParams[redirectURLKey] = [[self class] urlEncode:[NSString stringWithFormat:@"%@%@",self.redirectURL,pathParams[redirectURLKey]]];
        pathParams[@"wmreqmodify"] = @"1";
        
        NSString *path = parseDict[@"URL"];
        
        NSString *newURLStr = [URLUtil generateURL:path params:pathParams];
        
        NSMutableURLRequest *newRequest = [[NSMutableURLRequest alloc] init];
        newRequest.allHTTPHeaderFields = request.allHTTPHeaderFields;
        
        //TODO: 对newURLStr追加或修改参数redirect_url=URLEncode(xxx.n.weimob.com://)
        newRequest.URL = [NSURL URLWithString:newURLStr];
        [webView loadRequest:newRequest];
        
        NSLog(@"newURLStr ===== %@",newURLStr);
        
        return NO;
    } else if ([reqUrl hasPrefix:@"weixin://"]) {
        if (@available(iOS 10.0, *)) {
            [[UIApplication sharedApplication] openURL:request.URL options:@{} completionHandler:nil];
        } else {
            [[UIApplication sharedApplication] openURL:request.URL];
        }
        return NO;
    } else if ([reqUrl hasPrefix:self.redirectURL]){
        //获取原始的 redirect_url 加载
        NSString *originRedirectURL = [reqUrl substringFromIndex:self.redirectURL.length];
        NSLog(@"%@",originRedirectURL);
        
        NSString *decodeURL = [[self class] decodeURL:originRedirectURL];
        NSMutableURLRequest *newRequest = [[NSMutableURLRequest alloc] init];
        newRequest.allHTTPHeaderFields = request.allHTTPHeaderFields;
        newRequest.URL = [NSURL URLWithString:decodeURL];
        [webView loadRequest:newRequest];
        
        return NO;
    }
  
    return YES;
}

@end
