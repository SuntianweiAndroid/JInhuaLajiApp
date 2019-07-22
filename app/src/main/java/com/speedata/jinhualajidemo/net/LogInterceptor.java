package com.speedata.jinhualajidemo.net;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * 功能描述:日志
 */
public class LogInterceptor implements Interceptor {
    @SuppressWarnings("ConstantConditions")
    @Override
    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        okhttp3.Response response = chain.proceed(chain.request());
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}

