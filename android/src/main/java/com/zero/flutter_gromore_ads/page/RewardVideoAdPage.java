package com.zero.flutter_gromore_ads.page;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.MediationConstant;
import com.bytedance.sdk.openadsdk.MediationAdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.zero.flutter_gromore_ads.event.AdEventAction;
import com.zero.flutter_gromore_ads.utils.RewardBundleModel;

import io.flutter.plugin.common.MethodCall;

/**
 * 激励视频广告
 */
public class RewardVideoAdPage extends BaseAdPage implements TTAdNative.RewardVideoAdListener, TTRewardVideoAd.RewardAdInteractionListener {
    private final String TAG = RewardVideoAdPage.class.getSimpleName();
    // 显示广告对象
    private TTRewardVideoAd rvad;
    // 设置激励视频服务端验证的自定义信息
    private String customData;
    // 设置服务端验证的用户信息
    private String userId;

    @Override
    public void loadAd(@NonNull MethodCall call) {
        customData = call.argument("customData");
        userId = call.argument("userId");
        // 配置广告
        adSlot = new AdSlot.Builder()
                .setCodeId(this.posId)
                .setOrientation(TTAdConstant.VERTICAL)
                .setMediationAdSlot(new MediationAdSlot
                        .Builder()
                        .setExtraObject(MediationConstant.ADN_PANGLE, "pangleRewardCustomData")
                        .setExtraObject(MediationConstant.ADN_GDT, "gdtRewardCustomData")
                        .setExtraObject(MediationConstant.ADN_BAIDU, "baiduRewardCustomData")
                        .build())
                .build();
        // 加载广告
        adNativeLoader.loadRewardVideoAd(adslot, this);
    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG, "onInterstitialLoadFail code:" + i + " msg:" + s);
        sendErrorEvent(i, s);
    }
    @Override
    public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
        Log.i(TAG, "onRewardVideoAdLoad");
        rvad = ttRewardVideoAd;
        rvad.setRewardAdInteractionListener(this);
        rvad.setRewardPlayAgainInteractionListener(this);
        // 添加广告事件
        sendEvent(AdEventAction.onAdLoaded);
    }

    @Override
    public void onRewardVideoCached() {
        Log.i(TAG, "onRewardVideoCached");
    }

    @Override
    public void onRewardVideoCached(TTRewardVideoAd ttRewardVideoAd) {
        Log.i(TAG, "onRewardVideoCached ttRewardVideoAd");
        if (rvad != null) {
            rvad.showRewardVideoAd(activity);
        }
        // 添加广告事件
        sendEvent(AdEventAction.onAdPresent);
    }

    @Override
    public void onAdShow() {
        Log.i(TAG, "onAdShow");
        // 添加广告事件
        sendEvent(AdEventAction.onAdExposure);
    }

    @Override
    public void onAdVideoBarClick() {
        Log.i(TAG, "onAdVideoBarClick");
        // 添加广告事件
        sendEvent(AdEventAction.onAdClicked);
    }

    @Override
    public void onAdClose() {
        Log.i(TAG, "onAdClose");
        // 添加广告事件
        sendEvent(AdEventAction.onAdClosed);
        rvad = null;
    }

    @Override
    public void onVideoComplete() {
        Log.i(TAG, "onVideoComplete");
        // 添加广告事件
        sendEvent(AdEventAction.onAdComplete);
    }

    @Override
    public void onVideoError() {
        Log.i(TAG, "onVideoError");
    }

    // 视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励数量，rewardName：奖励名称，code：错误码，msg：错误信息
    @Override
    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName, int code, String msg) {
        String logString ="verify:" + rewardVerify + " amount:" + rewardAmount +
                " name:" + rewardName + " errorCode:" + code + " errorMsg:" + msg;
        Log.e(TAG, "onRewardVerify " + logString);
//        sendEvent(new AdRewardEvent(posId,0, rewardVerify, rewardAmount, rewardName, code, msg, customData, userId));
    }

    @Override
    public void onRewardArrived(boolean isRewardValid, int rewardType, Bundle extraInfo) {
        RewardBundleModel rewardBundleModel = new RewardBundleModel(extraInfo);
        String logString = "rewardType："+rewardType+" verify:" + isRewardValid + " amount:" + rewardBundleModel.getRewardAmount() +
                " name:" + rewardBundleModel.getRewardName() + " errorCode:" + rewardBundleModel.getServerErrorCode() + " errorMsg:" + rewardBundleModel.getServerErrorMsg();
        Log.e(TAG, "onRewardArrived " + logString);
        sendEvent(new AdRewardEvent(posId,rewardType, isRewardValid, rewardBundleModel.getRewardAmount(), rewardBundleModel.getRewardName(), rewardBundleModel.getServerErrorCode(), rewardBundleModel.getServerErrorMsg(), customData, userId));
    }

    @Override
    public void onSkippedVideo() {
        Log.i(TAG, "onSkippedVideo");
        // 添加广告事件
        sendEvent(AdEventAction.onAdSkip);
    }
}