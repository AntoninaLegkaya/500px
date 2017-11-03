package com.dbbest.a500px.loader;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

class PicassoBuilder extends PictureLoaderBuilder {

    private RequestCreator creator;
    private final View imageView;

    PicassoBuilder(String url, int holder, View view) {
        super(url, holder);
        this.imageView = view;
    }

    @Override
    public PictureLoaderBuilder loadBitmap() {
        return requestBuilder()
                .holder()
                .applyCropCenter()
                .into();
    }

    private PicassoBuilder requestBuilder() {
        if (imageView != null) {
            creator = Picasso.with(imageView.getContext()).load(getUrl());
        }
        return this;
    }

    private PicassoBuilder holder() {
        if (creator != null) {
            creator.placeholder(getHolder());
        }
        return this;
    }

    private PicassoBuilder applyCropCenter() {
        if (creator != null) {
            creator.fit().centerCrop();
        }
        return this;
    }


    private PicassoBuilder into() {
        if (creator != null) {
            creator.into((ImageView) imageView);
        }
        return this;
    }

}
