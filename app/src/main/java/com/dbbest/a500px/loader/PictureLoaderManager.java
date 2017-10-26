package com.dbbest.a500px.loader;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.DrawableRes;
import android.view.View;

public final class PictureLoaderManager implements IPictureLoaderBuilder {

    private static final String SETTINGS = "settings";
    private static final String IS_GLIDE = "checkedGlide";
    private static PictureLoaderManager loaderManager;
    private boolean isGlide;
    private PictureLoaderBuilder builder;

    private PictureLoaderManager(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        setGlide(preferences.getBoolean(IS_GLIDE, false));
    }

    public static PictureLoaderManager getInstance(Context context) {
        synchronized (PictureLoaderManager.class) {
            if (loaderManager == null) {
                loaderManager = new PictureLoaderManager(context);
            }
            return loaderManager;
        }
    }

    public IPictureLoaderBuilder createPictureLoader(View view, @DrawableRes
            int holder, String url) {

        if (isGlide) {
            builder = new GlideBuilder(url, holder, view);
        } else {
            builder = new PicassoBuilder(url, holder, view);
        }
        return builder;
    }


    public void setGlide(boolean glide) {
        isGlide = glide;
    }

    @Override
    public IPictureLoaderBuilder build() {
        if (builder != null) {
            return builder.build();
        }
        throw new UnknownError("Instance of PictureLoader not created");
    }
}
