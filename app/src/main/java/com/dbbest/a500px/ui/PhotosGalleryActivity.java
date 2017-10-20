package com.dbbest.a500px.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import com.dbbest.a500px.data.PhotoEntry;
import com.dbbest.a500px.net.service.BindingExecuteService;
import com.dbbest.a500px.net.service.Client;
import com.dbbest.a500px.net.service.Producer;

import timber.log.Timber;


public class PhotosGalleryActivity extends AppCompatActivity implements Client, LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, PhotoAdapter.PreviewCallback {

    public static final int LOADER_PHOTO = 0;
    public static final int DOWNLOAD_LIMIT = 50;
    private static final int SPAN_COUNT = 3;
    private final ServiceConnection connection = new ActiveConnection();
    private TextView infoView;
    private PhotoAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Producer producer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getContentResolver().delete(PhotoEntry.URI, null, null);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        infoView = (TextView) findViewById(R.id.text_info);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);


        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recycler, int dx, int dy) {
                super.onScrolled(recycler, dx, dy);
                int totalItemCount = recycler.getLayoutManager().getItemCount();
                int lastItemPosition = ((GridLayoutManager) recycler.getLayoutManager()).findLastVisibleItemPosition();
                if (!producer.isLoading() && (lastItemPosition >= totalItemCount - DOWNLOAD_LIMIT / 2)) {
                    producer.executeService();
                }
            }
        });
        adapter = new PhotoAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().restartLoader(LOADER_PHOTO, null, this);
        bindService(new Intent(PhotosGalleryActivity.this, BindingExecuteService.class), connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
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
        if (data != null) {
            if (data.moveToFirst()) {
                infoView.setVisibility(View.GONE);
                adapter.swapCursor(data);
            } else {
                infoView.setVisibility(View.VISIBLE);
                infoView.setText(R.string.error_swap_cursor);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onRefresh() {
        getContentResolver().delete(PhotoEntry.URI, null, null);
        producer.refreshData();
    }

    @Override
    public void photoSelected(String name, String url) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra(PhotoActivity.PHOTOGRAPH_NAME, name);
        intent.putExtra(PhotoActivity.PHOTO_URL, url);
        startActivity(intent);
    }


    @Override
    public void onRequestStatusRunning() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRequestStatusSuccess() {
        infoView.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRequestStatusFail() {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(PhotosGalleryActivity.this, "Some Error was happened! ", Toast.LENGTH_LONG).show();
    }

    private class ActiveConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Timber.i("Service is binding....start Connection");
            producer = (Producer) service;
            producer.registerHandler(new Handler());
            producer.addClient(PhotosGalleryActivity.this);
            producer.executeService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (producer != null) {
                producer.removeClient(PhotosGalleryActivity.this);
                producer = null;
                Timber.i("Service ....Stop Connection");
            }
        }
    }

}


