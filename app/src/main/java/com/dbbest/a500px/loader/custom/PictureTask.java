package com.dbbest.a500px.loader.custom;

import java.lang.ref.WeakReference;
import java.net.URL;

public class PictureTask implements PictureCustomRunnable.TaskRunnableDownloadMethods {
    private static PictureCustomManager pictureCustomManager;
    private byte[] pictureBuffer;
    private Runnable downloadRunnable;
    private URL pictureURL;
    private Thread currentThread;
    private WeakReference<PictureView> pictureWeakRef;
    private int targetWidth;
    private int targetHeight;
    private boolean cacheEnabled;

    public PictureTask() {
        downloadRunnable = new PictureCustomRunnable(this);
        pictureCustomManager = PictureCustomManager.getInstance();
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public Runnable getDownloadRunnable() {
        return downloadRunnable;
    }

    public Thread getCurrentThread() {
        synchronized (pictureCustomManager) {
            return currentThread;
        }
    }

    private void setCurrentThread(Thread currThread) {
        this.currentThread = currThread;
    }

    // Delegates handling the current state of the task to the PictureCustomManager object
    private void handleState(int state) {
        pictureCustomManager.handleState(this, state);
    }

    @Override
    public void setDownloadThread(Thread currThread) {
        setCurrentThread(currThread);
    }

    @Override
    public byte[] getByteBuffer() {
        return pictureBuffer;
    }

    @Override
    public void setByteBuffer(byte[] buffer) {
        pictureBuffer = buffer;
    }

    void initializeDownloaderTask(
            PictureCustomManager pictureManager,
            PictureView pictureView,
            boolean cacheFlag) {
        pictureCustomManager = pictureManager;

        pictureURL = pictureView.getPictureUrl();

        pictureWeakRef = new WeakReference<>(pictureView);

        // Sets the cache flag to the input argument
        cacheEnabled = cacheFlag;

        targetWidth = pictureView.getPictureWidth();
        targetHeight = pictureView.getPictureHeght();

    }


    @Override
    public void handleDownloadState(int state) {
        int outState = -1;
        switch (state) {
            case PictureCustomRunnable.HTTP_STATE_STARTED: {
                outState = PictureCustomManager.DOWNLOAD_STARTED;
                break;
            }
            case PictureCustomRunnable.HTTP_STATE_COMPLETED: {
                outState = PictureCustomManager.DOWNLOAD_COMPLETE;
                break;
            }
            case PictureCustomRunnable.HTTP_STATE_FAILED: {
                outState = PictureCustomManager.DOWNLOAD_FAILED;
                break;

            }
            default:
                break;
        }
        handleState(outState);
    }

    @Override
    public URL getPictureURL() {
        return pictureURL;
    }

    void recycle() {

        // Deletes the weak reference to the imageView
        if (null != pictureWeakRef) {
            pictureWeakRef.clear();
            pictureWeakRef = null;
        }

        pictureBuffer = null;
    }

    public PictureView getPictureView() {
        if (null != pictureWeakRef) {
            return pictureWeakRef.get();
        }
        return null;
    }
}
