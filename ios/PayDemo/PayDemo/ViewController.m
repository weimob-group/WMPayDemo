//
//  ViewController.m
//  PayTest
//
//  Created by js on 2018/11/23.
//  Copyright © 2018 js. All rights reserved.
//

#import "ViewController.h"
#import "WebViewController.h"

@interface ViewController ()

@property (nonatomic, weak) IBOutlet UITextField *urlTextField;
@property (nonatomic, weak) IBOutlet UITextField *redirectUrlTextField;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
//    self.urlTextField.text = @"http://100000069029.retail.n.weimob.com/saas/retail/100000069029/2527729/shop/index?code=001X7hw90J4aVw15clz90oWmw90X7hwU&appid=wx21ddea222c206c85";
    self.urlTextField.text = @"http://3400.retail.n.saas.weimobqa.com/saas/retail/3400/12556400/goods/list";

    self.redirectUrlTextField.text = @"wm123.n.weimob.com://";
}

- (IBAction)testAction:(id)sender{
    if (self.urlTextField.text.length > 0 && self.redirectUrlTextField.text.length > 0){
        
        WebViewController *vc = [[WebViewController alloc] init];
        
        vc.webURL = self.urlTextField.text;
        vc.redirectURL = self.redirectUrlTextField.text;
        
        [self.navigationController pushViewController:vc animated:YES];
    }
}

@end
