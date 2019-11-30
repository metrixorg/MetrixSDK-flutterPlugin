import 'dart:async';

import 'package:flutter/services.dart';
import 'dart:io' show Platform;

import 'package:metrix/MetrixConfig.dart';


class Metrix {
  static const MethodChannel _channel = const MethodChannel('metrix');

  static Future<void> initialize(MetrixConfig config) async {

    if(!Platform.isAndroid) {
      await _channel
          .invokeMethod('initialize', <String, dynamic>{'appId': config.appId});
    } else {
      await _channel
          .invokeMethod('initialize', config.toJson());
    }
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

  static Future<void> newRevenue(
      String slug, double amount, int currency, String orderId) async {
    await _channel.invokeMethod('newRevenue', <String, dynamic>{
      'slug': slug,
      'amount': amount,
      'currency': currency,
      'orderId': orderId
    });
    return;
  }

}
