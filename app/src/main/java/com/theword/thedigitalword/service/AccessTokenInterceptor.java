package com.theword.thedigitalword.service;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.theword.thedigitalword.R;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessTokenInterceptor implements Interceptor {
    String accessToken= "";
    public AccessTokenInterceptor(SharedPreferences sp, Context context){
        accessToken = SharedPreferencesUtil.getToken(sp,context);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = newRequestWithAccessToken(chain.request(), accessToken);
        return chain.proceed(request);
    }

    @NonNull
    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken) {
        return request.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }
}
