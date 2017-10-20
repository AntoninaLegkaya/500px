package com.dbbest.a500px.loaders;

import android.widget.ImageView;

import com.dbbest.a500px.loaders.interfaces.Provider;
import com.dbbest.a500px.loaders.interfaces.ProviderType;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

public class PicassoLoader implements Provider {
    private final DataLoadProvider dataLoadProvider;

    PicassoLoader(DataLoadProvider dataLoadProvider) {
        this.dataLoadProvider = dataLoadProvider;
    }

    @Override
    public void loadImage() {
        Timber.i("Load from Picasso");
        Picasso.with(dataLoadProvider.getView().getContext())
                .load(dataLoadProvider.getUrl())
                .placeholder(dataLoadProvider.getPlaceholder())
                .into((ImageView) dataLoadProvider.getView());
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.PICASSO;
    }
}