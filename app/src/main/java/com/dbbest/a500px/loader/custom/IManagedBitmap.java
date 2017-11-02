package com.dbbest.a500px.loader.custom;

import android.graphics.Bitmap;

public interface IManagedBitmap {
    Bitmap getBitmap();

    void recycle();

    IManagedBitmap retain();

}
