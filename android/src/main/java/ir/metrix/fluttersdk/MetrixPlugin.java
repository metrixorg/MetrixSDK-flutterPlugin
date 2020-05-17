package ir.metrix.fluttersdk;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import ir.metrix.sdk.Metrix;
import ir.metrix.sdk.MetrixConfig;
import ir.metrix.sdk.MetrixCurrency;
import ir.metrix.sdk.OnAttributionChangedListener;
import ir.metrix.sdk.OnDeeplinkResponseListener;
import ir.metrix.sdk.OnReceiveUserIdListener;
import ir.metrix.sdk.OnSessionIdListener;
import ir.metrix.sdk.network.model.AttributionModel;

/**
 * MetrixPlugin
 */
public class MetrixPlugin implements MethodCallHandler {

    private final Activity activity;
    private Result sessionIdResult;
    private Result userIdResult;
    private Result deferredDeeplinkResult;
    private Result attributionResult;
    private boolean lunchDeferredDeeplink = false;

    public MetrixPlugin(Activity activity) {
        this.activity = activity;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "metrix");
        channel.setMethodCallHandler(new MetrixPlugin(registrar.activity()));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        switch (call.method) {
            case "onCreate":

                JSONObject settings = null;
                try {
                    settings = new JSONObject(call.arguments.toString());

                    MetrixConfig metrixConfig = new MetrixConfig(activity.getApplication(), settings.getString("appId"));

                    if (settings.has("appSecret") && settings.get("appSecret") != JSONObject.NULL) {
                        JSONObject appSecret = settings.getJSONObject("appSecret");
                        if (appSecret.has("secretId") && appSecret.get("secretId") != JSONObject.NULL &&
                                appSecret.has("info1") && appSecret.get("info1") != JSONObject.NULL &&
                                appSecret.has("info2") && appSecret.get("info2") != JSONObject.NULL &&
                                appSecret.has("info3") && appSecret.get("info3") != JSONObject.NULL &&
                                appSecret.has("info4") && appSecret.get("info4") != JSONObject.NULL) {
                            metrixConfig.setAppSecret(
                                    (appSecret.getLong("secretId")),
                                    (appSecret.getLong("info1")),
                                    (appSecret.getLong("info2")),
                                    (appSecret.getLong("info3")),
                                    (appSecret.getLong("info4")));
                        }
                    }
                    
                    if (settings.has("locationListening") && settings.get("locationListening") != JSONObject.NULL) {
                        metrixConfig.setLocationListening(settings.getBoolean("locationListening"));
                    }
                    
                    if (settings.has("lunchDeferredDeeplink") && settings.get("lunchDeferredDeeplink") != JSONObject.NULL) {
                        lunchDeferredDeeplink = settings.getBoolean("lunchDeferredDeeplink");
                    }

                    if (settings.has("eventUploadThreshold") && settings.get("eventUploadThreshold") != JSONObject.NULL) {
                        metrixConfig.setEventUploadThreshold(settings.getInt("eventUploadThreshold"));
                    }

                    if (settings.has("eventUploadMaxBatchSize") && settings.get("eventUploadMaxBatchSize") != JSONObject.NULL) {
                        metrixConfig.setEventUploadMaxBatchSize(settings.getInt("eventUploadMaxBatchSize"));
                    }

                    if (settings.has("eventMaxCount") && settings.get("eventMaxCount") != JSONObject.NULL) {
                        metrixConfig.setEventMaxCount(settings.getInt("eventMaxCount"));
                    }
                    
                    if (settings.has("eventUploadPeriodMillis") && settings.get("eventUploadPeriodMillis") != JSONObject.NULL) {
                        metrixConfig.setEventUploadPeriodMillis(settings.getInt("eventUploadPeriodMillis"));
                    }

                    if (settings.has("sessionTimeoutMillis") && settings.get("sessionTimeoutMillis") != JSONObject.NULL) {
                        metrixConfig.setSessionTimeoutMillis(settings.getInt("sessionTimeoutMillis"));
                    }

                    if (settings.has("loggingEnabled") && settings.get("loggingEnabled") != JSONObject.NULL) {
                        metrixConfig.enableLogging(settings.getBoolean("loggingEnabled"));
                    }
                    
                    if (settings.has("logLevel") && settings.get("logLevel") != JSONObject.NULL) {
                        metrixConfig.setLogLevel(settings.getInt("logLevel"));
                    }

                    if (settings.has("flushEventsOnClose") && settings.get("flushEventsOnClose") != JSONObject.NULL) {
                        metrixConfig.setFlushEventsOnClose(settings.getBoolean("flushEventsOnClose"));
                    }

                    if (settings.has("trackerToken") && settings.get("trackerToken") != JSONObject.NULL) {
                        metrixConfig.setDefaultTrackerToken(settings.getString("trackerToken"));
                    }
                    
                    if (settings.has("firebaseAppId") && settings.get("firebaseAppId") != JSONObject.NULL) {
                        metrixConfig.setFirebaseAppId(settings.getString("firebaseAppId").replace("_", ":"));
                    }

                    if (settings.has("store") && settings.get("store") != JSONObject.NULL) {
                        metrixConfig.setStore(settings.getString("store"));
                    }

                    metrixConfig.setOnAttributionChangedListener(new OnAttributionChangedListener() {
                        @Override
                        public void onAttributionChanged(AttributionModel attributionModel) {
                            if (attributionResult != null) {
                                final String attr = (new Gson()).toJson(attributionModel);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        attributionResult.success(attr);
                                    }
                                });
                            }
                        }
                    });

                    metrixConfig.setOnSessionIdListener(new OnSessionIdListener() {
                        @Override
                        public void onReceiveSessionId(final String s) {
                            if (sessionIdResult != null) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sessionIdResult.success(s);
                                    }
                                });
                            }
                        }
                    });

                    metrixConfig.setOnReceiveUserIdListener(new OnReceiveUserIdListener() {
                        @Override
                        public void onReceiveUserId(final String s) {
                            if (userIdResult != null) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        userIdResult.success(s);
                                    }
                                });
                            }
                        }
                    });

                    metrixConfig.setOnDeeplinkResponseListener(new OnDeeplinkResponseListener() {
                        @Override
                        public boolean launchReceivedDeeplink(final Uri uri) {
                            if (deferredDeeplinkResult != null) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        deferredDeeplinkResult.success(uri.toString());
                                    }
                                });
                            }
                            return lunchDeferredDeeplink;
                        }
                    });

                    Metrix.onCreate(metrixConfig);

                    Metrix.getInstance().activityCreated(activity, new Bundle());
                    Metrix.getInstance().activityStarted(activity);
                    Metrix.getInstance().activityResumed(activity);


                    result.success(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            
            case "setDeferredDeeplinkMethod":
                deferredDeeplinkResult = result;
                break;
            
            case "setAttributionMethod":
                attributionResult = result;
                break;
            
            case "setUserIdMethod":
                userIdResult = result;
                break;
            
            case "setSessionIdMethod":
                sessionIdResult = result;
                break;
            
            case "newEvent": {
                String slug = call.argument("slug");
                Map<String, String> attr = call.argument("attributes");

                Metrix.getInstance().newEvent(slug, attr);
                result.success(null);
                break;
            }
            
            case "newRevenue": {
                String slug = call.argument("slug");
                Double amount = call.argument("amount");
                Integer currency = call.argument("currency");
                String orderId = call.argument("orderId");
                MetrixCurrency metrixCurrency = MetrixCurrency.IRR;
                if (currency != null && currency == 1)
                    metrixCurrency = MetrixCurrency.USD;
                if (currency != null && currency == 2)
                    metrixCurrency = MetrixCurrency.EUR;

                Metrix.getInstance().newRevenue(slug, amount, metrixCurrency, orderId);
                result.success(null);
                break;
            }

            case "addUserAttributes": {
                Map<String, String> attr = call.argument("attributes");
                Metrix.getInstance().addUserAttributes(attr);
                result.success(null);
                break;
            }

            case "appWillOpenUrl": {
                String uri = call.argument("uri");
                Metrix.getInstance().appWillOpenUrl(Uri.parse(uri));
                result.success(null);
                break;
            }
            
            default:
                result.notImplemented();
                break;
        }
    }
}
