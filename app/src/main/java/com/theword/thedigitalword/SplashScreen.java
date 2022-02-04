package com.theword.thedigitalword;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import com.theword.thedigitalword.notification.NotificationScheduler;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONObject;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3500;
    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    String dir = "ltr";
    JSONObject jObject = null;
    String text = "";
    String book = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Util.checkConnection(getApplicationContext())) {

            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
            Context context = this.getApplicationContext();
            setContentView(R.layout.activity_splash);
            ImageView view = (ImageView) findViewById(R.id.splash_image);
            TextView tv = (TextView) findViewById(R.id.splash_text);
            TextView bv = (TextView) findViewById(R.id.splash_book);

            //Check if the DEVICE IS ON DARK Mode
            int nightModeFlags =
                    this.getApplicationContext().getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                    break;

                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    break;
            }

            if (nightModeFlags != Configuration.UI_MODE_NIGHT_YES) {
                //Look for Dark Mode
                boolean dark = (SharedPreferencesUtil.getDarkMode(sharedPreferences, SplashScreen.this) != null
                        && SharedPreferencesUtil.getDarkMode(sharedPreferences, SplashScreen.this).trim()
                        .equalsIgnoreCase("TRUE")) ? true : false;
                if (dark) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }

            //Trigger the scheduler if it is not setup
            String pushNotification = SharedPreferencesUtil.getPushNotification(sharedPreferences, this);
            if (pushNotification == null) {
                NotificationScheduler.setReminder(getApplicationContext());
                SharedPreferencesUtil.saveNotification(sharedPreferences, this, "true");
            } else if (pushNotification != null && pushNotification.trim().equalsIgnoreCase("TRUE")) {
                NotificationScheduler.setReminderIfNotSet(getApplicationContext());
            }

            //Trigger the Reminder if it is not setup
            String remNotification = SharedPreferencesUtil.getRemNotification(sharedPreferences, this);
            if (remNotification == null) {
                NotificationScheduler.setRemReminder(getApplicationContext());
                SharedPreferencesUtil.saveRemNotification(sharedPreferences, this, "true");
            } else if (remNotification != null && remNotification.trim().equalsIgnoreCase("TRUE")) {
                NotificationScheduler.setRemReminderIfNotSet(getApplicationContext());
            }

            new Thread() {
                @Override
                public void run() {

                    try {
                        String jsonData = TheWordContentService.getRandomDailyVerse(sharedPreferences, context);
                        if (jsonData != null && jsonData.trim().length() > 0) {
                            jObject = new JSONObject(jsonData);
                            dir = jObject.getString("dir");
                            JSONObject o = (JSONObject) jObject.getJSONArray("passages").get(0);
                            book = o.getString("name");
                            //get rest
                            JSONObject p = (JSONObject) o.getJSONArray("content").get(0);
                            book = book + " " + p.getString("chapter");
                            JSONObject t = (JSONObject) p.getJSONArray("verses").get(0);
                            book = book + ":" + t.getString("verse");
                            text = t.getString("text") + "\n";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setImageResource(Util.getRandomSharedResource(getApplicationContext()));
                            tv.setTextDirection(dir.equalsIgnoreCase("LTR") ? TextView.TEXT_DIRECTION_LTR : TextView.TEXT_DIRECTION_RTL);

                            bv.setTextDirection(dir.equalsIgnoreCase("LTR") ? TextView.TEXT_DIRECTION_LTR : TextView.TEXT_DIRECTION_RTL);
                            tv.setText(text);
                            bv.setText(book);
                        }
                    });
                }

            }.start();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Run after splash screen is done
                    //Check if first time user and launch intro
                    boolean isIntroSeen = Boolean.valueOf(SharedPreferencesUtil.getIntroSeen(sharedPreferences, getApplicationContext()));
                    if (!isIntroSeen) {
                        Intent introIntent = new Intent(getApplicationContext(), OnboardActivity.class);
                        introIntent.putExtra(PREF_USER_FIRST_TIME, isIntroSeen);
                        startActivity(introIntent);
                        finish();
                    } else {
                        Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        }else{
            Util.onFatalException(getApplicationContext());
        }
    }
}


