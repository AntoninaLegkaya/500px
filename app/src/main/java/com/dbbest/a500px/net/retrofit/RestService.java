package com.dbbest.a500px.net.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestService {

    @GET("/v1/photos")
    Call<Object> getPhotos(@Query("consumer_key") String key, @Query("page") int page, @Query("rpp") int limit);
}
