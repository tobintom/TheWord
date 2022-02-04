package com.theword.thedigitalword.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.service.TheWordContentService;

import org.json.JSONObject;

public class ReminderReceiver extends BroadcastReceiver {

    String TAG = "ReminderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                NotificationScheduler.setRemReminder(context);
                return;
            }
        }

        try {

            //Trigger the notification
            NotificationScheduler.showRemNotification(context, HomeActivity.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}