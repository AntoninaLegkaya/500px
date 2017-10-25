package com.dbbest.a500px.loader;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

class GlideBuilder extends LoaderBuilder {
    private DrawableRequestBuilder drBuilder;
    private View view;

    private GlideBuilder requestBuilder() {
        if (view != null) {
            drBuilder = Glide.with(view.getContext()).load(getUrl());
        }
        return this;
    }

    private GlideBuilder holder() {
        if (drBuilder != null) {
            drBuilder.placeholder(getHolder());
        }
        return this;
    }

    private GlideBuilder into() {
        if (drBuilder != null) {
            drBuilder.into((ImageView) view);
        }
        return this;
    }

    private GlideBuilder applyCropCenter() {
        if (drBuilder != null) {
            drBuilder.centerCrop();
        }
        return this;
    }

    GlideBuilder view(View imageView) {
        this.view = imageView;
        return this;
    }


    @Override
    public LoaderBuilder build() {
        return requestBuilder()
                .holder()
                .applyCropCenter()
                .into();
    }
}
