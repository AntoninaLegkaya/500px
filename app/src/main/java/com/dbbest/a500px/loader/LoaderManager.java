package com.dbbest.a500px.loader;

import android.support.annotation.DrawableRes;
import android.view.View;

public final class LoaderManager {

    private LoaderManager() {
    }

    public static Loader makeLoader(boolean isGlide, View view, @DrawableRes
            int holder, String url) {
        LoaderBuilder builder;
        if (isGlide) {
            builder = new GlideBuilder();
            ((GlideBuilder) builder).view(view)
                    .urlLoad(url)
                    .placeholder(holder);
            return new LoaderImp(builder);
        } else {
            builder = new PicassoBuilder();
            ((PicassoBuilder) builder).view(view)
                    .urlLoad(url)
                    .placeholder(holder);
            return new LoaderImp(builder);
        }
    }

}
