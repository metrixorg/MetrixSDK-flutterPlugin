import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:metrix/metrix.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  void initState() {
    super.initState();
    Metrix.initialize("ilhskoifzdmyhhk");
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
    Metrix.newRevenue("dmldm", 34400, 0,null);
  }
}
