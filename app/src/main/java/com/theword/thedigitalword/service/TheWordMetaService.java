package com.theword.thedigitalword.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.theword.thedigitalword.R;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TheWordMetaService {

    public static String getLanguages(SharedPreferences sp, Context c)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.meta_api)+"/list").newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String getBooks(SharedPreferences sp, Context c)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.meta_api)+"/"
        + SharedPreferencesUtil.getLanguage(sp,c) + "/books").newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String getChapters(SharedPreferences sp, Context c,String book)throws Exception{
        OkHttpClient client = Service.getServiceClient().newBuilder()
                .authenticator(new Authenticator(sp,c))
                .addInterceptor(new AccessTokenInterceptor(sp,c)).build();
        //Build URL
        book = (book==null)?SharedPreferencesUtil.getBook(sp,c):book;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(c.getString(R.string.meta_api)+"/"
                + SharedPreferencesUtil.getLanguage(sp,c) + "/"
                + book +"/chapters").newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
