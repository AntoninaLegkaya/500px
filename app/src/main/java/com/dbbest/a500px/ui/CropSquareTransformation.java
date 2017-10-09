package com.dbbest.a500px.ui;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;


public class CropSquareTransformation implements Transformation<Bitmap> {

    private final BitmapPool bitmapPool;
    private int width;
    private int height;

    public CropSquareTransformation(Context context) {
        this.bitmapPool = Glide.get(context).getBitmapPool();
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        int size = Math.min(source.getWidth(), source.getHeight());

        width = (source.getWidth() - size) / 2;
        height = (source.getHeight() - size) / 2;

        Bitmap bitmap = Bitmap.createBitmap(source, width, height, size, size);
        if (bitmap.equals(source)) {
            source.recycle();
        }
        return BitmapResource.obtain(bitmap, bitmapPool);
    }

    @Override
    public String getId() {
        return "CropSquareTransformation(width=" + width + ", height=" + height + ")";
    }
}
