package com.dbbest.a500px.loader;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

class GlideBuilder extends PictureLoaderBuilder {

    private DrawableRequestBuilder drawBuilder;
    private final  View imageView;

    GlideBuilder(String url, int holder, View view) {
        super(url, holder);
        this.imageView = view;
    }

    @Override
    public PictureLoaderBuilder build() {
        return urlLoad()
                .placeholder()
                .applyCropCenter()
                .into();
    }

    private GlideBuilder urlLoad() {
        if (imageView != null) {
            drawBuilder = Glide.with(imageView.getContext()).load(getUrl());
        }
        return this;
    }

    private GlideBuilder placeholder() {
        if (drawBuilder != null) {
            drawBuilder.placeholder(getHolder());
        }
        return this;
    }

    private GlideBuilder applyCropCenter() {
        if (drawBuilder != null) {
            drawBuilder.centerCrop();
        }
        return this;
    }

    private GlideBuilder into() {
        if (drawBuilder != null) {
            drawBuilder.into((ImageView) imageView);
        }
        return this;
    }

}
