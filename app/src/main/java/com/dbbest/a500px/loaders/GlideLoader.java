package com.dbbest.a500px.loaders;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dbbest.a500px.loaders.interfaces.Provider;
import com.dbbest.a500px.loaders.interfaces.ProviderType;

import timber.log.Timber;

public class GlideLoader implements Provider {
    private final DataLoadProvider dataLoadProvider;

    GlideLoader(DataLoadProvider dataLoadProvider) {
        this.dataLoadProvider = dataLoadProvider;
    }

    @Override
    public void loadImage() {
        Timber.i("Load from Glide");
        Glide.with(dataLoadProvider.getView().getContext())
                .load(dataLoadProvider.getUrl())
                .placeholder(dataLoadProvider.getPlaceholder())
                .centerCrop()
                .into((ImageView) dataLoadProvider.getView());
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.GLIDE;
    }
}
