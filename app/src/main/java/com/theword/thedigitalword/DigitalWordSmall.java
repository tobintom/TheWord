package com.theword.thedigitalword;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.text.HtmlCompat;

import com.theword.thedigitalword.notification.AlarmReceiver;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Formatter;

/**
 * Implementation of App Widget functionality.
 */
public class DigitalWordSmall extends AppWidgetProvider {

    SharedPreferences sharedPreferences = null;
    private int REQ_CODE = 2000;
    String texti = "";
    String booki = "";
    String diri = "ltr";
    String imageBook = "";
    String imageChapter = "";
    String time = "";
    String greeting = "";
    Bitmap bitmap = null;

    private long INTERVAL = 43200000;
    private long ALLOWED_INTERVAL = 1800000;


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferencesUtil.saveWidgetSmallServiceUpdate(sharedPreferences, context, 0);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_id), Context.MODE_PRIVATE);
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.digital_word_small);
            Handler handle = new Handler();
            ComponentName theWidget = new ComponentName(context, DigitalWordSmall.class);
            int[] widgetId = appWidgetManager.getAppWidgetIds(theWidget);
            //Update Date and Image
            //Set the Date Greeting and Image
            Formatter fmt = new Formatter();
            Calendar cal = Calendar.getInstance();
            fmt.format("%tA, %tB %td", cal, cal, cal);
            time = fmt.toString();
            int timeOfDay = cal.get(Calendar.HOUR_OF_DAY);

            //Update Date and Stuff
            Runnable runnabled = new Runnable() {
                @Override
                public void run() {
                    try {
                        //getDrawable
                        bitmap = decodeSampledBitmapFromResource(context.getResources(), Util.getTodayImage(timeOfDay, context), 150, 220);
                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                if (timeOfDay >= 0 && timeOfDay < 12) {
                                    greeting = "Good Morning";
                                    view.setInt(R.id.dwoutside_image, "setBackgroundColor", Color.rgb(229, 220, 210));
                                } else if (timeOfDay >= 12 && timeOfDay < 17) {
                                    greeting = "Good Afternoon";
                                    view.setInt(R.id.dwoutside_image, "setBackgroundColor", Color.rgb(255, 240, 189));
                                } else if (timeOfDay >= 17 && timeOfDay < 20) {
                                    greeting = "Good Evening";
                                    view.setInt(R.id.dwoutside_image, "setBackgroundColor", Color.rgb(229, 164, 198));
                                } else if (timeOfDay >= 20 && timeOfDay < 24) {
                                    greeting = "Good Evening";
                                    view.setInt(R.id.dwoutside_image, "setBackgroundColor", Color.rgb(188, 184, 206));
                                }
                                view.setTextViewText(R.id.dwtodayGreeting, greeting);
                                view.setTextViewText(R.id.dwtodaydate, time);
                                view.setImageViewBitmap(R.id.dwtodayimage, bitmap);
                                appWidgetManager.updateAppWidget(widgetId, view);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnabled).start();

            //Update Verse Text
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    long lastRefreshedTime = SharedPreferencesUtil.getWidgetSmallServiceUpdate(sharedPreferences, context);
                    //Check time and decide whether to run service
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    long interval = (currentTime - lastRefreshedTime);

                    if (interval >= INTERVAL  || interval <= ALLOWED_INTERVAL) {
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
                                    //Setting direction
                                    if (diri != null && diri.trim().equalsIgnoreCase("LTR")) {
                                        view.setTextViewText(R.id.dwslt, texti);
                                        view.setTextViewText(R.id.dwslb, booki);
                                        view.setInt(R.id.dwslt, "setVisibility", View.VISIBLE);
                                        view.setInt(R.id.dwslb, "setVisibility", View.VISIBLE);
                                        view.setInt(R.id.dwsltr, "setVisibility", View.INVISIBLE);
                                        view.setInt(R.id.dwslbr, "setVisibility", View.INVISIBLE);
                                    } else {
                                        view.setTextViewText(R.id.dwsltr, texti);
                                        view.setTextViewText(R.id.dwslbr, booki);
                                        view.setInt(R.id.dwsltr, "setVisibility", View.VISIBLE);
                                        view.setInt(R.id.dwslbr, "setVisibility", View.VISIBLE);
                                        view.setInt(R.id.dwslt, "setVisibility", View.INVISIBLE);
                                        view.setInt(R.id.dwslb, "setVisibility", View.INVISIBLE);
                                    }

                                    //Read Full Bible
                                    Intent readIntent = new Intent(context, HomeActivity.class);
                                    PendingIntent pendingReadIntent = PendingIntent.getActivity(context, 0,
                                            readIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    view.setOnClickPendingIntent(R.id.dwslr, pendingReadIntent);

                                    //Share content
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.setType("text/*");
                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The Digital Word Verse");
                                    String message = "<h1><b><span dir ='" + diri + "' >" + booki + "</span></b></h1>" +
                                            "<p dir='" + diri + "'> " + texti + "</p><BR>" +
                                            "<a href='https://thedigitalword.org'>https://thedigitalword.org</a>";
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 10,
                                            shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    view.setOnClickPendingIntent(R.id.dwsls, pendingIntent);
                                    appWidgetManager.updateAppWidget(widgetId, view);
                                    //Update last run time
                                    SharedPreferencesUtil.saveWidgetSmallServiceUpdate(sharedPreferences, context, Calendar.getInstance().getTimeInMillis());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            try {
                long lastRefreshedTime = SharedPreferencesUtil.getWidgetSmallServiceUpdate(sharedPreferences, context);
                //Check time and decide whether to run service
                long currentTime = Calendar.getInstance().getTimeInMillis();
                long interval = (currentTime - lastRefreshedTime);

                if (interval >= INTERVAL  || interval<=ALLOWED_INTERVAL) {
                    new Thread(runnable).start();
                }
            }catch(Exception e){
                //ignore
            }
        }catch (Exception e){
            //e.printStackTrace();
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static  int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //Update last run time
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_id), Context.MODE_PRIVATE);
        SharedPreferencesUtil.saveWidgetSmallServiceUpdate(sharedPreferences, context, 0);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        //Update last run time
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_id), Context.MODE_PRIVATE);
        SharedPreferencesUtil.saveWidgetSmallServiceUpdate(sharedPreferences, context, 0);

    }

    //    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        // There may be multiple widgets active, so update all of them
