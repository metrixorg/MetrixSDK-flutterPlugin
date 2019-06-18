import 'dart:async';

import 'package:flutter/services.dart';

class Metrix {
  static const MethodChannel _channel = const MethodChannel('metrix');

  static Future<void> initialize(String appId) async {
    await _channel
        .invokeMethod('initialize', <String, dynamic>{'appId': appId});
    return;
  }

  static Future<void> newEvent(String slug, Map<String, String> attributes,
      Map<String, double> metrics) async {
    await _channel.invokeMethod('newEvent', <String, dynamic>{
      'slug': slug,
      'attributes': attributes,
      'metrics': metrics
    });
    return;
  }
  static Future<void> setDefaultTracker(String trackerToken) async {
    await _channel.invokeMethod('setDefaultTracker', <String, dynamic>{
      'trackerToken': trackerToken
    });
    return;
  }
  static Future<void> newRevenue(String slug,double amount, int currency,
      String orderId) async {
    await _channel.invokeMethod('newRevenue', <String, dynamic>{
      'slug': slug,
      'amount': amount,
      'currency': currency,
      'orderId': orderId
    });
    return;
  }
}
