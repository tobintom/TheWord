package com.theword.thedigitalword.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.theword.thedigitalword.R;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TokenService {

    public static String getAccessToken(SharedPreferences sharedPreferences, Context context){
        String accessToken = null;
        String refreshToken = null;
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(context.getString(R.string.token_auth_url)).newBuilder();
            urlBuilder.addQueryParameter("grant_type", "password");
            urlBuilder.addQueryParameter("username", context.getString(R.string.oauth_id));
            urlBuilder.addQueryParameter("password", context.getString(R.string.oauth_password));
            String credential = Credentials.basic(context.getString(R.string.oauth_client), context.getString(R.string.oauth_secret));
            RequestBody body = RequestBody.create(null, new byte[]{});
            Request request = new Request.Builder()
                    .header("Authorization", credential)
                    .url(urlBuilder.build().toString())
                    .post(body)
                    .build();
            Response response = Service.getServiceClient().newCall(request).execute();
            String jsonData = response.body().string();
            JSONObject jObject = new JSONObject(jsonData);
            accessToken = jObject.getString("access_token");
            refreshToken = jObject.getString("refresh_token");
            //Set in Shared Pref
            SharedPreferencesUtil.setToken(sharedPreferences,context,accessToken);
            SharedPreferencesUtil.setRefreshToken(sharedPreferences,context,refreshToken);
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return accessToken;

    }
}
