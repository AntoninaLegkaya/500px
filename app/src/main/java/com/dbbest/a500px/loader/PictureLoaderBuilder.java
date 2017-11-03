package com.dbbest.a500px.loader;

import android.support.annotation.DrawableRes;

public class PictureLoaderBuilder implements IPictureLoaderBuilder {

    private final String url;
    private final
    @DrawableRes
    int holder;

    PictureLoaderBuilder(String url, int holder) {
        this.url = url;
        this.holder = holder;
    }

    String getUrl() {
        return url;
    }

    int getHolder() {
        return holder;
    }

    @Override
    public PictureLoaderBuilder loadBitmap() {
        return this;
    }

}
