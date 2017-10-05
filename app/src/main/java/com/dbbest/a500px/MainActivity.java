package com.dbbest.a500px;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dbbest.a500px.net.service.ExecuteResultReceiver;
import com.dbbest.a500px.net.service.ExecuteService;
import com.dbbest.a500px.simpleDb.PhotoEntry;
import com.dbbest.a500px.ui.PhotosGalleryActivity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import timber.log.Timber;


@SuppressFBWarnings(value = "SIC_INNER_SHOULD_BE_STATIC_ANON")
public class MainActivity extends AppCompatActivity implements ExecuteResultReceiver.Receiver {


    private static final int FADE_DELAY = 5000;
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                moveToGallery();
            }
            return true;
        }
    });
    private ExecuteResultReceiver receiver;
    private SharedPreferences preferences;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button clearBtn = (Button) findViewById(R.id.login_btn);


        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.instance().getContentResolver().delete(PhotoEntry.URI, null, null);
            }
        });
        preferences = App.instance().getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        receiver = new ExecuteResultReceiver(new Handler());
        receiver.setReceiver(this);
        getPhotosFromBd();
    }

    @Override
    protected void onPause() {
        super.onPause();
        receiver.setReceiver(null);
    }

    public void moveToGallery() {
        Intent intent = new Intent(this, PhotosGalleryActivity.class);
        startActivity(intent);
        finish();
    }

    private void startService() {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, App.instance(), ExecuteService.class);
        intent.putExtra(Constant.RECEIVER, receiver);
        intent.putExtra(Constant.COMMAND, "execute");
        intent.putExtra(Constant.IMAGE_SIZE_FLAG, Constant.IMAGE_SIZE);
        intent.putExtra(Constant.PAGE, page);
        intent.putExtra(Constant.COUNT, Constant.DOWNLOAD_LIMIT);
        startService(intent);
    }


    private void getPhotosFromBd() {
        Cursor cursor = App.instance().getContentResolver().query(PhotoEntry.URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToLast();
            page = (int) preferences.getLong(Constant.CURRENT_PAGE, 1);
            cursor.close();
            handler.sendEmptyMessageDelayed(1, FADE_DELAY);
        } else {
            Timber.i("DB empty start Service");
            startService();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case Constant.STATUS_RUNNING:
                break;
            case Constant.STATUS_SUCCESSFUL:
                Timber.i("Successful get data from page: %d", page);
                preferences.edit().putLong(Constant.CURRENT_PAGE, page).apply();
                moveToGallery();
                break;
            case Constant.STATUS_FAILED:
                Timber.i("Fail get data from service");
                break;
            default:
                break;
        }
    }
}
