package com.dbbest.a500px.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.dbbest.a500px.R;
import com.dbbest.a500px.imageProvider.Loader;
import com.dbbest.a500px.imageProvider.ProviderManager;

public class PhotoActivity extends AppCompatActivity {

    public static final String PHOTOGRAPH_NAME = "name";
    public static final String PHOTO_URL = "url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ImageView photoView = (ImageView) findViewById(R.id.image_photo);
        TextView nameView = (TextView) findViewById(R.id.text_name);
        if (intent != null) {
            String name = intent.getStringExtra(PHOTOGRAPH_NAME);
            String url = intent.getStringExtra(PHOTO_URL);
            if (name != null) {
                nameView.setText(name);
                ProviderManager manager = new ProviderManager();
                Loader loader = new Loader.Builder(url)
                        .addPlaceholder(R.drawable.ic_empty)
                        .addView(photoView)
                        .build();
                manager.makePicassoProvider(loader).loadImage();
            }
        }
    }
}
