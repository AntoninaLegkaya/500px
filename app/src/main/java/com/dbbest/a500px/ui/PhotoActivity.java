package com.dbbest.a500px.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dbbest.a500px.R;
import com.dbbest.a500px.loader.LoaderManager;

public class PhotoActivity extends AppCompatActivity {

    public static final String PHOTOGRAPH_NAME = "name";
    public static final String PHOTO_URL = "url";
    private static final String SETTINGS = "settings";
    private static final String IS_GLIDE = "checkedGlide";
    private SharedPreferences preferences;
    private ImageView photoView;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        preferences = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        photoView = (ImageView) findViewById(R.id.image_photo);
        TextView nameView = (TextView) findViewById(R.id.text_name);
        if (intent != null) {
            String name = intent.getStringExtra(PHOTOGRAPH_NAME);
            url = intent.getStringExtra(PHOTO_URL);
            if (name != null) {
                nameView.setText(name);
                fillPhoto();
            }
        }
    }

    private void fillPhoto() {
        boolean isGlide = preferences.getBoolean(IS_GLIDE, false);
        LoaderManager.makeLoader(isGlide, photoView, R.drawable.ic_empty, url).loadImage();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        switch (item.getItemId()) {
            case R.id.action_glide_check: {
                if (!item.isChecked()) {
                    item.setChecked(true);
                    editor.putBoolean(IS_GLIDE, true);
                }
                break;
            }
            case R.id.action_picasso_check: {
                if (!item.isChecked()) {
                    item.setChecked(true);
                    editor.putBoolean(IS_GLIDE, false);
                }
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        editor.apply();
        fillPhoto();
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
        boolean isGlideChecked = preferences.getBoolean(IS_GLIDE, false);
        MenuItem itemGlide = menu.findItem(R.id.action_glide_check);
        MenuItem itemPicasso = menu.findItem(R.id.action_picasso_check);
        if (isGlideChecked) {
            itemGlide.setChecked(true);
        } else {
            itemPicasso.setChecked(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }
}
