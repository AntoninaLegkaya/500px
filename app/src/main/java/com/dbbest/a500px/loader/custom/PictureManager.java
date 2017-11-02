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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import timber.log.Timber;

@SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
public final class PictureManager {

    static final int DOWNLOAD_FAILED = -1;
    static final int DOWNLOAD_STARTED = 1;
    static final int DOWNLOAD_COMPLETE = 2;
    static final int DECODE_STARTED = 3;
    static final int TASK_COMPLETE = 4;
    private static final int BITMAP_CACHE_SIZE = 10;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;
    private static final int IMAGE_CACHE_SIZE = 1024 * 1024 * 4;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final PictureManager managerInstance;
    //    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    static {

        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        managerInstance = new PictureManager();
    }

    private final LruCache<URL, byte[]> pictureCache;
    private final Queue<PictureTask> pictureTaskWorkQueue;
    private final ThreadPoolExecutor downloadThreadPool;
    private final ThreadPoolExecutor decodeThreadPool;
    private final BitmapPool bitmapPool;
    private final Handler handler;


    private PictureManager() {
        pictureCache = new LruCache<URL, byte[]>(IMAGE_CACHE_SIZE) {
            @Override
            protected int sizeOf(URL paramURL, byte[] paramArrayOfByte) {
                return paramArrayOfByte.length;
            }
        };

        BlockingQueue<Runnable> downloadWorkQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Runnable> decodeWorkQueue = new LinkedBlockingQueue<>();
        pictureTaskWorkQueue = new LinkedBlockingQueue<>();
        bitmapPool = new BitmapPool(140, 140, BITMAP_CACHE_SIZE);

        downloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, downloadWorkQueue);
        decodeThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, decodeWorkQueue);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                PictureTask task = (PictureTask) inputMessage.obj;
                PictureView pictureView = task.getPictureView();
                if (pictureView != null) {
                    URL pictureUrl = pictureView.getPictureViewUrl();
                    if (task.getPictureURL() == pictureUrl) {
                        switch (inputMessage.what) {
                            case DOWNLOAD_STARTED:
                                pictureView.setImageResource(pictureView.getPlaceHolder());
//                                Timber.i("Manager handle message:DOWNLOAD started");
                                break;

                            case DOWNLOAD_COMPLETE:
//                                Timber.i("Manager handle message: DOWNLOAD completed");
                                break;
                            case DECODE_STARTED:
//                                Timber.i("Manager handle message: DECODE started");
                                break;
                            case TASK_COMPLETE:
//                                Timber.i("Manager handle message: DECODE completed");
                                pictureView.setImageBitmap(task.getImage());
                                recycleTask(task);
                                break;
                            case DOWNLOAD_FAILED:
                                pictureView.setImageResource(pictureView.getPlaceHolder());
                                recycleTask(task);
                                Timber.i("Manager handle message:DOWNLOAD fail");
                                break;
                            default:
                                super.handleMessage(inputMessage);
                                break;
                        }
                    }
                }
            }
        };
    }

    static PictureManager getInstance() {
        return managerInstance;
    }

    static PictureTask startDownload(PictureView view, boolean cacheFlag) {
        PictureTask downloadTask = managerInstance.pictureTaskWorkQueue.poll();
        if (downloadTask == null) {
            downloadTask = new PictureTask();
        }

        downloadTask.initializeDownloaderTask(view, cacheFlag);
        downloadTask.setByteBuffer(managerInstance.pictureCache.get(downloadTask.getPictureURL()));
        if (downloadTask.getByteBuffer() == null) {
            managerInstance.downloadThreadPool.execute(downloadTask.getDownloadRunnable());
        } else {
            managerInstance.handleState(downloadTask, DOWNLOAD_COMPLETE);
        }
        return downloadTask;
    }

    static void removeLoader(PictureTask downloaderTask, URL pictureURL) {
        if (downloaderTask != null && downloaderTask.getPictureURL().toString().equals(pictureURL.toString())) {
            synchronized (managerInstance) {
                Thread thread = downloaderTask.getCurrentThread();
                if (null != thread) {
                    thread.interrupt();
                }
            }
            managerInstance.downloadThreadPool.remove(downloaderTask.getDownloadRunnable());
            managerInstance.decodeThreadPool.remove(downloaderTask.getDecodeRunnable());
        }
    }

    BitmapPool getBitmapPool() {
        return bitmapPool;
    }

    void handleState(PictureTask task, int state) {
        switch (state) {
            case DOWNLOAD_COMPLETE: {
                decodeThreadPool.execute(task.getDecodeRunnable());
                break;
            }
            case TASK_COMPLETE: {
                if (task.isCacheEnabled()) {
                    pictureCache.put(task.getPictureURL(), task.getByteBuffer());
                }
                Message completeMessage = handler.obtainMessage(state, task);
                completeMessage.sendToTarget();
                break;
            }
            default:
                handler.obtainMessage(state, task).sendToTarget();
                break;
        }
    }

     boolean recycleTask(PictureTask downloadTask) {
        downloadTask.recycle();
        return pictureTaskWorkQueue.offer(downloadTask);
    }

}
