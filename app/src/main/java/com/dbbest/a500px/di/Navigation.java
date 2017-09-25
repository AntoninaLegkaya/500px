package com.dbbest.a500px.di;

import android.content.Context;
import android.content.Intent;

import com.dbbest.a500px.ui.PhotosGalleryActivity;

public class Navigation {

    public Intent galleryActivity(Context context) {
        return PhotosGalleryActivity.start(context);
    }
}
