package com.dbbest.a500px.loader.custom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import timber.log.Timber;

public class PictureDecodeRunnable implements Runnable {

    static final int DECODE_STATE_FAILED = -1;
    static final int DECODE_STATE_STARTED = 0;
    static final int DECODE_STATE_COMPLETED = 1;


    private final ReusableRunnableMethods decodeTask;


    public PictureDecodeRunnable(ReusableRunnableMethods task) {
        this.decodeTask = task;


    }

    public static boolean canUseForInBitmap(
            Bitmap candidate, BitmapFactory.Options targetOptions) {

        // From Android 4.4 (KitKat) onward we can re-use if the byte size of
        // the new bitmap is smaller than the reusable bitmap candidate
        // allocation byte count.
        Timber.i("Check re-use: outWidth: %d:" +
                " outHeight: %d; inSampleSize: %d", targetOptions.outWidth, targetOptions.outHeight, targetOptions.inSampleSize);
        int width = targetOptions.outWidth / targetOptions.inSampleSize;
        int height = targetOptions.outHeight / targetOptions.inSampleSize;
        int byteCount = width * height * getBytesPerPixel(candidate.getConfig());


        try {
            return byteCount <= candidate.getAllocationByteCount();
        } catch (NullPointerException e) {
            return byteCount <= candidate.getHeight() * candidate.getRowBytes();
        }
    }

    private static int getBytesPerPixel(Bitmap.Config config) {
        Bitmap.Config bitmapConfig = config;
        if (config == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        int bytesPerPixel;
        switch (bitmapConfig) {
            case ALPHA_8:
                bytesPerPixel = 1;
                break;
            case RGB_565:
            case ARGB_4444:
                bytesPerPixel = 2;
                break;
            case ARGB_8888:
            default:
                bytesPerPixel = 4;
                break;
        }
        return bytesPerPixel;
    }

    @Override
    public void run() {

        boolean isMutable = false;
        decodeTask.setDecodeThread(Thread.currentThread());

        byte[] pictureBuffer = decodeTask.getByteBuffer();
        Bitmap returnBitmap = null;
        Bitmap reuseBitmap = decodeTask.getBitmapPool().getBitmap();
        try {
            decodeTask.handleDecodeState(DECODE_STATE_STARTED);

            final BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            if (Thread.interrupted()) {
                return;
            }

            bitmapOptions.inJustDecodeBounds = true;

            int targetWidth = decodeTask.getTargetWidth();
            int targetHeight = decodeTask.getTargetHeight();
            BitmapFactory
                    .decodeByteArray(pictureBuffer, 0, pictureBuffer.length, bitmapOptions);
            int hScale = bitmapOptions.outHeight / targetHeight;
            int wScale = bitmapOptions.outWidth / targetWidth;

            int sampleSize = Math.max(hScale, wScale);

            if (sampleSize > 1) {
                bitmapOptions.inSampleSize = sampleSize;
            } else {
                bitmapOptions.inSampleSize = 1;
            }

            if (reuseBitmap != null && canUseForInBitmap(reuseBitmap, bitmapOptions)) {
                Timber.i("Re-use bitmap from Bitmap poll");
                bitmapOptions.inMutable = true;
                bitmapOptions.inBitmap = reuseBitmap;
                isMutable = true;
            }
            if (Thread.interrupted()) {
                return;
            }
            bitmapOptions.inJustDecodeBounds = false;

            try {
                returnBitmap = BitmapFactory.decodeByteArray(
                        pictureBuffer,
                        0,
                        pictureBuffer.length,
                        bitmapOptions
                );
            } catch (Throwable e) {
                if (Thread.interrupted()) {
                    return;
                }
            }

        } finally {
            if (returnBitmap == null) {
                decodeTask.handleDecodeState(DECODE_STATE_FAILED);
            } else {
                if (reuseBitmap == null) {
                    reuseBitmap = returnBitmap;
                    decodeTask.getBitmapPool().returnBitmap(reuseBitmap);
                } else if (isMutable) {
                    decodeTask.getBitmapPool().returnBitmap(reuseBitmap);
                }
                decodeTask.setImage(returnBitmap);
                decodeTask.handleDecodeState(DECODE_STATE_COMPLETED);
            }
            decodeTask.setDecodeThread(null);
            Thread.interrupted();

        }
    }

    interface ReusableRunnableMethods {
        void setDecodeThread(Thread currentThread);

        byte[] getByteBuffer();

        void handleDecodeState(int state);

        int getTargetWidth();

        int getTargetHeight();

        void setImage(Bitmap image);

        BitmapPool getBitmapPool();

    }
}
