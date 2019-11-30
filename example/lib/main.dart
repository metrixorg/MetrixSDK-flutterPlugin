import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:metrix/metrix.dart';
import 'package:metrix/MetrixConfig.dart';

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
    config.setAppSecret(23, 23124342342, 32432432, 34234, 2322222);
    config.store = "dfff";
    config.trackerToken = "rtret";
    Metrix.initialize(config);

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
