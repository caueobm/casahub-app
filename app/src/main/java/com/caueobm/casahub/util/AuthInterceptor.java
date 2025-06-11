package com.caueobm.casahub.util;

import android.content.Context;

import com.caueobm.casahub.util.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private TokenManager tokenManager;

    public AuthInterceptor(Context context) {
        tokenManager = new TokenManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String token = tokenManager.getAuthToken();

        if (token != null) {
            Request requestWithToken = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(requestWithToken);
        }

        return chain.proceed(original);
    }
}