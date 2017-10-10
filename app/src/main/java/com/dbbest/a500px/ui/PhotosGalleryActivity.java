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
import com.dbbest.a500px.data.PhotoEntry;
import com.dbbest.a500px.net.service.ExecuteResultReceiver;
import com.dbbest.a500px.net.service.ExecuteService;

import timber.log.Timber;


public class PhotosGalleryActivity extends AppCompatActivity implements ExecuteResultReceiver.Receiver, LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, PhotoAdapter.PreviewCallback {

    public static final int LOADER_PHOTO = 0;
    public static final int DOWNLOAD_LIMIT = 50;
    private static final int SPAN_COUNT = 3;
    private ExecuteResultReceiver receiver;
    private TextView infoView;
    private int page;
    private PhotoAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        page = 1;
        receiver = new ExecuteResultReceiver(new Handler());

        getContentResolver().delete(PhotoEntry.URI, null, null);
        startService(ExecuteService.startService(this, receiver, page));
        Timber.plant(new Timber.DebugTree());

        page = 1;
        startService(ExecuteService.startService(this, receiver, page));

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
                if (!loading && (lastItemPosition >= totalItemCount - DOWNLOAD_LIMIT / 2)) {
                    page = page + 1;
                    startService(ExecuteService.startService(PhotosGalleryActivity.this, receiver,
                            page));
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
        receiver.setReceiver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case ExecuteService.STATUS_RUNNING:
                loading = true;
                swipeRefreshLayout.setRefreshing(true);
                break;
            case ExecuteService.STATUS_SUCCESSFUL:
                infoView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                loading = false;
                break;
            case ExecuteService.STATUS_FAILED:
                loading = false;
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(PhotosGalleryActivity.this, "Some Error was happened! ", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        getContentResolver().delete(PhotoEntry.URI, null, null);
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

}


