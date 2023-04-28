package org.sanket.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClient {

    OkHttpClient okHttpClient;

    public HttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        this.okHttpClient = builder.build();
    }

    public ResponseBody get(String url) {
        Request request = new Request.Builder()
                .url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
