package com.dbbest.a500px.imageProvider;

import com.dbbest.a500px.imageProvider.interfaces.interfaces.Provider;

public interface CreatorManager {

    Provider makeGlideProvider(Loader loader);

    Provider makePicassoProvider(Loader loader);
}
