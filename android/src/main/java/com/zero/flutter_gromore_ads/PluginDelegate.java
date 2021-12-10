package com.zero.flutter_gromore_ads;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.msdk.api.v2.GMAdConfig;
import com.bytedance.msdk.api.v2.GMMediationAdSdk;
import com.zero.flutter_gromore_ads.page.AdSplashActivity;
import com.zero.flutter_gromore_ads.page.FullVideoPage;
import com.zero.flutter_gromore_ads.page.InterstitialFullPage;
import com.zero.flutter_gromore_ads.page.InterstitialPage;

import java.util.ArrayList;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/// 插件代理
public class PluginDelegate implements MethodChannel.MethodCallHandler, EventChannel.StreamHandler {
    private final String TAG = PluginDelegate.class.getSimpleName();
    // Flutter 插件绑定对象
    public FlutterPlugin.FlutterPluginBinding bind;
    // 当前 Activity
    public Activity activity;
    // 返回通道
    private MethodChannel.Result result;
    // 事件通道
    private EventChannel.EventSink eventSink;
    // 插件代理对象
    private static PluginDelegate _instance;

    public static PluginDelegate getInstance() {
        return _instance;
    }

    // Banner View
    public static final String KEY_BANNER_VIEW = "flutter_gromore_ads_banner";
    // Feed View
    public static final String KEY_FEED_VIEW = "flutter_gromore_ads_feed";
    // 广告参数
    public static final String KEY_POSID = "posId";
    // logo 参数
    public static final String KEY_LOGO = "logo";
    // timeout 参数
    public static final String KEY_TIMEOUT = "timeout";
    // splashButtonType 参数
    public static final String KEY_SPLASH_BUTTON_TYPE = "buttonType";

    /**
     * 插件代理构造函数构造函数
     *
     * @param activity      Activity
     * @param pluginBinding FlutterPluginBinding
     */
    public PluginDelegate(Activity activity, FlutterPlugin.FlutterPluginBinding pluginBinding) {
        this.activity = activity;
        this.bind = pluginBinding;
        _instance = this;
    }

