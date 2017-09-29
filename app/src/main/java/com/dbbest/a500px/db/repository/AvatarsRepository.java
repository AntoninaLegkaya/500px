package com.dbbest.a500px.db.repository;

import android.database.Cursor;
import android.net.Uri;

import com.dbbest.a500px.db.ProviderDefinition;
import com.dbbest.a500px.db.model.AvatarsModel;

import java.util.ArrayList;
import java.util.List;

public class AvatarsRepository extends ContentRepository<AvatarsModel> {
    @Override
    public AvatarsModel get(Integer id) {
        Cursor cursor = getEntryAsCursor(id);
        if (cursor != null && cursor.moveToFirst()) {
            AvatarsModel user = new AvatarsModel(cursor);
            closeCursor(cursor);
            return user;
        }
        closeCursor(cursor);
        return null;
    }

    @Override
    public List<AvatarsModel> select(String where, Object... args) {
        Cursor cursor = getEntryListAsCursor(where, args);
        List<AvatarsModel> items = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                items.add(new AvatarsModel(cursor));
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return items;
    }

    @Override
    Uri uri() {
        return ProviderDefinition.AvatarsEntry.URI;
    }
}
