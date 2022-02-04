package com.theword.thedigitalword;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStats;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.text.HtmlCompat;

import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class DigitalWordLarge extends AppWidgetProvider {

    String texti = "";
    String booki = "";
    String diri = "ltr";
    String imageBook = "";
    String imageChapter = "";
    SharedPreferences sharedPreferences = null;
    private int REQ_CODE = 2001;
    private long INTERVAL = 86400000;

    @Override
    public void onReceive(Context context, Intent intent){
        // Chain up to the super class so the onEnabled, etc callbacks get dispatched
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        try {
            ComponentName theWidget = new ComponentName(context, DigitalWordLarge.class);
            int[] widgetId = appWidgetManager.getAppWidgetIds(theWidget);
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_id), Context.MODE_PRIVATE);
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.digital_word_large);
            Handler handle = new Handler();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jObjecti = null;
                        String jsonData = TheWordContentService.getRandomDailyVerse(sharedPreferences, context);
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
                                    Intent readIntent = new Intent(context, HomeActivity.class);
                                    PendingIntent pendingReadIntent = PendingIntent.getActivity(context, 0,
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
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                                            shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    view.setOnClickPendingIntent(R.id.dwlls, pendingIntent);
                                    appWidgetManager.updateAppWidget(widgetId, view);
                                    //Update last run time
                                    SharedPreferencesUtil.saveWidgetServiceUpdate(sharedPreferences, context, Calendar.getInstance().getTimeInMillis());
                                } catch (Exception e) {
                                    //e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception e) {
                       // e.printStackTrace();
                    }
                }
            };
        try {
            new Thread(runnable).start();
        } catch (Exception e) {
           // e.printStackTrace();
        }
    }catch(Exception e){
       // e.printStackTrace();
    }
    }



    //    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        // There may be multiple widgets active, so update all of them
//        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(context, DigitalWordWidgetService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(context, REQ_CODE, i, PendingIntent.FLAG_NO_CREATE);
//
//        if(pendingIntent==null) {
//            context.stopService(i);
//            pendingIntent = PendingIntent.getService(context, REQ_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
//            manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), INTERVAL, pendingIntent);
//            i.putExtra("first","true");
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(i);
//            }else {
//                context.startService(i);
//            }
//        }else{
//            manager.cancel(pendingIntent);
//            pendingIntent.cancel();
//            context.stopService(i);
//            pendingIntent = PendingIntent.getService(context, REQ_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
//            manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), INTERVAL, pendingIntent);
//            i.putExtra("first","true");
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(i);
//            }else {
//                context.startService(i);
//            }
//        }
//    }
//
//    @Override
//    public void onEnabled(Context context) {
//        // Enter relevant functionality for when the first widget is created
//        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(context, DigitalWordWidgetService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(context, REQ_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
//        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), INTERVAL, pendingIntent);
//        i.putExtra("first","true");
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(i);
//        }else {
//            context.startService(i);
//        }
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        // Enter relevant functionality for when the last widget is disabled
//        Intent i = new Intent(context, DigitalWordWidgetService.class);
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent    pendingIntent = PendingIntent.getService(context, REQ_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
//        if(pendingIntent!=null) {
//            am.cancel(pendingIntent);
//            pendingIntent.cancel();
//            context.stopService(i);
//        }
//    }
}