package com.dbbest.a500px;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dbbest.a500px.net.service.ExecuteResultReceiver;
import com.dbbest.a500px.net.service.ExecuteService;
import com.dbbest.a500px.ui.PhotosGalleryActivity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static android.app.DownloadManager.STATUS_FAILED;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;

@SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
public class MainActivity extends AppCompatActivity implements
        ExecuteResultReceiver.Receiver {

    private ExecuteResultReceiver receiver;
    private RelativeLayout loadingView;
    private RelativeLayout relativeLayout1;

    private Button loginBtn;


    @Override
    protected void onStop() {
        super.onStop();
        receiver.setReceiver(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new ExecuteResultReceiver(new Handler());
        receiver.setReceiver(this);

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, this, ExecuteService.class);
        intent.putExtra("receiver", receiver);
        intent.putExtra("command", "execute");

        loginBtn = (Button) findViewById(R.id.login_btn);
        loadingView = (RelativeLayout) findViewById(R.id.loadingView);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSpinner();
                startService(intent);
            }
        });
    }

    private void showSpinner() {
        loginBtn.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        relativeLayout1.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    private void hideSpinner() {
        loginBtn.setEnabled(true);
        loadingView.setVisibility(View.GONE);
        relativeLayout1.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    }

    public void moveToGallery() {
        Intent intent = new Intent(this, PhotosGalleryActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        hideSpinner();
        switch (resultCode) {
            case STATUS_RUNNING:
                showSpinner();
                break;
            case STATUS_SUCCESSFUL:
                hideSpinner();
                Toast.makeText(MainActivity.this,
                        ",You have got Data  count photo: "+resultData.getInt("results"), Toast.LENGTH_LONG)
                        .show();
                moveToGallery();
                break;
            case STATUS_FAILED:
                hideSpinner();
                Toast.makeText(MainActivity.this,
                        "Some Error was happened! ", Toast.LENGTH_LONG)
                        .show();
                break;
            default:
                break;
        }


    }
}
