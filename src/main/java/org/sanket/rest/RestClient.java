package org.sanket.rest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private final JSONPlaceholderAPI jsonPlaceholderAPI;

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        jsonPlaceholderAPI = retrofit.create(JSONPlaceholderAPI.class);
    }

    public JSONPlaceholderAPI getJsonPlaceholderAPI() {
        return jsonPlaceholderAPI;
    }
}
