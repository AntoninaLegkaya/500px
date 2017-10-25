package com.dbbest.a500px.loader;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

class PicassoBuilder extends LoaderBuilder {

    private RequestCreator creator;
    private View view;

    private PicassoBuilder requestBuilder() {
        if (view != null) {
            creator = Picasso.with(view.getContext()).load(getUrl());
        }
        return this;
    }

    private PicassoBuilder holder() {
        if (creator != null) {
            creator.placeholder(getHolder());
        }
        return this;
    }

    private PicassoBuilder into() {
        if (creator != null) {
            creator.into((ImageView) view);
        }
        return this;
    }


    PicassoBuilder view(View imageView) {
        this.view = imageView;
        return this;
    }


    @Override
    public LoaderBuilder build() {
        return requestBuilder()
                .holder()
                .into();
    }

}
