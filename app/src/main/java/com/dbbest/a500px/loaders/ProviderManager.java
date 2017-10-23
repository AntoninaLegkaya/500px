package com.dbbest.a500px.loaders;

import com.dbbest.a500px.loaders.interfaces.Provider;
import com.dbbest.a500px.loaders.interfaces.ProviderType;

public class ProviderManager implements CreatorManager {

    @Override
    public Provider makeProvider(ProviderBuilder data, ProviderType type) {

        if (type == ProviderType.GLIDE) {
            return new GlideLoader(data);
        } else {
            return new PicassoLoader(data);
        }
    }

}
