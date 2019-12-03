import 'dart:convert';

import 'package:flutter/material.dart';

import 'package:metrix/metrix.dart';
import 'package:metrix/metrixConfig.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    MetrixConfig config = new MetrixConfig("ilhskoifzdmyhhk");
//    config.setAppSecret(23, 23124342342, 32432432, 34234, 2322222);
//    config.store = "google play";
//    config.trackerToken = "rtret";
    config.sessionIdCallback = (sesid) => {ss(sesid)};
    config.deeplinkCallback = (sesid) => {print("deeplink: " + sesid)};
    config.userIdCallback = (sesid) => {print("userid: " + sesid)};
    config.attributionCallback = (sesid) => {print("att: " + jsonEncode(sesid))};
    config.firebaseAppId = "1:730143097783:android:227c981a44d0492eaa9e32";
    config.setAppSecret(1, 2, 3, 4, 5);
    Metrix.onCreate(config);
  }

  void ss(String ss) async {
    debugPrint("sessionId: " + ss);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Metrix example app flutter'),
        ),
        floatingActionButton: FloatingActionButton(onPressed: press),
      ),
    );
  }

  void press() {
    Map<String, String> attr = new Map();
    attr["phone"] = "09121234123";
    Map<String, double> metric = new Map();
    metric["length"] = 43.3;

    Metrix.newEvent("dmldm", attr, metric);
  }
}