    /**
     * 方法通道调用
     *
     * @param call   方法调用对象
     * @param result 回调结果对象
     */
    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        String method = call.method;
        Log.d(TAG, "MethodChannel onMethodCall method:" + method + " arguments:" + call.arguments);
        if ("requestPermissionIfNecessary".equals(method)) {
            requestPermissionIfNecessary(call, result);
        } else if ("initAd".equals(method)) {
            initAd(call, result);
        } else if ("showSplashAd".equals(method)) {
            showSplashAd(call, result);
        } else if ("showInterstitialAd".equals(method)) {
            showInterstitialAd(call, result);
        } else if ("showInterstitialFullAd".equals(method)) {
            showInterstitialFullAd(call, result);
        } else if ("showFullVideoAd".equals(method)) {
            showFullVideoAd(call, result);
        } else if ("showRewardVideoAd".equals(method)) {
            showRewardVideoAd(call, result);
        } else if ("loadFeedAd".equals(method)) {
            loadFeedAd(call, result);
        } else if ("clearFeedAd".equals(method)) {
            clearFeedAd(call, result);
        } else {
            result.notImplemented();
        }
    }

    /**
     * 建立事件通道监听
     *
     * @param arguments 参数
     * @param events    事件回调对象
     */
    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        Log.d(TAG, "EventChannel onListen arguments:" + arguments);
        eventSink = events;
    }

    /**
     * 取消事件通道监听
     *
     * @param arguments 参数
     */
    @Override
    public void onCancel(Object arguments) {
        Log.d(TAG, "EventChannel onCancel");
        eventSink = null;
    }

    /**
     * 添加事件
     *
     * @param event 事件
     */
    public void addEvent(Object event) {
        if (eventSink != null) {
            Log.d(TAG, "EventChannel addEvent event:" + event.toString());
            eventSink.success(event);
        }
    }

    /**
     * 展示 Banner 广告
     */
    public void registerBannerView() {
//        bind.getPlatformViewRegistry()
//                .registerViewFactory(KEY_BANNER_VIEW, new NativeViewFactory(KEY_BANNER_VIEW, this));
    }

    /**
     * 展示 Feed 信息流广告
     */
    public void registerFeedView() {
//        bind.getPlatformViewRegistry()
//                .registerViewFactory(KEY_FEED_VIEW, new NativeViewFactory(KEY_FEED_VIEW, this));
    }

    /**
     * 请求权限
     *
     * @param call   MethodCall
     * @param result Result
     */
    public void requestPermissionIfNecessary(MethodCall call, MethodChannel.Result result) {
        GMMediationAdSdk.requestPermissionIfNecessary(activity);
        result.success(true);
    }

    /**
     * 初始化广告
     *
     * @param call   MethodCall
     * @param result Result
     */
    public void initAd(MethodCall call, final MethodChannel.Result result) {
        String appId = call.argument("appId");
        boolean useTextureView = call.argument("useTextureView");
        boolean supportMultiProcess = call.argument("supportMultiProcess");
        boolean allowShowNotify = call.argument("allowShowNotify");
        ArrayList directDownloadNetworkType = call.argument("directDownloadNetworkType");
//        int[] directDownloadNetworkTypeList = DataUtils.convertIntegers(directDownloadNetworkType);
        // 构建配置
        GMAdConfig config = new GMAdConfig.Builder()
                .setAppId(appId)
                .setAppName("测试App")
                .setDebug(BuildConfig.DEBUG)
                .setOpenAdnTest(BuildConfig.DEBUG)
                .build();
        // 初始化 SDK
        GMMediationAdSdk.initialize(activity.getApplicationContext(), config);
        result.success(true);
    }

    /**
     * 显示开屏广告
     *
     * @param call   MethodCall
     * @param result Result
     */
    public void showSplashAd(MethodCall call, MethodChannel.Result result) {
        String posId = call.argument(KEY_POSID);
        String logo = call.argument(KEY_LOGO);
        double timeout = call.argument(KEY_TIMEOUT);
        int buttonType = call.argument(KEY_SPLASH_BUTTON_TYPE);
        Intent intent = new Intent(activity, AdSplashActivity.class);
        intent.putExtra(KEY_POSID, posId);
        intent.putExtra(KEY_LOGO, logo);
        intent.putExtra(KEY_TIMEOUT, timeout);
        intent.putExtra(KEY_SPLASH_BUTTON_TYPE, buttonType);
        activity.startActivity(intent);
        // 设置进入动画
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        result.success(true);
    }

    /**
     * 显示插屏广告
     *
     * @param call   MethodCall
     * @param result Result
     */
    public void showInterstitialAd(MethodCall call, MethodChannel.Result result) {
        InterstitialPage adPage = new InterstitialPage();
        adPage.showAd(activity, call);
        result.success(true);
    }

    /**
     * 显示插屏全屏广告
     *
     * @param call   MethodCall
     * @param result Result
     */
    public void showInterstitialFullAd(MethodCall call, MethodChannel.Result result) {
        InterstitialFullPage adPage = new InterstitialFullPage();
        adPage.showAd(activity, call);
        result.success(true);
    }

    /**
     * 显示全屏视频广告
     *
     * @param call   MethodCall
     * @param result Result
     */
    public void showFullVideoAd(MethodCall call, MethodChannel.Result result) {
        FullVideoPage adPage = new FullVideoPage();
        adPage.showAd(activity, call);
        result.success(true);
    }

    /**
     * 显示激励视频广告
     *
     * @param call   MethodCall
     * @param result Result
     */
    public void showRewardVideoAd(MethodCall call, MethodChannel.Result result) {
//        RewardVideoPage adPage = new RewardVideoPage();
//        adPage.showAd(activity, call);
//        result.success(true);
    }

    /**
     * 加载信息流广告列表
     *
     * @param call   MethodCall
     * @param result Result
     */
    public void loadFeedAd(MethodCall call, MethodChannel.Result result) {
//        FeedAdLoad feedAd = new FeedAdLoad();
//        feedAd.loadFeedAdList(activity, call, result);
    }

    /**
     * 删除信息流广告列表
     *
     * @param call   MethodCall
     * @param result Result
     */
    public void clearFeedAd(MethodCall call, MethodChannel.Result result) {
//        List<Integer> adList = call.argument("list");
//        if (adList != null) {
//            for (int ad : adList) {
//                FeedAdManager.getInstance().removeAd(ad);
//            }
//        }
//        result.success(true);

    }
}
