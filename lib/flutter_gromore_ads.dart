import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

import 'event/ad_event_handler.dart';

export 'event/ad_event_handler.dart';
export 'view/ad_banner_widget.dart';
export 'view/ad_feed_widget.dart';

/// GroMore 广告插件
class FlutterGromoreAds {
  // 方法通道
  static const MethodChannel _methodChannel =
      MethodChannel('flutter_gromore_ads');
  // 事件通道
  static const EventChannel _eventChannel =
      EventChannel('flutter_gromore_ads_event');

  ///事件回调
  ///@params onData 事件回调
  static Future<void> onEventListener(
      OnAdEventListener onAdEventListener) async {
    _eventChannel.receiveBroadcastStream().listen((data) {
      handleAdEvent(data, onAdEventListener);
    });
  }

  /// 请求应用跟踪透明度授权(仅 iOS)
  static Future<bool> get requestIDFA async {
    if (Platform.isIOS) {
      final bool result = await _methodChannel.invokeMethod('requestIDFA');
      return result;
    }
    return true;
  }

  /// 动态请求相关权限（仅 Android）
  static Future<bool> get requestPermissionIfNecessary async {
    if (Platform.isAndroid) {
      final bool result =
          await _methodChannel.invokeMethod('requestPermissionIfNecessary');
      return result;
    }
    return true;
  }

  /// 初始化广告
  /// [appId] 应用ID
  /// [config] 配置文件名称
  /// [limitPersonalAds] 是否限制个性化广告，0：不限制 1：限制
  static Future<bool> initAd(String appId,
      {String? config, int limitPersonalAds = 0}) async {
    final bool result = await _methodChannel.invokeMethod(
      'initAd',
      {
        'appId': appId,
        'config': config,
        'limitPersonalAds': limitPersonalAds,
      },
    );
    return result;
  }

  /// 展示开屏广告
  /// [posId] 广告位 id
  /// [logo] 如果传值则展示底部logo，不传不展示，则全屏展示
  /// [timeout] 加载超时时间
  static Future<bool> showSplashAd(String posId,
      {String? logo, double timeout = 3.5}) async {
    final bool result = await _methodChannel.invokeMethod(
      'showSplashAd',
      {
        'posId': posId,
        'logo': logo,
        'timeout': timeout,
      },
    );
    return result;
  }

  /// 展示插屏广告
  /// [posId] 广告位 id
  static Future<bool> showInterstitialAd(String posId) async {
    final bool result = await _methodChannel.invokeMethod(
      'showInterstitialAd',
      {'posId': posId},
    );
    return result;
  }

  /// 展示激励视频广告
  /// [posId] 广告位 id
  /// [customData] 设置服务端验证的自定义信息
  /// [userId] 设置服务端验证的用户信息
  static Future<bool> showRewardVideoAd(
      String posId, {
        String? customData,
        String? userId,
      }) async {
    final bool result = await _methodChannel.invokeMethod(
      'showRewardVideoAd',
      {
        'posId': posId,
        'customData': customData,
        'userId': userId,
      },
    );
    return result;
  }

  /// 加载信息流广告列表
  /// [posId] 广告位 id
  /// [width] 宽度
  /// [height] 高度
  /// [count] 获取广告数量，建议 1~3 个
  static Future<List<int>> loadFeedAd(String posId,
      {int width = 375, int height = 0, int count = 1}) async {
    final List<dynamic> result = await _methodChannel.invokeMethod(
      'loadFeedAd',
      {
        'posId': posId,
        'width': width,
        'height': height,
        'count': count,
      },
    );
    return List<int>.from(result);
  }

  /// 清除信息流广告列表
  /// [list] 信息流广告 id 列表
  static Future<bool> clearFeedAd(List<int> list) async {
    final bool result = await _methodChannel.invokeMethod(
      'clearFeedAd',
      {
        'list': list,
      },
    );
    return result;
  }

  /// 设置个性化推荐
  /// @params personalAdsType,不传或传空或传非01值没任何影响,默认不屏蔽, 0屏蔽个性化推荐广告, 1不屏蔽个性化推荐广告
  static setUserExtData({required String personalAdsType}) async {
    await _methodChannel.invokeMethod(
      'setUserExtData',
      {
        'personalAdsType': personalAdsType,
      },
    );
  }
}
