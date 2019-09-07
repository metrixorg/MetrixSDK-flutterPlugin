package ir.metrix.metrix_example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.metrix.sdk.MetrixReferrerReceiver;

public class InstallReceiver extends BroadcastReceiver {
@Override
public void onReceive(Context context, Intent intent) {
    // Metrix
    new MetrixReferrerReceiver().onReceive(context, intent);

    // Google Analytics
//    new CampaignTrackingReceiver().onReceive(context, intent);
    }
}