package com.theword.thedigitalword.notification;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;

import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.Util;

import org.json.JSONObject;

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";
    String booki = "";
    String diri = "";
    String texti = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                NotificationScheduler.setReminder(context);
                return;
            }
        }
       SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_id), Context.MODE_PRIVATE);
        Handler handle = new Handler();
        try {
            Runnable runnabled = new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            JSONObject jObjecti = null;
                            String jsonData = TheWordContentService.getRandomDailyVerse(sharedPreferences, context);
                            if (jsonData != null && jsonData.trim().length() > 0) {
                                jObjecti = new JSONObject(jsonData);
                                diri = jObjecti.getString("dir");
                                JSONObject o = (JSONObject) jObjecti.getJSONArray("passages").get(0);
                                booki = o.getString("name");
                                //get rest
                                JSONObject p = (JSONObject) o.getJSONArray("content").get(0);
                                booki = booki + " " + p.getString("chapter");
                                JSONObject t = (JSONObject) p.getJSONArray("verses").get(0);
                                booki = booki + ":" + t.getString("verse");
                                texti = t.getString("text") + "\n";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                           handle.post(new Runnable() {
                            @Override
                            public void run() {
                                //Trigger the notification
                                NotificationScheduler.showNotification(context, HomeActivity.class,
                                        booki, texti);
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnabled).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}