package com.dbbest.a500px.net.retrofit;

import android.text.TextUtils;

import com.dbbest.a500px.BuildConfig;
import com.dbbest.a500px.net.model.ListPhotos;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class RestClient {

    private final RestService restService;

    public RestClient() {

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


    public Object getPhotos(String consumeKey, int page, int count) {
        return executeCall(restService.getPhotos(consumeKey, page, count));
    }

   private Object executeCall(Call<ListPhotos> call) {
        try {
            Response<ListPhotos> response = call.execute();
            if (!response.isSuccessful()) {
                Timber.w("Unsuccessful response code: %d message: %s", response.code(), response.message());

            }

            if (response.body() == null) {
                if (response.errorBody() == null) {
                    return null;
                } else {
                    String errorJson = response.errorBody().string();
                    if (TextUtils.isEmpty(errorJson)) {
                        return null;
                    } else {
                        Timber.w("Error Body: %s", errorJson);
                        try {

                            Timber.v("Error response normalized:");
                            return null;
                        } catch (Exception ex) {
                            Timber.e(ex, "Failed to parse error json body: %s", errorJson);
                            return null;
                        }
                    }
                }
            }

            return response.body();

        } catch (IOException e) {
            Timber.e(e, "Network error");
            return handleNetworkError(e);
        }
    }

    private Gson handleNetworkError(Throwable throwable) {
        if (throwable != null) {
            if (throwable instanceof SocketTimeoutException) {
                Timber.e("Socket Timeout");
                return null;
            } else if (throwable instanceof UnknownHostException) {
                Timber.e("Unable to resolve host");
                return null;
            } else if (throwable instanceof ConnectException) {
                Timber.e("Failed to connect to server");
                return null;
            }
        }

        Timber.e("An unknown exception happened");
        return null;
    }

}

