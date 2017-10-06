package com.dbbest.a500px.net.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;

import com.dbbest.a500px.BuildConfig;
import com.dbbest.a500px.model.PhotoModel;
import com.dbbest.a500px.net.responce.ListPhotos;
import com.dbbest.a500px.net.responce.Photo;
import com.dbbest.a500px.net.retrofit.RestClient;
import com.dbbest.a500px.simpleDb.PhotoEntry;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("RestrictedApi")
public class ExecuteService extends IntentService {

    public static final int STATUS_FAILED = -1;
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_SUCCESSFUL = 1;
    public static final int DOWNLOAD_LIMIT = 10;
    private static final String PAGE = "page";
    private static final String RECEIVER = "receiver";
    private static final String COUNT = "command";
    private static final String IMAGE_SIZE_FLAG = "image_size";
    private static final String IMAGE_SIZE = "2,3";


    public ExecuteService() {
        super("ExecuteService");
    }

    public static Intent startService(Context context, ResultReceiver receiver, int page) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, context, ExecuteService.class);
        intent.putExtra(RECEIVER, receiver);
        intent.putExtra(IMAGE_SIZE_FLAG, IMAGE_SIZE);
        intent.putExtra(PAGE, page);
        intent.putExtra(COUNT, DOWNLOAD_LIMIT);
        return intent;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
            Bundle bundle = new Bundle();
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);
            try {
                RestClient restClient = RestClient.getInstance();
                int count = intent.getIntExtra(COUNT, 5);
                int page = intent.getIntExtra(PAGE, 1);
                String size = intent.getStringExtra(IMAGE_SIZE_FLAG);
                ListPhotos results = restClient.getPhotos(BuildConfig.CONSUMER_KEY, page,
                        count, size);
                List<ContentValues> photosToSave = new ArrayList<>();
                if (results == null) {
                    receiver.send(STATUS_FAILED, bundle);
                } else {
                    for (Photo photo : results.getPhotos()) {
                        PhotoModel photoModel = new PhotoModel(photo);
                        photosToSave.add(photoModel.values());
                    }
                    if (photosToSave.isEmpty()) {
                        receiver.send(STATUS_FAILED, bundle);
                    } else {
                        ContentValues[] cvArray = new ContentValues[photosToSave.size()];
                        photosToSave.toArray(cvArray);
                        getContentResolver().bulkInsert(PhotoEntry.URI, cvArray);
                        receiver.send(STATUS_SUCCESSFUL, Bundle.EMPTY);
                    }
                }

            } catch (Exception e) {
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_FAILED, bundle);
            }
        }
    }
}
