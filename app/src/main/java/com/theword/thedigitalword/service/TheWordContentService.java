package com.theword.thedigitalword.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.theword.thedigitalword.R;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TheWordContentService {

    public static String getRandomDailyVerse(SharedPreferences sp, Context c)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.word_api)+"/"+
                SharedPreferencesUtil.getLanguage(sp,c) +"/randomDailyVerse").newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String getDailyVerse(SharedPreferences sp, Context c)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.word_api)+"/"+
                SharedPreferencesUtil.getLanguage(sp,c) +"/dailyverse").newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String getChapter(SharedPreferences sp, Context c, String book, String chapter)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.word_api)+"/"+
                SharedPreferencesUtil.getLanguage(sp,c) +"/"+book+"/"+chapter).newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String getNextChapter(SharedPreferences sp, Context c, String book, String chapter)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.word_api)+"/"+
                SharedPreferencesUtil.getLanguage(sp,c) +"/nextChapter?bookId="+book+"&chapter="+chapter).newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String getPreviousChapter(SharedPreferences sp, Context c, String book, String chapter)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.word_api)+"/"+
                SharedPreferencesUtil.getLanguage(sp,c) +"/previousChapter?bookId="+book+"&chapter="+chapter).newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String getCrossReferences(SharedPreferences sp, Context c, String book, String chapter,String verse)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        String verseUrl = book+" "+chapter+":"+verse;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.word_api)+"/"+
                SharedPreferencesUtil.getLanguage(sp,c) +"/crossreference?verse="+verseUrl).newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String getPassages(SharedPreferences sp, Context c, String passageText)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.word_api)+"/"+
                SharedPreferencesUtil.getLanguage(sp,c) +"/passage?passage="+passageText).newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String getSearch(SharedPreferences sp, Context c, String searchText)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.word_api)+"/"+
                SharedPreferencesUtil.getLanguage(sp,c) +"/search?key="+searchText+"&limit=49").newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}
