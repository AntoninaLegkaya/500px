package com.dbbest.a500px.net.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;

import com.dbbest.a500px.App;
import com.dbbest.a500px.BuildConfig;
import com.dbbest.a500px.Constant;
import com.dbbest.a500px.db.model.PhotoModel;
import com.dbbest.a500px.net.responce.ListPhotos;
import com.dbbest.a500px.net.responce.Photo;
import com.dbbest.a500px.net.retrofit.RestClient;
import com.dbbest.a500px.simpleDb.PhotoEntry;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("RestrictedApi")
public class ExecuteService extends IntentService {


    public ExecuteService() {
        super("ExecuteService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final ResultReceiver receiver = intent.getParcelableExtra(Constant.RECEIVER);
            Bundle bundle = new Bundle();
            receiver.send(Constant.STATUS_RUNNING, Bundle.EMPTY);
            try {
                RestClient restClient = RestClient.getInstance();
                int count = intent.getIntExtra(Constant.COUNT, 1);
                int page = intent.getIntExtra(Constant.PAGE, 1);
                String size = intent.getStringExtra(Constant.IMAGE_SIZE_FLAG);

                ListPhotos results = restClient.getPhotos(BuildConfig.CONSUMER_KEY, page,
                        count, size);

                List<ContentValues> photosToSave = new ArrayList<>();

                for (Photo photo : results.getPhotos()) {
                    PhotoModel photoModel = new PhotoModel(photo);
                    photosToSave.add(photoModel.values());
                }

                if (!photosToSave.isEmpty()) {
                    ContentValues[] cvArray = new ContentValues[photosToSave.size()];
                    photosToSave.toArray(cvArray);
                    App.instance().getContentResolver().bulkInsert(PhotoEntry.URI, cvArray);
                }


                receiver.send(Constant.STATUS_SUCCESSFUL, Bundle.EMPTY);

            } catch (Exception e) {
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(Constant.STATUS_FAILED, bundle);
            }
        }
    }
}
