package com.dbbest.a500px.net.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.dbbest.a500px.BuildConfig;
import com.dbbest.a500px.data.PhotoEntry;
import com.dbbest.a500px.model.PhotoModel;
import com.dbbest.a500px.net.responce.ListPhotos;
import com.dbbest.a500px.net.responce.Photo;
import com.dbbest.a500px.net.retrofit.RestClient;

import java.util.ArrayList;
import java.util.List;

public class BindingExecuteService extends Service {

    private static final int DOWNLOAD_LIMIT = 50;
    private static final String IMAGE_SIZE = "2,3";

    private final ServiceProducer producer = new ServiceProducer();
    private final ServiceClient serviceClient = new ServiceClient();
    private boolean isLoading;
    private HandlerThread workThread;
    private Handler localHandler;
    private Handler uiHandler;
    private int page;

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
        localHandler = new Handler(workThread.getLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        workThread.quit();
    }

    @WorkerThread
    private void doWork() {

        serviceClient.onRequestStatusRunning();
        RestClient restClient = RestClient.getInstance();
        ListPhotos results = restClient.getPhotos(BuildConfig.CONSUMER_KEY, page,
                DOWNLOAD_LIMIT, IMAGE_SIZE);
        List<ContentValues> photosToSave = new ArrayList<>();

        if (results == null) {
            serviceClient.onRequestStatusFail();

        } else {
            for (Photo photo : results.getPhotos()) {
                PhotoModel photoModel = new PhotoModel(photo);
                photosToSave.add(photoModel.values());
            }
            if (photosToSave.isEmpty()) {
                serviceClient.onRequestStatusFail();
            } else {
                ContentValues[] cvArray = new ContentValues[photosToSave.size()];
                photosToSave.toArray(cvArray);
                getContentResolver().bulkInsert(PhotoEntry.URI, cvArray);
                serviceClient.onRequestStatusSuccess();
            }
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

        public void executeService() {
            localHandler.post(new Runnable() {
                @Override
                public void run() {
                    page = page + 1;
                    doWork();
                }
            });

        }

        @Override
        public void refreshData() {
            localHandler.post(new Runnable() {
                @Override
                public void run() {
                    page = 1;
                    doWork();
                }
            });
        }

        public void registerHandler(Handler handler) {
            uiHandler = handler;
        }

        @Override
        public boolean isLoading() {
            return isLoading;
        }
    }

    private class ServiceClient implements Client {

        private Client client;

        @Override
        public void onRequestStatusRunning() {
            isLoading = true;
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    client.onRequestStatusRunning();
                }
            });
        }

        @Override
        public void onRequestStatusSuccess() {
            isLoading = false;
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    client.onRequestStatusSuccess();
                }
            });
        }

        @Override
        public void onRequestStatusFail() {
            isLoading = false;
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    client.onRequestStatusFail();
                }
            });
        }
    }

}
