package com.dbbest.a500px.net.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("https_url")
    @Expose
    private String httpsUrl;

    public String getHttpsUrl() {
        return httpsUrl;
    }


}
