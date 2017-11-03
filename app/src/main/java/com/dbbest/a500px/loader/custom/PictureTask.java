package com.dbbest.a500px.loader.custom;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;
import java.net.URL;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("EI_EXPOSE_REP")
public class PictureTask implements PictureDownloadRunnable.TaskRunnableDownloadMethods, PictureDecodeRunnable.ReusableRunnableMethods {

    private final Runnable downloadRunnable;
    //    private final static PictureManager pictureManager=PictureManager.getInstance();
    private byte[] pictureBuffer;
    private Runnable decodeRunnable;
    private URL pictureURL;
    private Thread currentThread;
    private WeakReference<PictureView> pictureWeakRef;
    private int targetWidth;
    private int targetHeight;
    private Bitmap decodedImage;
    private boolean cacheEnabled;

    PictureTask() {
        downloadRunnable = new PictureDownloadRunnable(this);
        decodeRunnable = new PictureDecodeRunnable(this);
    }

    @Override
    public byte[] getByteBuffer() {
        return pictureBuffer;
    }

    @Override
    public void setByteBuffer(byte[] buffer) {
        pictureBuffer = buffer;
    }

    @Override
    public URL getPictureURL() {
        return pictureURL;
    }

    @Override
    public void handleDownloadState(int state) {
        int outState = -1;
        switch (state) {
            case PictureDownloadRunnable.HTTP_STATE_STARTED: {
                outState = PictureManager.DOWNLOAD_STARTED;
                break;
            }
            case PictureDownloadRunnable.HTTP_STATE_COMPLETED: {
                decodeRunnable = new PictureDecodeRunnable(this);
                outState = PictureManager.DOWNLOAD_COMPLETE;
                break;
            }
            case PictureDownloadRunnable.HTTP_STATE_FAILED: {
                outState = PictureManager.DOWNLOAD_FAILED;
                break;

            }
            default:
                break;
        }
        handleState(outState);
    }

    @Override
    public void setDecodeThread(Thread thread) {
        setCurrentThread(thread);
    }

    @Override
    public void handleDecodeState(int state) {
        int outState;
        switch (state) {
            case PictureDecodeRunnable.DECODE_STATE_COMPLETED:
                outState = PictureManager.TASK_COMPLETE;
                break;
            case PictureDecodeRunnable.DECODE_STATE_FAILED:
                outState = PictureManager.DOWNLOAD_FAILED;
                break;
            default:
                outState = PictureManager.DECODE_STARTED;
                break;
        }

        handleState(outState);
    }

    @Override
    public int getTargetWidth() {
        return targetWidth;
    }

    @Override
    public int getTargetHeight() {
        return targetHeight;
    }

    @Override
    public BitmapPool getBitmapPool() {
        return PictureManager.getInstance().getBitmapPool();
    }

    Bitmap getImage() {
        return decodedImage;
    }

    @Override
    public void setImage(Bitmap image) {
        decodedImage = image;
    }

    boolean isCacheEnabled() {
        return cacheEnabled;
    }

    Runnable getDownloadRunnable() {
        return downloadRunnable;
    }

    Thread getCurrentThread() {
        synchronized (PictureManager.getInstance()) {
            return currentThread;
        }
    }

    private void setCurrentThread(Thread thread) {
        synchronized (PictureManager.getInstance()) {
            currentThread = thread;
        }
    }

    void initializeDownloaderTask(
            PictureView pictureView,
            boolean cacheFlag) {

        pictureURL = pictureView.getPictureViewUrl();

        pictureWeakRef = new WeakReference<>(pictureView);

        // Sets the cache flag to the input argument
        cacheEnabled = cacheFlag;

        targetWidth = pictureView.getWidth();
        targetHeight = pictureView.getHeight();


    }

    void recycle() {

        // Deletes the weak reference to the imageView
        if (null != pictureWeakRef) {
            pictureWeakRef.clear();
            pictureWeakRef = null;
        }

        pictureBuffer = null;
    }

    PictureView getPictureView() {
        if (pictureWeakRef != null) {
            return pictureWeakRef.get();
        }
        return null;
    }

    Runnable getDownloadThread() {

        return downloadRunnable;
    }

    @Override
    public void setDownloadThread(Thread currThread) {
        setCurrentThread(currThread);
    }

    Runnable getDecodeRunnable() {
        return decodeRunnable;
    }

    private void handleState(int state) {
        PictureManager.getInstance().handleState(this, state);
    }
}
