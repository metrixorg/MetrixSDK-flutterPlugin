package ir.metrix.fluttersdk;

import android.app.Activity;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import ir.metrix.sdk.Metrix;
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
            case "initialize":

                String appId = call.argument("appId");
                Metrix.initialize(activity, appId);
                result.success(null);
                break;
            case "newEvent": {
                String slug = call.argument("slug");
                Map attr = call.argument("attributes");
                Map metrics = call.argument("metrics");

                Metrix.getInstance().newEvent(slug, attr, metrics);
                result.success(null);
                break;
            }
            case "setDefaultTracker":
                String trackerToken = call.argument("trackerToken");
                Metrix.getInstance().setDefaultTracker(trackerToken);
                result.success(null);
                break;
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
            case "screenDisplayed":
                String screen = call.argument("screen");
                Metrix.getInstance().screenDisplayed(screen);
                result.success(null);

                break;
            case "setEventUploadThreshold":
                Integer eventUploadThreshold = call.argument("eventUploadThreshold");
                Metrix.getInstance().setEventUploadThreshold(eventUploadThreshold);
                result.success(null);

                break;
            case "setEventMaxCount":
                Integer eventMaxCount = call.argument("eventMaxCount");
                Metrix.getInstance().setEventMaxCount(eventMaxCount);
                result.success(null);

                break;
            case "setEventUploadMaxBatchSize":
                Integer eventUploadMaxBatchSize = call.argument("eventUploadMaxBatchSize");
                Metrix.getInstance().setEventUploadMaxBatchSize(eventUploadMaxBatchSize);
                result.success(null);

                break;
            case "setEventUploadPeriodMillis":
                Integer eventUploadPeriodMillis = call.argument("eventUploadPeriodMillis");
                Metrix.getInstance().setEventUploadPeriodMillis(eventUploadPeriodMillis);
                result.success(null);

                break;
            case "setAppSecret":

                Long secretId = Long.parseLong(call.argument("secretId").toString());
                Long info1 = Long.parseLong(call.argument("info1").toString());
                Long info2 = Long.parseLong(call.argument("info2").toString());
                Long info3 = Long.parseLong(call.argument("info3").toString());
                Long info4 = Long.parseLong(call.argument("info4").toString());


                Metrix.getInstance().setAppSecret(secretId, info1, info2, info3, info4);
                result.success(null);

                break;
            default:
                result.notImplemented();
                break;
        }
    }
}
