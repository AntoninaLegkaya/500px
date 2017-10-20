package com.dbbest.a500px.loaders;

import com.dbbest.a500px.loaders.interfaces.Provider;

public interface CreatorManager {

    Provider makeGlideProvider(DataLoadProvider dataLoadProvider);

    Provider makePicassoProvider(DataLoadProvider dataLoadProvider);
}
