package com.theword.thedigitalword;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import org.json.JSONObject;

import java.util.Calendar;

public class DigitalWordWidgetService extends Service {

    String texti = "";
    String booki = "";
    String diri = "ltr";
    String imageBook = "";
    String imageChapter = "";
    private long INTERVAL = 72000000;
    private long ALLOWED_INTERVAL = 1800000;

    public DigitalWordWidgetService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null) {
            String firstTime = (intent != null) ? intent.getStringExtra("first") : null;
            boolean isFirst = (firstTime != null && firstTime.trim().equalsIgnoreCase("TRUE")) ? true : false;

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.app_id), Context.MODE_PRIVATE);
            RemoteViews view = new RemoteViews(getPackageName(), R.layout.digital_word_large);
            Handler handle = new Handler();

            ComponentName theWidget = new ComponentName(this, DigitalWordLarge.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                        long lastRefreshedTime = SharedPreferencesUtil.getWidgetServiceUpdate(sharedPreferences, getApplicationContext());
                        //Check time and decide whether to run service
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        long interval = (currentTime - lastRefreshedTime);

                        if (interval >= INTERVAL || isFirst || interval <= ALLOWED_INTERVAL) {
                            try {
                                JSONObject jObjecti = null;
                                String jsonData = TheWordContentService.getRandomDailyVerse(sharedPreferences, getApplicationContext());
                                jObjecti = new JSONObject(jsonData);
                                diri = jObjecti.getString("dir");
                                JSONObject o = (JSONObject) jObjecti.getJSONArray("passages").get(0);
                                booki = o.getString("name");
                                imageBook = o.getString("book");
                                //get rest
                                JSONObject p = (JSONObject) o.getJSONArray("content").get(0);
                                imageChapter = p.getString("chapter");
                                booki = booki + " " + p.getString("chapter");
                                JSONObject t = (JSONObject) p.getJSONArray("verses").get(0);
                                booki = booki + ":" + t.getString("verse");
                                texti = t.getString("text") + "\n";
                                handle.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            //Setting direction
                                            if (diri != null && diri.trim().equalsIgnoreCase("LTR")) {
                                                view.setTextViewText(R.id.dwllt, texti);
                                                view.setTextViewText(R.id.dwllb, booki);
                                                view.setInt(R.id.dwllt, "setVisibility", View.VISIBLE);
                                                view.setInt(R.id.dwllb, "setVisibility", View.VISIBLE);
                                                view.setInt(R.id.dwlltr, "setVisibility", View.INVISIBLE);
                                                view.setInt(R.id.dwllbr, "setVisibility", View.INVISIBLE);
                                            } else {
                                                view.setTextViewText(R.id.dwlltr, texti);
                                                view.setTextViewText(R.id.dwllbr, booki);
                                                view.setInt(R.id.dwllt, "setVisibility", View.INVISIBLE);
                                                view.setInt(R.id.dwllb, "setVisibility", View.INVISIBLE);
                                                view.setInt(R.id.dwlltr, "setVisibility", View.VISIBLE);
                                                view.setInt(R.id.dwllbr, "setVisibility", View.VISIBLE);
                                            }

                                            //Read Full Bible
                                            Intent readIntent = new Intent(getApplicationContext(), HomeActivity.class);
                                            PendingIntent pendingReadIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                                                    readIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            view.setOnClickPendingIntent(R.id.dwllr, pendingReadIntent);

                                            //Share content
                                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                            shareIntent.setType("text/*");
                                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The Digital Word Verse");
                                            String message = "<h1><b><span dir ='" + diri + "' >" + booki + "</span></b></h1>" +
                                                    "<p dir='" + diri + "'> " + texti + "</p><BR>" +
                                                    "<a href='https://thedigitalword.org'>https://thedigitalword.org</a>";
                                            shareIntent.putExtra(Intent.EXTRA_TEXT, HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                                                    shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            view.setOnClickPendingIntent(R.id.dwlls, pendingIntent);
                                            manager.updateAppWidget(theWidget, view);
                                            //Update last run time
                                            SharedPreferencesUtil.saveWidgetServiceUpdate(sharedPreferences, getApplicationContext(), Calendar.getInstance().getTimeInMillis());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            };
            try {
                long lastRefreshedTime = SharedPreferencesUtil.getWidgetServiceUpdate(sharedPreferences, getApplicationContext());
                //Check time and decide whether to run service
                long currentTime = Calendar.getInstance().getTimeInMillis();
                long interval = (currentTime - lastRefreshedTime);

                if (interval >= INTERVAL || isFirst || interval <= ALLOWED_INTERVAL) {
                    new Thread(runnable).start();
                }
            }catch(Exception e){
                //ignore
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}