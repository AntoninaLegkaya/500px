package com.dbbest.a500px.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dbbest.a500px.R;
import com.dbbest.a500px.loader.LoaderType;

public class BaseActivity extends AppCompatActivity {
    protected static final String SETTINGS = "settings";
    protected static final String SHARED_KEY = "name";
    protected SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            switch (item.getItemId()) {

                case R.id.action_glide_check: {
                    if (!item.isChecked()) {
                        item.setChecked(true);
                        editor.putString(SHARED_KEY, LoaderType.GLIDE.geType());
                    }
                    break;
                }
                case R.id.action_picasso_check: {
                    if (!item.isChecked()) {
                        item.setChecked(true);
                        editor.putString(SHARED_KEY, LoaderType.PICASSO.geType());
                    }
                    break;
                }
                case R.id.action_loader_check: {
                    if (!item.isChecked()) {
                        item.setChecked(true);
                        editor.putString(SHARED_KEY, LoaderType.LOADER.geType());
                    }
                    break;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
            editor.apply();

            onRefreshData();
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (preferences != null) {
            LoaderType loaderType = LoaderType.fromString(preferences.getString(SHARED_KEY, LoaderType.GLIDE.geType()));
            MenuItem itemGlide = menu.findItem(R.id.action_glide_check);
            MenuItem itemPicasso = menu.findItem(R.id.action_picasso_check);
            MenuItem itemLoader = menu.findItem(R.id.action_loader_check);

            switch (loaderType) {
                case GLIDE: {
                    itemGlide.setChecked(true);
                    break;
                }
                case PICASSO: {
                    itemPicasso.setChecked(true);
                    break;
                }
                case LOADER: {
                    itemLoader.setChecked(true);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown type of loader");
                }
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void onRefreshData() {
        // implement in child
    }
}
