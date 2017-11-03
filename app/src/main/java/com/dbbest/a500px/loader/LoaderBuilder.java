package com.dbbest.a500px.loader;

import com.dbbest.a500px.loader.custom.PictureView;

import java.net.MalformedURLException;
import java.net.URL;

public class LoaderBuilder extends PictureLoaderBuilder {
    private boolean cache;
    private PictureView view;

    LoaderBuilder(String url, int holder) {
        super(url, holder);
    }

    LoaderBuilder(String url, int holder, PictureView pictureView, boolean isCache) {
        super(url, holder);
        this.cache = isCache;
        this.view = pictureView;
    }

    @Override
    public PictureLoaderBuilder loadBitmap() {
        if (view != null) {
            URL urlPreview = null;
            try {
                urlPreview = new URL(getUrl());
                view.setPictureUrl(urlPreview, getHolder(), cache);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }
}
