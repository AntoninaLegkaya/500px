package com.dbbest.a500px.loader.custom;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;

import java.net.URL;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class PictureCustomManager {

    static final int DOWNLOAD_FAILED = -1;
    static final int DOWNLOAD_STARTED = 1;
    static final int DOWNLOAD_COMPLETE = 2;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;
    private static final int IMAGE_CACHE_SIZE = 1024 * 1024 * 4;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;
    private static final int KEEP_ALIVE_TIME = 1;
    private static PictureCustomManager managerInstance = null;
//    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    static {

        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        managerInstance = new PictureCustomManager();
    }

    private final LruCache<URL, byte[]> pictureCache;
    private final Queue<PictureTask> pictureTaskWorkQueue;
    private final BlockingQueue<Runnable> downloadWorkQueue;
    private final ThreadPoolExecutor downloadThreadPool;
    private Handler handler;

    private PictureCustomManager() {
        pictureCache = new LruCache<URL, byte[]>(IMAGE_CACHE_SIZE) {
            @Override
            protected int sizeOf(URL paramURL, byte[] paramArrayOfByte) {
                return paramArrayOfByte.length;
            }
        };
        downloadWorkQueue = new LinkedBlockingQueue<>();
        pictureTaskWorkQueue = new LinkedBlockingQueue<>();

        downloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, downloadWorkQueue);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                PictureTask task = (PictureTask) inputMessage.obj;
                PictureView pictureView = task.getPictureView();
                if (pictureView != null) {
                    URL pictureUrl = pictureView.getPictureUrl();
                    if (task.getPictureURL() == pictureUrl)
                        switch (inputMessage.what) {
                            case DOWNLOAD_STARTED:
                                Timber.i("Manager handle message: started");
                                break;

                            case DOWNLOAD_COMPLETE:
                                Timber.i("Manager handle message: completed");
                                break;

                            case DOWNLOAD_FAILED:
                                recycleTask(task);
                                Timber.i("Manager handle message: fail");
                                break;
                            default:
                                super.handleMessage(inputMessage);
                        }
                }
            }
        };
    }

    static PictureCustomManager getInstance() {
        return managerInstance;
    }

    static public PictureTask startDownload(PictureView view, boolean cacheFlag) {

        PictureTask downloadTask = managerInstance.pictureTaskWorkQueue.poll();

        if (downloadTask == null) {

            downloadTask = new PictureTask();
        }
        downloadTask.initializeDownloaderTask(PictureCustomManager.managerInstance, view, cacheFlag);
        downloadTask.setByteBuffer(managerInstance.pictureCache.get(downloadTask.getPictureURL()));

        if (downloadTask.getByteBuffer() == null) {
            managerInstance.downloadThreadPool.execute(downloadTask.getDownloadRunnable());
            Timber.i("Manager start pool thread");
        } else {
            managerInstance.handleState(downloadTask, DOWNLOAD_COMPLETE);
        }
        return downloadTask;
    }

    void handleState(PictureTask task, int state) {
        switch (state) {

            case DOWNLOAD_STARTED: {

                break;
            }
            case DOWNLOAD_COMPLETE: {
                if (task.isCacheEnabled()) {
                    pictureCache.put(task.getPictureURL(), task.getByteBuffer());
                }
                Message completeMessage = handler.obtainMessage(state, task);
                completeMessage.sendToTarget();
                break;
            }
            case DOWNLOAD_FAILED: {
                Message completeMessage = handler.obtainMessage(state, task);
                completeMessage.sendToTarget();
                break;
            }
            // In all other cases, pass along the message without any other action.
            default:
                handler.obtainMessage(state, task).sendToTarget();
                break;

        }
    }

    private void recycleTask(PictureTask downloadTask) {
        downloadTask.recycle();
        pictureTaskWorkQueue.offer(downloadTask);
    }


}
