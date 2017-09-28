package com.dbbest.a500px.ui;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dbbest.a500px.App;
import com.dbbest.a500px.R;
import com.dbbest.a500px.db.ProviderDefinition;
import com.dbbest.a500px.db.model.PhotoModel;

import timber.log.Timber;

public class PhotosGalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        try {
            ContentResolver resolver = App.instance().getContentResolver();

            Cursor cursor = resolver.query(ProviderDefinition.PhotoEntry.URI, null, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    Timber.i("Cursor count = 0");
                } else {
                    Timber.i("Cursor has data");
                    parseCursor(cursor);
                }
            } else {
                Timber.i("Cursor has no data");
            }
        } catch (Exception e) {
            Timber.e(e, "query error");
        }

    }

    private void parseCursor(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            do {
                PhotoModel model = new PhotoModel(cursor);
                Timber.i("Data from Cursor: user_id: %d " + model.getUser_id());
            } while (cursor.moveToNext());

        } else {
            Timber.i("Cursor Empty");
        }


    }

}
