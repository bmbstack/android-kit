package com.bmbstack.kit.util;

import com.bmbstack.kit.api.JWT;
import com.bmbstack.kit.api.JWTHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class WithTokenRequestInterceptor implements Interceptor {
    private JWT jwt;

    public WithTokenRequestInterceptor(JWT jwt) {
        this.jwt = jwt;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        // JWT :Authorization: Bearer tokenValue
        if (jwt != null && jwt.available()) {
            builder.header(JWTHelper.AUTHORIZATION, JWTHelper.getAuthorizationVal(jwt.get()));
        }
        return chain.proceed(builder.build());
    }
}
