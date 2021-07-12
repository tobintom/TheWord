package com.theword.thedigitalword.service;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class Authenticator implements okhttp3.Authenticator {
    SharedPreferences sp = null;
    Context context = null;
    public Authenticator(SharedPreferences sps, Context contexts){
        sp = sps;
        context = contexts;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
        String updatedAccessToken = TokenService.getAccessToken(sp,context);
        return newRequestWithAccessToken(response.request(), updatedAccessToken);
    }

    @NonNull
    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken) {
        return request.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }
}
