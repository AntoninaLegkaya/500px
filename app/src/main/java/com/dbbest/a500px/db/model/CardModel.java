package com.dbbest.a500px.db.model;

public class CardModel {
    public String nameUser;
    public String avatarUrl;
    public String imageUrl;
    public AvatarsModel avatars;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAvatars(AvatarsModel avatars) {
        this.avatars = avatars;
    }

    public CardModel(String nameUser, String avatarUrl, String imageUrl, AvatarsModel avatars) {

        this.nameUser = nameUser;
        this.avatarUrl = avatarUrl;
        this.imageUrl = imageUrl;

        this.avatars=avatars;
    }

    public CardModel() {
        //nothing todo
    }

    public AvatarsModel getAvatars() {
        return avatars;
    }

    public String getNameUser() {

        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
