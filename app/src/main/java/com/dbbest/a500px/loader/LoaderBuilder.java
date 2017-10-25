package com.dbbest.a500px.loader;

import android.support.annotation.DrawableRes;

public class LoaderBuilder extends AbstractBuilder {

    private String url;
    private
    @DrawableRes
    int holder;

    String getUrl() {
        return url;
    }

    int getHolder() {
        return holder;
    }

    @Override
    public LoaderBuilder build() {
        return this;
    }

    @Override
    public LoaderBuilder urlLoad(String url) {
        this.url = url;
        return this;
    }

    @Override
    public LoaderBuilder placeholder(@DrawableRes int placeholder) {
        this.holder = placeholder;
        return this;
    }

}
