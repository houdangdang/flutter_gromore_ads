//
//  ABURitInfo.h
//  ABUAdSDK
//
//  Created by CHAORS on 2021/10/25.
//

#import <Foundation/Foundation.h>

#import "ABUAdSDKConst.h"


@interface ABURitInfo : NSObject

/// ADN的名称，与平台配置一致
@property (nonatomic, copy, readonly) NSString *adnName;

// 代码位
@property (nonatomic, copy, readonly) NSString *slotID;

// 价格标签，多阶底价下有效
@property (nonatomic, copy, readonly) NSString *levelTag;

// 返回价格，nil为无权限
@property (nonatomic, copy, readonly) NSString *ecpm;

// 广告类型
@property (nonatomic, assign, readonly) ABUBiddingType biddingType;

// 额外错误信息,一般为空(扩展字段)
@property (nonatomic, copy, readonly) NSString *errorMsg;

@end

