package com.dbbest.a500px.net.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;

import com.dbbest.a500px.App;
import com.dbbest.a500px.R;
import com.dbbest.a500px.db.model.PhotoModel;
import com.dbbest.a500px.db.model.UserModel;
import com.dbbest.a500px.net.model.ListPhotos;
import com.dbbest.a500px.net.model.Photo;
import com.dbbest.a500px.net.retrofit.RestClient;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.app.DownloadManager.STATUS_FAILED;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;

public class ExecuteService extends IntentService {


    public ExecuteService() {
        super("ExecuteService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final ResultReceiver receiver = intent.getParcelableExtra("receiver");
            String command = intent.getStringExtra("command");
            Bundle bundle = new Bundle();
            if (command.equals("execute")) {
                receiver.send(STATUS_RUNNING, Bundle.EMPTY);
                try {
                    RestClient restClient = new RestClient();
                    ListPhotos results = (ListPhotos) restClient.getPhotos(getApplicationContext().getString(R.string.px_consumer_key), 1, 3);

                    List<UserModel> userToSave = new ArrayList<>();
                    List<PhotoModel> photosToSave = new ArrayList<>();

                    for (Photo photo : results.getPhotos()) {
                        PhotoModel photoModel = new PhotoModel(photo);
                        UserModel userModel = new UserModel(photo.getUser());
                        userToSave.add(userModel);
                        photosToSave.add(photoModel);
                        Timber.i("Photo id: %d", photoModel.getId());
                        Timber.i("User name: %s", userModel.getName());
                    }

//                    if (offset == 0) {
//                    App.instance().processor().repository().photo().removeAll();
//                    App.processor().repository().user().removeAll();
//                    }

                    App.processor().repository().photo().bulk(photosToSave);
//                    App.processor().repository().user().bulk(userToSave);

                    receiver.send(STATUS_SUCCESSFUL, Bundle.EMPTY);
                } catch (Exception e) {
                    bundle.putString(Intent.EXTRA_TEXT, e.toString());
                    receiver.send(STATUS_FAILED, bundle);
                }
            }
        }
    }
}
