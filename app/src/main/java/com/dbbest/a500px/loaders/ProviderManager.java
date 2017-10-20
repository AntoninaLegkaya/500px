package com.dbbest.a500px.loaders;

import com.dbbest.a500px.loaders.interfaces.Provider;

public class ProviderManager implements CreatorManager {

    @Override
    public Provider makeGlideProvider(DataLoadProvider dataLoadProvider) {
        return new GlideLoader(dataLoadProvider);
    }

    @Override
    public Provider makePicassoProvider(DataLoadProvider dataLoadProvider) {
        return new PicassoLoader(dataLoadProvider);
    }
}
