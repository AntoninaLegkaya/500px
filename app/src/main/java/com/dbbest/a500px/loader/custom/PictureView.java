package com.dbbest.a500px.loader.custom;

import android.widget.ImageView;

import java.net.URL;

public class PictureView {
    private URL pictureUrl;
    private ImageView pictureImageView;

    public PictureView(URL url, ImageView view) {
        this.pictureUrl = url;
        this.pictureImageView = view;
    }

    public URL getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(URL url) {
        this.pictureUrl = url;
    }

    public int getPictureWidth() {
        return pictureImageView.getMaxWidth();
    }

    public int getPictureHeght() {
        return pictureImageView.getMaxHeight();
    }
}
