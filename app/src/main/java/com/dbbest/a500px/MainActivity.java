package com.dbbest.a500px;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dbbest.a500px.db.ProviderDefinition;
import com.dbbest.a500px.net.service.ExecuteResultReceiver;
import com.dbbest.a500px.net.service.ExecuteService;
import com.dbbest.a500px.ui.PhotosGalleryActivity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static android.app.DownloadManager.STATUS_FAILED;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;

@SuppressFBWarnings(value = "SIC_INNER_SHOULD_BE_STATIC_ANON")
public class MainActivity extends AppCompatActivity implements ExecuteResultReceiver.Receiver {

    private static final int DOWNLOAD_LIMIT = 3;
    private static final int IMAGE_SIZE = 3;
    private ExecuteResultReceiver receiver;
    private int page = 0;
    private View splashView;


    @Override
    protected void onStart() {
        super.onStart();
        receiver = new ExecuteResultReceiver(new Handler());
        receiver.setReceiver(this);
        splashView.animate()
                .alpha(1f)
                .setDuration(300)
                .start();
        isEmptyCursor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        receiver.setReceiver(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button clearBtn = (Button) findViewById(R.id.login_btn);
        splashView = findViewById(R.id.image_logo);
        splashView.setAlpha(0f);


        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.processor().repository().photo().removeAll();
                App.processor().repository().user().removeAll();
                App.processor().repository().avatars().removeAll();
            }
        });
    }

    public void moveToGallery() {
        Intent intent = new Intent(this, PhotosGalleryActivity.class);
        intent.putExtra("page", page);
        startActivity(intent);
        finish();
    }

    private void buildServiceCommand() {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, App.instance(), ExecuteService.class);
        intent.putExtra("receiver", receiver);
        intent.putExtra("command", "execute");
        intent.putExtra("image_size", IMAGE_SIZE);
        intent.putExtra("page", page);
        intent.putExtra("count", DOWNLOAD_LIMIT);
        page = page + 1;
        startService(intent);
    }


    private void isEmptyCursor() {
        Cursor cursor = App.instance().getContentResolver().query(ProviderDefinition.PhotoEntry.URI, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                cursor.close();
                moveToGallery();
            } else {
                buildServiceCommand();
            }
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case STATUS_RUNNING:
                break;
            case STATUS_SUCCESSFUL:
                isEmptyCursor();
                break;
            case STATUS_FAILED:
                break;
            default:
                break;
        }


    }

}
