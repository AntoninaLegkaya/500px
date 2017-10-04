package com.dbbest.a500px.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dbbest.a500px.App;
import com.dbbest.a500px.Constant;
import com.dbbest.a500px.R;
import com.dbbest.a500px.adapter.PhotoAdapter;
import com.dbbest.a500px.net.service.ExecuteResultReceiver;
import com.dbbest.a500px.net.service.ExecuteService;
import com.dbbest.a500px.simpleDb.PhotoEntry;


public class PhotosGalleryActivity extends AppCompatActivity implements
        ExecuteResultReceiver.Receiver, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int VISIBLE_THRESHOLD = 4;
    private ExecuteResultReceiver receiver;
    private TextView infoView;
    private int page;
    private boolean loading = false;
    private PhotoAdapter adapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                PhotoEntry.URI,
                null,
                null,
                null,
                null
        );
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        getPhotosFromBd(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        infoView = (TextView) findViewById(R.id.text_info);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItemPosition + VISIBLE_THRESHOLD)) {
                    page = page + 1;
                    startService();
                    loading = true;
                }
            }
        });
        adapter = new PhotoAdapter();
        recyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(Constant.LOADER_PHOTO, null, this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            page = intent.getIntExtra(Constant.PAGE, 0);
        }
        receiver = new ExecuteResultReceiver(new Handler());
        receiver.setReceiver(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        receiver.setReceiver(null);
    }

    public void onResume() {
        super.onResume();
        // Restart loader so that it refreshes displayed items according to database
        getSupportLoaderManager().restartLoader(0x01, null, this);
    }

    private void startService() {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, App.instance(), ExecuteService.class);
        intent.putExtra(Constant.RECEIVER, receiver);
        intent.putExtra(Constant.IMAGE_SIZE_FLAG, Constant.IMAGE_SIZE);
        intent.putExtra(Constant.PAGE, page);
        intent.putExtra(Constant.COUNT, Constant.DOWNLOAD_LIMIT);
        startService(intent);
    }

    private void getPhotosFromBd(Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                infoView.setVisibility(View.GONE);
                adapter.swapCursor(cursor);
            } else {
                infoView.setVisibility(View.VISIBLE);
                infoView.setText(R.string.error_swap_cursor);
                startService();
            }

        }
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case Constant.STATUS_RUNNING:
                break;
            case Constant.STATUS_SUCCESSFUL:
                infoView.setVisibility(View.GONE);
                loading = false;
                adapter.notifyDataSetChanged();
                break;
            case Constant.STATUS_FAILED:
                Toast.makeText(PhotosGalleryActivity.this,
                        "Some Error was happened! ", Toast.LENGTH_LONG)
                        .show();
                loading = false;
                break;
            default:
                break;
        }


    }




}
