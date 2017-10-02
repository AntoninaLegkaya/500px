package com.dbbest.a500px.db.model;

public class CardModel {
    public String nameUser;
    public String imageUrl;
    public AvatarsModel avatars;

    public CardModel(String nameUser, String imageUrl, AvatarsModel avatars) {

        this.nameUser = nameUser;
        this.imageUrl = imageUrl;

        this.avatars = avatars;
    }

    public CardModel() {
        //nothing todo
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public AvatarsModel getAvatars() {
        return avatars;
    }

    public void setAvatars(AvatarsModel avatars) {
        this.avatars = avatars;
    }

    public String getNameUser() {

        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

}
