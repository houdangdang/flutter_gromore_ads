package com.zero.flutter_gromore_ads.load;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.zero.flutter_gromore_ads.event.AdEventAction;
import com.zero.flutter_gromore_ads.page.BaseAdPage;

import java.util.ArrayList;
import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * 信息流加载对象
 */
public class FeedAdLoad extends BaseAdPage implements TTAdNative.FeedAdListener {
    private final String TAG =  FeedAdManager.class.getSimpleName();
    private MethodChannel.Result result;

    /**
     * 加载信息流广告列表
     * @param call
     * @param result
     */
    public void loadFeedAdList(Activity activity, @NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Log.i(TAG, "loadFeedAdList");
        this.result=result;
        showAd(activity,call);
    }

    @Override
    public void loadAd(@NonNull MethodCall call) {
        Log.i(TAG, "loadAd");
        // 获取请求模板广告素材的尺寸
        int expressViewWidth = call.argument("width");
        int expressViewHeight = call.argument("height");
        int count = call.argument("count");
        adslot = new AdSlot.Builder()
                .setCodeId(posId)
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight)
                .setAdCount(count)
                .setSupportDeepLink(true)
                .build();
        adNativeLoader.loadFeedAd(adslot, this);
    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG, "onError code:" + i + " msg:" + s);
        sendErrorEvent(i, s);
        this.result.error(""+i,s,s);
    }

    @Override
    public void onFeedAdLoad(List<TTFeedAd> list) {
        List<Integer> adResultList=new ArrayList<>();
        Log.i(TAG, "onFeedAdLoad" + adResultList);
        if (list == null || list.size() == 0) {
            this.result.success(adResultList);
            return;
        }
        for (TTFeedAd adItem : list) {
            int key=adItem.hashCode();
            adResultList.add(key);
            FeedAdManager.getInstance().putAd(key,adItem);
        }
        // 添加广告事件
        sendEvent(AdEventAction.onAdLoaded);
        this.result.success(adResultList);
    }
}