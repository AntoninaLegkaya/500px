package com.dbbest.a500px.net.retrofit;

import com.dbbest.a500px.BuildConfig;
import com.dbbest.a500px.net.responce.ListPhotos;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class RestClient {

    private static RestClient instance;
    private final RestService restService;

    private RestClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addNetworkInterceptor(httpLoggingInterceptor);
        builder.addNetworkInterceptor(new StethoInterceptor());
        OkHttpClient okHttpClient = builder.build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_SERVER_ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.restService = retrofit.create(RestService.class);

    }

    public static RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public ListPhotos getPhotos(String consumeKey, int page, int count, int size) {
//        (restService.getPhotos(consumeKey, page, count, size)).enqueue(new Callback<ListPhotos>() {
//            @Override
//            public void onResponse(Call<ListPhotos> call, Response<ListPhotos> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ListPhotos> call, Throwable t) {
//
//            }
//        });
        try {
            Response<ListPhotos> response = (restService.getPhotos(consumeKey, page, count, size)).execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
            Timber.e("Error call REST %s", e.getMessage());
        }

        return null;
    }

}

