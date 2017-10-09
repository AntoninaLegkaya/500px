package com.dbbest.a500px.net.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListPhotos {

    @SerializedName("photos")
    @Expose
    private List<Photo> photos;

    public List<Photo> getPhotos() {
        return photos;
    }
}
