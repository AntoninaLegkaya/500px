package com.dbbest.a500px.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dbbest.a500px.R;
import com.dbbest.a500px.adapter.PhotoAdapter;
import com.dbbest.a500px.net.service.ExecuteResultReceiver;
import com.dbbest.a500px.net.service.ExecuteService;
import com.dbbest.a500px.simpleDb.PhotoEntry;

import timber.log.Timber;


public class PhotosGalleryActivity extends AppCompatActivity implements ExecuteResultReceiver.Receiver, LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, PhotoAdapter.PreviewCallback {

    public static final int LOADER_PHOTO = 0;
    private ExecuteResultReceiver receiver;
    private TextView infoView;
    private int page;
    private PhotoAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        receiver = new ExecuteResultReceiver(new Handler());
        receiver.setReceiver(this);
        getContentResolver().delete(PhotoEntry.URI, null, null);
        Timber.plant(new Timber.DebugTree());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        infoView = (TextView) findViewById(R.id.text_info);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        page = page + 1;
        startService(ExecuteService.startService(this, receiver, page));
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recycler, int dx, int dy) {
                super.onScrolled(recycler, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastItemPosition >= visibleItemCount - totalItemCount / 2) {
                    page = page + 1;
                    swipeRefreshLayout.setRefreshing(true);
                    startService(ExecuteService.startService(PhotosGalleryActivity.this, receiver,
                            page));
                }
            }
        });
        adapter = new PhotoAdapter(this);
        recyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(LOADER_PHOTO, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(0x01, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        receiver.setReceiver(null);
    }

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
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case ExecuteService.STATUS_RUNNING:
                break;
            case ExecuteService.STATUS_SUCCESSFUL:
                infoView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                break;
            case ExecuteService.STATUS_FAILED:
                Toast.makeText(PhotosGalleryActivity.this, "Some Error was happened! ", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = page + 1;
        startService(ExecuteService.startService(this, receiver, page));
    }

    @Override
    public void photoSelected(String name, String url) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra(PhotoActivity.PHOTOGRAPH_NAME, name);
        intent.putExtra(PhotoActivity.PHOTO_URL, url);
        startActivity(intent);
    }

    private void getPhotosFromBd(Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                infoView.setVisibility(View.GONE);
                adapter.swapCursor(cursor);
            } else {
                infoView.setVisibility(View.VISIBLE);
                infoView.setText(R.string.error_swap_cursor);
            }
        }
    }
}


