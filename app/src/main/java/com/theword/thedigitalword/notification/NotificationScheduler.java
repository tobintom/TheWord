package com.theword.thedigitalword.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.theword.thedigitalword.R;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationScheduler {

    public static final int DAILY_REMINDER_REQUEST_CODE=1001;
    public static final int DAILY_REMINDER_REQUEST_CODE_ALRM=1002;
    public static final String TAG="NotificationScheduler";

    public static void setReminder(Context context){

        int hour = Integer.parseInt(context.getResources().getString(R.string.push_hour));
        int min = Integer.parseInt(context.getResources().getString(R.string.push_min));
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        // cancel already scheduled reminders
        cancelReminder(context);

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void setReminderIfNotSet(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_NO_CREATE);
        if(pendingIntent==null) {
           setReminder(context);
        }
    }

    public static void cancelReminder(Context context){
        // Disable a receiver
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_NO_CREATE);
        if(pendingIntent!=null) {
            am.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    public static void setRemReminder(Context context){
        String hour = SharedPreferencesUtil.getRemHour(context.getSharedPreferences(context.getResources().getString(R.string.app_id), Context.MODE_PRIVATE),context);
        String min = SharedPreferencesUtil.getRemMin(context.getSharedPreferences(context.getResources().getString(R.string.app_id), Context.MODE_PRIVATE),context);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        setcalendar.set(Calendar.MINUTE, Integer.parseInt(min));
        setcalendar.set(Calendar.SECOND, 0);
        setcalendar.set(Calendar.MILLISECOND, 0);
        // cancel already scheduled reminders
        cancelRemReminder(context);

        if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1);

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, ReminderReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE_ALRM, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void setRemReminderIfNotSet(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ComponentName receiver = new ComponentName(context, ReminderReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE_ALRM, intent1, PendingIntent.FLAG_NO_CREATE);
        if(pendingIntent==null) {
            setRemReminder(context);
        }
    }

    public static void cancelRemReminder(Context context){
        // Disable a receiver
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ComponentName receiver = new ComponentName(context, ReminderReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE_ALRM, intent1, PendingIntent.FLAG_NO_CREATE);
        if(pendingIntent!=null) {
            am.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    public static void showNotification(Context context, Class<?> cls, String title, String content)
    {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String vodChannelID = context.getResources().getString(R.string.push_channel_ID);
        String vodChannelName = context.getResources().getString(R.string.push_channel_name);
        String vodChannel = "The Digital Word Verse Of The Day";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(vodChannelID, vodChannelName, importance);
            channel.setDescription(vodChannel);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,vodChannelID);

        Notification notification = builder
                .setContentText(content)
                .setContentTitle("The Digital Word")
                .setContentInfo("Daily Verse")
                .setAutoCancel(true)
                .setSubText(title)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.bible)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);
    }

    public static void showRemNotification(Context context, Class<?> cls){
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String vodChannelID = context.getResources().getString(R.string.reminder_channel_ID);
        String vodChannelName = context.getResources().getString(R.string.reminder_channel_name);
        String vodChannel = "The Digital Word Reminder";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(vodChannelID, vodChannelName, importance);
            channel.setDescription(vodChannel);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE_ALRM, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,vodChannelID);

        Notification notification = builder
                .setContentText("Daily Reminder To Read The Bible")
                .setContentTitle("The Digital Word")
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.bible)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE_ALRM, notification);
    }
}