//        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(context, DigitalWordWidgetSmallService.class);
//
//        PendingIntent pendingIntent = PendingIntent.getService(context, REQ_CODE, i, PendingIntent.FLAG_NO_CREATE);
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
//            //cancel current and create a new one
//            manager.cancel(pendingIntent);
//            pendingIntent.cancel();
//            context.stopService(i);
//            //Set new
//            pendingIntent = PendingIntent.getService(context, REQ_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
//            manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), INTERVAL, pendingIntent);
//            i.putExtra("first","true");
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(i);
//            }else {
//                context.startService(i);
//            }
//        }
//
//    }
//
//    @Override
//    public void onEnabled(Context context) {
//        // Enter relevant functionality for when the first widget is created
//        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(context, DigitalWordWidgetSmallService.class);
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
//    public void onDeleted(Context context, int[] appWidgetIds) {
//        super.onDeleted(context, appWidgetIds);
//        Intent i = new Intent(context, DigitalWordWidgetSmallService.class);
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent    pendingIntent = PendingIntent.getService(context, REQ_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
//        if(pendingIntent!=null) {
//            am.cancel(pendingIntent);
//            pendingIntent.cancel();
//            context.stopService(i);
//        }
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        Intent i = new Intent(context, DigitalWordWidgetSmallService.class);
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent    pendingIntent = PendingIntent.getService(context, REQ_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
//        if(pendingIntent!=null) {
//            am.cancel(pendingIntent);
//            pendingIntent.cancel();
//            context.stopService(i);
//        }
//    }
}