package com.dbbest.a500px.imageProvider;

import android.widget.ImageView;

import com.dbbest.a500px.imageProvider.interfaces.interfaces.Provider;
import com.dbbest.a500px.imageProvider.interfaces.interfaces.ProviderType;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

public class PicassoLoader implements Provider {
    private Loader loader;

    public PicassoLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public void loadImage() {
        Timber.i("Load from Picasso");
        Picasso.with(loader.getView().getContext())
                .load(loader.getUrl())
                .placeholder(loader.getPlaceholder())
                .into((ImageView) loader.getView());
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.PICASSO;
    }
}