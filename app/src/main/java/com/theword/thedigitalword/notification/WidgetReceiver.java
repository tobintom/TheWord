package com.theword.thedigitalword.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import org.json.JSONObject;

public class WidgetReceiver extends BroadcastReceiver {

    String TAG = "WidgetReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_id), Context.MODE_PRIVATE);
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                SharedPreferencesUtil.saveWidgetSmallServiceUpdate(sharedPreferences, context, 0);
                return;
            }
        }
    }
}