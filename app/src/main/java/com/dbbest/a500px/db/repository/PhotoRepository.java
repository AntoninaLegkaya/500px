package com.dbbest.a500px.db.repository;

import android.database.Cursor;
import android.net.Uri;

import com.dbbest.a500px.db.ProviderDefinition;
import com.dbbest.a500px.db.model.PhotoModel;

import java.util.ArrayList;
import java.util.List;

public class PhotoRepository extends ContentRepository<PhotoModel> {

    @Override
    public PhotoModel get(Integer id) {
        Cursor cursor = getEntryAsCursor(id);
        if (cursor != null && cursor.moveToFirst()) {
            PhotoModel photo = new PhotoModel(cursor);
            closeCursor(cursor);
            return photo;
        }
        closeCursor(cursor);
        return null;
    }

    @Override
    public List<PhotoModel> select(String where, Object... args) {
        Cursor cursor = getEntryListAsCursor(where, args);
        List<PhotoModel> items = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                items.add(new PhotoModel(cursor));
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return items;
    }

    @Override
    Uri uri() {
        return ProviderDefinition.PhotoEntry.URI;
    }
}
