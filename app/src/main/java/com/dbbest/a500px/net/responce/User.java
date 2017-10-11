package com.dbbest.a500px.net.responce;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private int id;

    @SerializedName("fullname")
    private String fullname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullname;
    }

}
