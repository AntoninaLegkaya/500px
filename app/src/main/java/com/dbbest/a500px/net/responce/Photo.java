package com.dbbest.a500px.net.responce;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = "UWF_NULL_FIELD")
public class Photo {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("name")
    private String name;

    @SerializedName("images")
    private List<Image> images;

    @SerializedName("user")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return images;
    }

    public User getUser() {
        return user;
    }

}
