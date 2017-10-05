package com.dbbest.a500px.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dbbest.a500px.Constant;
import com.dbbest.a500px.R;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ImageView photoView = (ImageView) findViewById(R.id.image_photo);
        TextView nameView = (TextView) findViewById(R.id.text_name);

        if (intent != null) {

            String name = intent.getStringExtra(Constant.PHOTOGRAPH_NAME);
            String url = intent.getStringExtra(Constant.PHOTO_URL);
            if (name != null) {

                nameView.setText(name);
                onPhotoSet(url, photoView);

            }
        }
    }

    void onPhotoSet(String fullPreviewUrl, ImageView previewView) {

        Glide.with(previewView.getContext())
                .load(fullPreviewUrl)
                .bitmapTransform(new CropSquareTransformation(previewView.getContext()))
                .placeholder(R.drawable.ic_empty)
                .fitCenter()
                .into(previewView);
    }
}
