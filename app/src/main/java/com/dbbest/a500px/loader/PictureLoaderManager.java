package com.dbbest.a500px.loader;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.dbbest.a500px.loader.custom.PictureView;

import timber.log.Timber;

public final class PictureLoaderManager implements IPictureLoaderBuilder {

    private static final String SETTINGS = "settings";
    private static final String SHARED_KEY = "name";
    private static PictureLoaderManager loaderManager;
    private LoaderType loaderType;
    private PictureLoaderBuilder builder;

    private PictureLoaderManager(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        setLoaderType(preferences.getString(SHARED_KEY, LoaderType.GLIDE.geType()));
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
        switch (loaderType) {
            case GLIDE: {
                Timber.i("Switch to Glide");
                builder = new GlideBuilder(url, holder, view);
                break;
            }
            case PICASSO: {
                Timber.i("Switch to Picasso");
                builder = new PicassoBuilder(url, holder, view);
                break;
            }
            case LOADER: {
                Timber.i("Switch to Loader");
                builder = new LoaderBuilder(url, holder, (PictureView) view, true);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown type of loader");
            }

        }
        return builder;
    }

    public void setLoaderType(String type) {
        loaderType = LoaderType.fromString(type);
    }

    @Override
    public IPictureLoaderBuilder loadBitmap() {
        if (builder != null) {
            return builder.loadBitmap();
        }
        throw new UnknownError("Instance of PictureLoader not created");
    }
}
