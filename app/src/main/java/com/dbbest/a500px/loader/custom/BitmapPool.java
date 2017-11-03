package com.dbbest.a500px.loader.custom;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class BitmapPool {

    private final ArrayList<Bitmap> pool;
    private final int poolLimit;
    private final int width;
    private final int height;


    BitmapPool(int bitmapWidth,
               int bitmapHeight,
               int limit) {
        this.width = bitmapWidth;
        this.height = bitmapHeight;
        this.poolLimit = limit;
        pool = new ArrayList<>(poolLimit);
    }

    Bitmap getBitmap() {
        synchronized (BitmapPool.class) {
            for (int i = 0; i < pool.size(); i++) {
                Bitmap b = pool.get(i);
                if (b.getWidth() == width && b.getHeight() == height) {
                    return pool.remove(i);
                }
            }
            return null;
        }
    }

    void returnBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if ((bitmap.getWidth() != width) ||
                (bitmap.getHeight() != height)) {
            bitmap.recycle();
            return;
        }
        synchronized (this) {
            if (pool.size() >= poolLimit) {
                pool.remove(0);
            }
            pool.add(bitmap);
//            Timber.i("Bitmap pool size: %d", pool.size());
        }
    }

    public  void clear() {
        synchronized (BitmapPool.class) {
            pool.clear();
        }
    }
}
