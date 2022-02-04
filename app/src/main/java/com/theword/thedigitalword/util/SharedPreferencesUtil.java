package com.theword.thedigitalword.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.theword.thedigitalword.R;

public class SharedPreferencesUtil {

    public static String getLanguage(SharedPreferences pref, Context context){
        String lang = pref.getString(context.getString(R.string.language),null);
        if(lang==null || lang.trim().length()==0){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(context.getString(R.string.language), "ENG");
            editor.apply();
            return "ENG";
        }else
            return lang;
    }
    public static String getDirection(SharedPreferences pref, Context context){
        String direction = pref.getString(context.getString(R.string.direction),null);
        if(direction==null || direction.trim().length()==0){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(context.getString(R.string.direction), "LTR");
            editor.apply();
            return "LTR";
        }else
            return direction;
    }


    public static String getBook(SharedPreferences pref, Context context){
        String book = pref.getString(context.getString(R.string.book),null);
        if(book==null || book.trim().length()==0){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(context.getString(R.string.book), "01");
            editor.apply();
            return "01";
        }else
            return book;
    }

    public static String getChapter(SharedPreferences pref, Context context){
        String chap = pref.getString(context.getString(R.string.chapter),null);
        if(chap==null || chap.trim().length()==0){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(context.getString(R.string.chapter), "1");
            editor.apply();
            return "1";
        }else
            return chap;
    }

    public static String getToken(SharedPreferences pref, Context context){
        return pref.getString(context.getString(R.string.token),null);
    }

    public static String getRefreshToken(SharedPreferences pref, Context context){
        return pref.getString(context.getString(R.string.refresh_token),null);
    }

    public static void setLanguage(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.language), val);
        editor.apply();
    }

    public static void setDirection(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.direction), val);
        editor.apply();
    }

    public static void setBook(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.book), val);
        editor.apply();
    }

    public static void setChapter(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.chapter), val);
        editor.apply();
    }

    public static void setToken(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.token), val);
        editor.apply();
    }

    public static void setRefreshToken(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.refresh_token), val);
        editor.apply();
    }

    public static void saveIntroSeen(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.intro_seen), val);
        editor.apply();
    }

    public static String getIntroSeen(SharedPreferences pref, Context context) {
        String lang = pref.getString(context.getString(R.string.intro_seen), "false");
        if(lang==null || lang.trim().length()==0){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(context.getString(R.string.intro_seen), "false");
            editor.apply();
            return "false";
        }
        return lang;
    }

    public static void saveNotification(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.push_notification), val);
        editor.apply();
    }

    public static String getPushNotification(SharedPreferences pref, Context context) {
        String nt = pref.getString(context.getString(R.string.push_notification), null);
        return nt;
    }
    public static int getTheme(SharedPreferences pref, Context context) {
        int theme = pref.getInt(context.getString(R.string.theme), 0);
        if(theme==0){
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(context.getString(R.string.theme), R.style.Theme_TheDigitalWord_RED);
            editor.apply();
            return R.style.Theme_TheDigitalWord_RED;
        }
        return theme;
    }

    public static void saveTheme(SharedPreferences pref , Context context,int theme){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(context.getString(R.string.theme), theme);
        editor.apply();
    }

    public static int getFontSize(SharedPreferences pref, Context context) {
        int font = pref.getInt(context.getString(R.string.font_size), 0);
        if(font==0){
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(context.getString(R.string.font_size), 20);
            editor.apply();
            return 20;
        }
        return font;
    }

    public static void saveFontSize(SharedPreferences pref , Context context,int font){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(context.getString(R.string.font_size), font);
        editor.apply();
    }

    public static void saveRemNotification(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.rem_notification), val);
        editor.apply();
    }

    public static String getRemNotification(SharedPreferences pref, Context context) {
        String nt = pref.getString(context.getString(R.string.rem_notification), null);
        return nt;
    }

    public static void saveDarkMode(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.dark), val);
        editor.apply();
    }

    public static String getDarkMode(SharedPreferences pref, Context context) {
        String nt = pref.getString(context.getString(R.string.dark), null);
        return nt;
    }

    public static void saveRemHour(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.rem_hour), val);
        editor.apply();
    }

    public static String getRemHour(SharedPreferences pref, Context context) {
        String nt = pref.getString(context.getString(R.string.rem_hour), "20");
        return nt;
    }

    public static void saveRemMin(SharedPreferences pref, Context context, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.rem_min), val);
        editor.apply();
    }

    public static String getRemMin(SharedPreferences pref, Context context) {
        String nt = pref.getString(context.getString(R.string.rem_min ), "0");
        return nt;
    }

    public static void saveWidgetServiceUpdate(SharedPreferences pref, Context context, long val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(context.getString(R.string.widget_service), val);
        editor.apply();
    }

    public static long getWidgetServiceUpdate(SharedPreferences pref, Context context) {
        long nt = pref.getLong(context.getString(R.string.widget_service ), 0);
        return nt;
    }

    public static void saveWidgetSmallServiceUpdate(SharedPreferences pref, Context context, long val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(context.getString(R.string.widget_small_service), val);
        editor.apply();
    }

    public static long getWidgetSmallServiceUpdate(SharedPreferences pref, Context context) {
        long nt = pref.getLong(context.getString(R.string.widget_small_service ), 0);
        return nt;
    }
}
