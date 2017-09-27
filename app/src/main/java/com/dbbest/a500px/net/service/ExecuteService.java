package com.dbbest.a500px.net.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;

import com.dbbest.a500px.R;
import com.dbbest.a500px.net.retrofit.RestClient;

import static android.app.DownloadManager.STATUS_FAILED;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;
@SuppressWarnings("")
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
                    Object results = restClient.getPhotos(getApplicationContext().getString(R.string.px_consumer_key), 1, 3);
                    bundle.putString("results", results.toString());
                    receiver.send(STATUS_SUCCESSFUL, bundle);
                } catch (Exception e) {
                    bundle.putString(Intent.EXTRA_TEXT, e.toString());
                    receiver.send(STATUS_FAILED, bundle);
                }

            }
        }
    }
}
