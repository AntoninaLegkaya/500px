package com.dbbest.a500px.loaders;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dbbest.a500px.loaders.interfaces.Provider;
import com.dbbest.a500px.loaders.interfaces.ProviderType;

public class GlideLoader implements Provider {
    private final ProviderBuilder providerBuilder;

    GlideLoader(ProviderBuilder data) {
        this.providerBuilder = data;
    }

    @Override
    public void loadImage() {
        Glide.with(providerBuilder.getView().getContext())
                .load(providerBuilder.getUrl())
                .placeholder(providerBuilder.getPlaceholder())
                .centerCrop()
                .into((ImageView) providerBuilder.getView());
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.GLIDE;
    }
}
