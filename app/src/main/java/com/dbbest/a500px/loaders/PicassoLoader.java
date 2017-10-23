package com.dbbest.a500px.loaders;

import android.widget.ImageView;

import com.dbbest.a500px.loaders.interfaces.Provider;
import com.dbbest.a500px.loaders.interfaces.ProviderType;
import com.squareup.picasso.Picasso;

public class PicassoLoader implements Provider {
    private final ProviderBuilder providerBuilder;

    PicassoLoader(ProviderBuilder data) {
        this.providerBuilder = data;
    }

    @Override
    public void loadImage() {
        Picasso.with(providerBuilder.getView().getContext())
                .load(providerBuilder.getUrl())
                .placeholder(providerBuilder.getPlaceholder())
                .into((ImageView) providerBuilder.getView());
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.PICASSO;
    }
}