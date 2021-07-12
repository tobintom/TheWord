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
}
