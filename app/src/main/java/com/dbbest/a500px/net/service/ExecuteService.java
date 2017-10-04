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

import timber.log.Timber;

@SuppressLint("RestrictedApi")
public class ExecuteService extends IntentService {


    public ExecuteService() {
        super("ExecuteService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            Timber.i("Start Service");
            final ResultReceiver receiver = intent.getParcelableExtra(Constant.RECEIVER);
            Bundle bundle = new Bundle();
                receiver.send(Constant.STATUS_RUNNING, Bundle.EMPTY);
                Timber.i("Running Service");
                try {
                    RestClient restClient = RestClient.getInstance();
                    int count = intent.getIntExtra(Constant.COUNT, 1);
                    int page = intent.getIntExtra(Constant.PAGE, 1);
                    int size = intent.getIntExtra(Constant.IMAGE_SIZE_FLAG, 2);

                    ListPhotos results = restClient.getPhotos(BuildConfig.CONSUMER_KEY, page,
                            count, size);

                    List<ContentValues> photosToSave = new ArrayList<>();

                    for (Photo photo : results.getPhotos()) {
                        PhotoModel photoModel = new PhotoModel(photo);
                        photoModel.setName(photo.getUser().getFullname());
                        photoModel.setImageUrl(photo.getImageUrl());
                        photoModel.setUserId(photo.getUserId());
                        photoModel.setAvDefUri(photo.getUser().getAvatars().getDefault().getHttps());
                        photoModel.setAvLargeUri(photo.getUser().getAvatars().getLarge().getHttps());
                        photoModel.setAvSmallUri(photo.getUser().getAvatars().getSmall().getHttps());
                        photoModel.setAvTinyUri(photo.getUser().getAvatars().getTiny().getHttps());
                        photosToSave.add(photoModel.values());
                    }

                    if (!photosToSave.isEmpty()) {
                        ContentValues[] cvArray = new ContentValues[photosToSave.size()];
                        photosToSave.toArray(cvArray);
                        int lines = App.instance().getContentResolver().bulkInsert(PhotoEntry.URI, cvArray);
                        Timber.i("End Service inserted lines: %d", lines);
                    }


                    receiver.send(Constant.STATUS_SUCCESSFUL, Bundle.EMPTY);

                } catch (Exception e) {
                    bundle.putString(Intent.EXTRA_TEXT, e.toString());
                    receiver.send(Constant.STATUS_FAILED, bundle);
                }
        }
    }
}
