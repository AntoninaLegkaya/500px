package com.dbbest.a500px.net.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;

import com.dbbest.a500px.App;
import com.dbbest.a500px.R;
import com.dbbest.a500px.db.model.AvatarsModel;
import com.dbbest.a500px.db.model.PhotoModel;
import com.dbbest.a500px.db.model.UserModel;
import com.dbbest.a500px.net.model.ListPhotos;
import com.dbbest.a500px.net.model.Photo;
import com.dbbest.a500px.net.retrofit.RestClient;

import java.util.ArrayList;
import java.util.List;

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
                    int count = intent.getIntExtra("count", 1);
                    int page = intent.getIntExtra("page", 1);

                    ListPhotos results = (ListPhotos) restClient.getPhotos(getApplicationContext().getString(R.string.px_consumer_key), page, count);

                    List<UserModel> userToSave = new ArrayList<>();
                    List<PhotoModel> photosToSave = new ArrayList<>();
                    List<AvatarsModel> avatarsToSave = new ArrayList<>();

                    for (Photo photo : results.getPhotos()) {
                        PhotoModel photoModel = new PhotoModel(photo);
                        UserModel userModel = new UserModel(photo.getUser());
                        userToSave.add(userModel);
                        photosToSave.add(photoModel);
                        AvatarsModel avatarsModel = new AvatarsModel(photo.getUser().getAvatars(), userModel.getId());
                        avatarsToSave.add(avatarsModel);
                    }
                    App.processor().repository().photo().bulk(photosToSave);
                    App.processor().repository().user().bulk(userToSave);
                    App.processor().repository().avatars().bulk(avatarsToSave);

                    receiver.send(STATUS_SUCCESSFUL, Bundle.EMPTY);
                } catch (Exception e) {
                    bundle.putString(Intent.EXTRA_TEXT, e.toString());
                    receiver.send(STATUS_FAILED, bundle);
                }
            }
        }
    }
}
