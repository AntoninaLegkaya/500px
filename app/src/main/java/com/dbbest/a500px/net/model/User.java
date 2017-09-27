package com.dbbest.a500px.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("usertype")
    @Expose
    private Integer usertype;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("userpic_url")
    @Expose
    private String userpicUrl;
    @SerializedName("userpic_https_url")
    @Expose
    private String userpicHttpsUrl;
    @SerializedName("cover_url")
    @Expose
    private String coverUrl;
    @SerializedName("upgrade_status")
    @Expose
    private Integer upgradeStatus;
    @SerializedName("store_on")
    @Expose
    private Boolean storeOn;
    @SerializedName("affection")
    @Expose
    private Integer affection;
    @SerializedName("avatars")
    @Expose
    private Avatars avatars;
    @SerializedName("following")
    @Expose
    private Boolean following;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserpicUrl() {
        return userpicUrl;
    }

    public void setUserpicUrl(String userpicUrl) {
        this.userpicUrl = userpicUrl;
    }

    public String getUserpicHttpsUrl() {
        return userpicHttpsUrl;
    }

    public void setUserpicHttpsUrl(String userpicHttpsUrl) {
        this.userpicHttpsUrl = userpicHttpsUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Integer getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public Boolean getStoreOn() {
        return storeOn;
    }

    public void setStoreOn(Boolean storeOn) {
        this.storeOn = storeOn;
    }

    public Integer getAffection() {
        return affection;
    }

    public void setAffection(Integer affection) {
        this.affection = affection;
    }

    public Avatars getAvatars() {
        return avatars;
    }

    public void setAvatars(Avatars avatars) {
        this.avatars = avatars;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

}
