package com.dbbest.a500px.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dbbest.a500px.R;
import com.dbbest.a500px.loader.LoaderType;
import com.dbbest.a500px.loader.PictureLoaderManager;
import com.dbbest.a500px.loader.custom.PictureView;

public class PhotoActivity extends BaseActivity {

    public static final String PHOTOGRAPH_NAME = "name";
    public static final String PHOTO_URL = "url";
    private PictureView photoView;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar bar = findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        photoView = findViewById(R.id.image_photo);
        TextView nameView = findViewById(R.id.text_name);
        if (intent != null) {
            String name = intent.getStringExtra(PHOTOGRAPH_NAME);
            url = intent.getStringExtra(PHOTO_URL);
            if (name != null) {
                nameView.setText(name);
                PictureLoaderManager.getInstance(photoView.getContext())
                        .createPictureLoader(photoView, R.drawable.ic_empty, url).loadBitmap();
            }
        }
    }

    @Override
    public void onRefreshData() {
        if (preferences != null) {
            PictureLoaderManager.getInstance(photoView.getContext()).setLoaderType(preferences.getString(SHARED_KEY, LoaderType.GLIDE.geType()));
            PictureLoaderManager.getInstance(photoView.getContext())
                    .createPictureLoader(photoView, R.drawable.ic_empty, url).loadBitmap();
        }
    }
}
