package com.bmbstack.kit.util;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.zhouyou.http.model.HttpHeaders;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonHeaderInterceptor implements Interceptor {
    private static final String TAG = "API";

    private HttpHeaders headers;

    public CommonHeaderInterceptor(HttpHeaders headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        Request request = builder.build();
        LogUtils.iTag(TAG, headersToString(headers) + request.headers().toString());
        return chain.proceed(request);
    }

    private String headersToString(HttpHeaders headers) {
        StringBuilder result = new StringBuilder();
        if (ObjectUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.headersMap.entrySet()) {
                result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        return result.toString();
    }
}
