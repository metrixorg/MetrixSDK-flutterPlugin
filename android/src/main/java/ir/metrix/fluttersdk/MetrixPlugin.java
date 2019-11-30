package ir.metrix.fluttersdk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

/**
 * MetrixPlugin
 */
public class MetrixPlugin implements MethodCallHandler {

    private final Activity activity;

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

                MetrixConfig metrixConfig = new MetrixConfig(activity.getApplication(),
                        settings.getString("appId"));

                if (settings.has("appSecret")) {
                    JSONObject appSecret = settings.getJSONObject("appSecret");
                    metrixConfig.setAppSecret(
                            (appSecret.getLong("secretId")),
                            (appSecret.getLong("info1")),
                            (appSecret.getLong("info2")),
                            (appSecret.getLong("info3")),
                            (appSecret.getLong("info4")));
                }
                if (settings.has("locationListening")) {
                    metrixConfig.setLocationListening(settings.getBoolean("locationListening"));
                }

                if (settings.has("eventUploadThreshold")) {
                    metrixConfig.setEventUploadThreshold(settings.getInt("eventUploadThreshold"));
                }

                if (settings.has("eventUploadMaxBatchSize")) {
                    metrixConfig.setEventUploadMaxBatchSize(settings.getInt("eventUploadMaxBatchSize"));
                }

                if (settings.has("eventMaxCount")) {
                    metrixConfig.setEventMaxCount(settings.getInt("eventMaxCount"));
                }
                if (settings.has("eventUploadPeriodMillis")) {
                    metrixConfig.setEventUploadPeriodMillis(settings.getInt("eventUploadPeriodMillis"));
                }

                if (settings.has("sessionTimeoutMillis")) {
                    metrixConfig.setSessionTimeoutMillis(settings.getInt("sessionTimeoutMillis"));
                }

                if (settings.has("enableLogging")) {
                    metrixConfig.enableLogging(settings.getBoolean("enableLogging"));
                }
                if (settings.has("logLevel")) {
                    metrixConfig.setLogLevel(settings.getInt("logLevel"));
                }

                if (settings.has("flushEventsOnClose")) {
                    metrixConfig.setFlushEventsOnClose(settings.getBoolean("flushEventsOnClose"));

                }

                if (settings.has("defaultTrackerToken")) {
                    metrixConfig.setDefaultTrackerToken(settings.getString("defaultTrackerToken"));
                }

                if (settings.has("store")) {
                    metrixConfig.setStore(settings.getString("store"));
                }


                Metrix.onCreate(metrixConfig);

                Metrix.getInstance().activityCreated(activity, new Bundle());
                Metrix.getInstance().activityStarted(activity);
                Metrix.getInstance().activityResumed(activity);


                result.success(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case "newEvent": {
                String slug = call.argument("slug");
                Map<String, String> attr = call.argument("attributes");
                Map<String, Double> metrics = call.argument("metrics");

                Metrix.getInstance().newEvent(slug, attr, metrics);
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
            default:
                result.notImplemented();
                break;
        }
    }
}
