package com.dbbest.a500px;

import android.app.Application;

import com.facebook.stetho.Stetho;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import timber.log.Timber;

@SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
public class App extends Application {

    public static final String PREF_TOKEN_SECRET = "500px.tokenSecret";
    public static final String PREF_ACCESS_TOKEN = "500px.accessToken";
    public static final String SHARED_PREFERENCES = "500pxSharedPreferences";
    private static App instance;

    /**
     * Method to get Application instance
     *
     * @return App instance
     */
    public static App instance() {
        return instance;
    }

    @Override

    public void onCreate() {
        super.onCreate();
        instance = this;
        Stetho.initializeWithDefaults(this);
        Timber.plant(new Timber.DebugTree());

    }
}
