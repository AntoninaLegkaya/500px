package com.dbbest.a500px.net.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.dbbest.a500px.BuildConfig;
import com.dbbest.a500px.data.PhotoEntry;
import com.dbbest.a500px.model.PhotoModel;
import com.dbbest.a500px.net.responce.ListPhotos;
import com.dbbest.a500px.net.responce.Photo;
import com.dbbest.a500px.net.retrofit.RestClient;

import java.util.ArrayList;
import java.util.List;

public class BindingExecuteService extends Service {

    public static final int DOWNLOAD_LIMIT = 50;
    public static final int STATUS_FAILED = -1;
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_SUCCESSFUL = 1;
    private static final String PAGE = "page";
    private static final String COUNT = "command";
    private static final String IMAGE_SIZE_FLAG = "image_size";
    private static final String IMAGE_SIZE = "2,3";
    private final ServiceProducer producer = new ServiceProducer();
    private final ServiceClient serviceClient = new ServiceClient();
    private HandlerThread workThread;
    private ServiceHandler localHandler;
    private Handler uiHandler;

    public static Intent startService(Context context, int page) {
        final Intent intent = new Intent(context, BindingExecuteService.class);
        intent.putExtra(IMAGE_SIZE_FLAG, IMAGE_SIZE);
        intent.putExtra(PAGE, page);
        intent.putExtra(COUNT, DOWNLOAD_LIMIT);
        return intent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return producer;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        workThread = new HandlerThread("BindingExecuteService.HandlerThread");
        workThread.start();
        localHandler = new ServiceHandler(workThread.getLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        workThread.quit();
    }


    private static final class ServiceHandler extends Handler {

        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }


    private class ServiceProducer extends Binder implements Producer {

        @Override
        public void removeClient(Client clientService) {
            serviceClient.client = null;
        }

        @Override
        public void addClient(Client clientService) {
            serviceClient.client = clientService;
        }

        public void executeService(final Intent workIntent) {

            localHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (workIntent != null) {
                        serviceClient.onRequestStatusChanged(STATUS_RUNNING);
                        RestClient restClient = RestClient.getInstance();
                        int count = workIntent.getIntExtra(COUNT, 5);
                        int page = workIntent.getIntExtra(PAGE, 1);
                        String size = workIntent.getStringExtra(IMAGE_SIZE_FLAG);
                        ListPhotos results = restClient.getPhotos(BuildConfig.CONSUMER_KEY, page,
                                count, size);
                        List<ContentValues> photosToSave = new ArrayList<>();

                        if (results == null) {
                            serviceClient.onRequestStatusChanged(STATUS_FAILED);

                        } else {
                            for (Photo photo : results.getPhotos()) {
                                PhotoModel photoModel = new PhotoModel(photo);
                                photosToSave.add(photoModel.values());
                            }
                            if (photosToSave.isEmpty()) {
                                serviceClient.onRequestStatusChanged(STATUS_FAILED);
                            } else {
                                ContentValues[] cvArray = new ContentValues[photosToSave.size()];
                                photosToSave.toArray(cvArray);
                                getContentResolver().bulkInsert(PhotoEntry.URI, cvArray);
                                serviceClient.onRequestStatusChanged(STATUS_SUCCESSFUL);
                            }
                        }
                    }
                }
            });

        }

        public void registerHandler(Handler handler) {
            uiHandler = handler;
        }
    }

    private class ServiceClient implements Client {

        private Client client;

        @Override
        public void onRequestStatusChanged(final int status) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    client.onRequestStatusChanged(status);
                }
            });

        }
    }

}
