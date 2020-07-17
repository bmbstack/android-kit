package com.bmbstack.kit.api;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * https://github.com/square/okhttp/wiki/Recipes#handling-authentication
 */
public class JWTAuthenticator implements Authenticator {
    private static final String TAG = JWTAuthenticator.class.getSimpleName();

    private JWT jwt;

    public JWTAuthenticator(JWT jwt) {
        this.jwt = jwt;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        LogUtils.dTag(TAG, "OkHttp", "Authenticating for response: " + response);
        LogUtils.dTag(TAG, "OkHttp", "Challenges: " + response.challenges());

        String refreshJWT = jwt.refresh();
        LogUtils.dTag(TAG, "OkHttp", "refreshJWT=" + refreshJWT);
        if (refreshJWT != null && refreshJWT.trim().length() > 0) {

            if (JWTHelper.getAuthorizationVal(refreshJWT).equals(response.request().header(JWTHelper.AUTHORIZATION))) {
                return null;  // If we already failed with these jwt, don't retry.
            }

            return response.request()
                    .newBuilder()
                    .header(JWTHelper.AUTHORIZATION, JWTHelper.getAuthorizationVal(refreshJWT))
                    .build();
        } else {
            return null;
        }
    }
}