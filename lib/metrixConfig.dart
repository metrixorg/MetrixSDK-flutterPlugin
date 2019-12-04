class MetrixConfig {
  String appId;
  int _secretId = 0;
  int _info1;
  int _info2;
  int _info3;
  int _info4;
  bool locationListening;
  bool lunchDeferredDeeplink;
  String store;
  String firebaseAppId;
  int eventUploadThreshold;

  int eventUploadMaxBatchSize;

  int eventMaxCount;

  int eventUploadPeriodMillis;

  int sessionTimeoutMillis;

  int logLevel;

  String trackerToken;
  bool loggingEnabled;

  bool flushEventsOnClose;

  Function sessionIdCallback;
  Function userIdCallback;
  Function attributionCallback;
  Function deferredDeeplinkCallback;

  void setAppSecret(int secretId, int info1, int info2, int info3, int info4) {
    this._secretId = secretId;
    this._info1 = info1;
    this._info2 = info2;
    this._info3 = info3;
    this._info4 = info4;
  }

  MetrixConfig(this.appId);

  Map<String, dynamic> toJson() => {
        'appId': appId,
        'appSecret': {
          'secretId ': _secretId,
          'info1': _info1,
          'info2': _info2,
          'info3': _info3,
          'info4': _info4,
        },
        'locationListening': locationListening,
        'store': store,
        'eventUploadThreshold': eventUploadThreshold,
        'eventUploadMaxBatchSize': eventUploadMaxBatchSize,
        'eventMaxCount': eventMaxCount,
        'eventUploadPeriodMillis': eventUploadPeriodMillis,
        'sessionTimeoutMillis': sessionTimeoutMillis,
        'logLevel': logLevel,
        'trackerToken': trackerToken,
        'loggingEnabled': loggingEnabled,
        'firebaseAppId': firebaseAppId != null ? "${firebaseAppId.replaceAll(":","_")}" : null,
        'lunchDeferredDeeplink': lunchDeferredDeeplink,
        'flushEventsOnClose': flushEventsOnClose,
      };
}
