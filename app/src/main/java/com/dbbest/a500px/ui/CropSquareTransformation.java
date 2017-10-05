package com.dbbest.a500px.ui;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;


public class CropSquareTransformation implements Transformation<Bitmap> {

    private final BitmapPool mBitmapPool;
    private int mWidth;
    private int mHeight;

    public CropSquareTransformation(Context context) {

        this.mBitmapPool = Glide.get(context).getBitmapPool();
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        int size = Math.min(source.getWidth(), source.getHeight());

        mWidth = (source.getWidth() - size) / 2;
        mHeight = (source.getHeight() - size) / 2;

        Bitmap bitmap = Bitmap.createBitmap(source, mWidth, mHeight, size, size);
        if (bitmap.equals(source)) {
            source.recycle();
        }

        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    @Override
    public String getId() {
        return "CropSquareTransformation(width=" + mWidth + ", height=" + mHeight + ")";
    }
}
