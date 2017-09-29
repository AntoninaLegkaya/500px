package com.dbbest.a500px.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dbbest.a500px.App;
import com.dbbest.a500px.R;
import com.dbbest.a500px.adapter.CardPhotoAdapter;
import com.dbbest.a500px.db.ProviderDefinition;
import com.dbbest.a500px.net.service.ExecuteResultReceiver;
import com.dbbest.a500px.net.service.ExecuteService;

import static android.app.DownloadManager.STATUS_FAILED;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;

public class PhotosGalleryActivity extends AppCompatActivity implements
        ExecuteResultReceiver.Receiver {

    private CardPhotoAdapter adapter;
    private static final int VISIBLE_THRESHOLD = 2;
    private ExecuteResultReceiver receiver;
    private TextView infoView;
    private RelativeLayout loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        receiver = new ExecuteResultReceiver(new Handler());
        receiver.setReceiver(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        infoView = (TextView) findViewById(R.id.text_info);
        loadingView = (RelativeLayout) findViewById(R.id.loadingView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager.getItemCount() <= linearLayoutManager.findLastVisibleItemPosition() + VISIBLE_THRESHOLD) {
                    final Intent intent = new Intent(Intent.ACTION_SYNC, null, App.instance(), ExecuteService.class);
                    intent.putExtra("receiver", receiver);
                    intent.putExtra("command", "execute");
                    intent.putExtra("page", 2);
                    intent.putExtra("count", 3);
                    showSpinner();
//                    startService(intent);

                }
            }
        });
        adapter = new CardPhotoAdapter(getPhotos());
        recyclerView.setAdapter(adapter);
    }

    private Cursor getPhotos() {
        Cursor cursor = App.instance().getContentResolver().query(ProviderDefinition.PhotoEntry.URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            infoView.setVisibility(View.GONE);
            return cursor;
        } else {
            infoView.setVisibility(View.VISIBLE);
            infoView.setText(R.string.error_swap_cursor);
        }
        return null;
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
                Toast.makeText(PhotosGalleryActivity.this,
                        "Success!" , Toast.LENGTH_LONG)
                        .show();
                break;
            case STATUS_FAILED:
                hideSpinner();
                Toast.makeText(PhotosGalleryActivity.this,
                        "Some Error was happened! ", Toast.LENGTH_LONG)
                        .show();
                break;
            default:
                break;
        }


    }

    private void showSpinner() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideSpinner() {
        loadingView.setVisibility(View.GONE);
    }

}
