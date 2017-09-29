package com.dbbest.a500px.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dbbest.a500px.App;
import com.dbbest.a500px.R;
import com.dbbest.a500px.adapter.CardPhotoAdapter;
import com.dbbest.a500px.db.ProviderDefinition;

public class PhotosGalleryActivity extends AppCompatActivity {

    private CardPhotoAdapter adapter;
    private TextView infoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        infoView = (TextView) findViewById(R.id.text_info);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
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

}
