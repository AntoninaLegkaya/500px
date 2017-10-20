package com.dbbest.a500px.imageProvider;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dbbest.a500px.imageProvider.interfaces.interfaces.Provider;
import com.dbbest.a500px.imageProvider.interfaces.interfaces.ProviderType;

import timber.log.Timber;

public class GlideLoader implements Provider {
    private Loader loader;

    public GlideLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public void loadImage() {
        Timber.i("Load from Glide");
        Glide.with(loader.getView().getContext())
                .load(loader.getUrl())
                .placeholder(loader.getPlaceholder())
                .centerCrop()
                .into((ImageView) loader.getView());
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.GLIDE;
    }
}
