package com.dbbest.a500px.loaders;

import com.dbbest.a500px.loaders.interfaces.Provider;
import com.dbbest.a500px.loaders.interfaces.ProviderType;

public interface CreatorManager {

    Provider makeProvider(ProviderBuilder providerBuilder, ProviderType type);

}
