package com.dbbest.a500px.net.retrofit;

import com.dbbest.a500px.net.model.ListPhotos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestService {

    @GET("/v1/photos")
    Call<ListPhotos> getPhotos(@Query("consumer_key") String key, @Query("page") int page, @Query("rpp") int limit, @Query("image_size") int size);
}
