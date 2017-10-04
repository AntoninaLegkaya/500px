package com.dbbest.a500px.db.model;

public class CardModel {
    public String nameUser;
    public String imageUrl;

    public CardModel(String nameUser, String imageUrl) {

        this.nameUser = nameUser;
        this.imageUrl = imageUrl;

    }

    public CardModel() {
        //nothing todo
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getNameUser() {

        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

}
