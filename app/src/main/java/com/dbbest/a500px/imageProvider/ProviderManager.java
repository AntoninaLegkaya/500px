package com.dbbest.a500px.imageProvider;

import com.dbbest.a500px.imageProvider.interfaces.interfaces.Provider;

public class ProviderManager implements CreatorManager {

    @Override
    public Provider makeGlideProvider(Loader loader) {
        return new GlideLoader(loader);
    }

    @Override
    public Provider makePicassoProvider(Loader loader) {
        return new PicassoLoader(loader);
    }
}
