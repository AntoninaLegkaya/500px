package com.dbbest.a500px.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dbbest.a500px.R;
import com.dbbest.a500px.net.task.GetPhotosTask;

import org.json.JSONObject;

public class PhotosGalleryActivity extends AppCompatActivity implements GetPhotosTask.Delegate {

    public static Intent start(Context context) {
        return new Intent(context, PhotosGalleryActivity.class);
    }

    @Override
    public void success(JSONObject obj) {
// TODO if success
    }

    @Override
    public void fail() {
// TODO if fail
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }
}
