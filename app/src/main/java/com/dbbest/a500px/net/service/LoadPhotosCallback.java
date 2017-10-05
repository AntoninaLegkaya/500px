package com.dbbest.a500px.net.service;

import android.database.Cursor;

public interface LoadPhotosCallback {

    void onPhotoSuccessLoad(Cursor cursor);

    void onPhotoFailLoad();
}
