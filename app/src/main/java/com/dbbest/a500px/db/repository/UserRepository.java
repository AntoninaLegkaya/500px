package com.dbbest.a500px.db.repository;

import android.database.Cursor;
import android.net.Uri;

import com.dbbest.a500px.db.ProviderDefinition;
import com.dbbest.a500px.db.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserRepository extends ContentRepository<UserModel> {
    @Override
    public UserModel get(Integer id) {
        Cursor cursor = getEntryAsCursor(id);
        if (cursor != null && cursor.moveToFirst()) {
            UserModel user = new UserModel(cursor);
            closeCursor(cursor);
            return user;
        }
        closeCursor(cursor);
        return null;
    }

    @Override
    public List<UserModel> select(String where, Object... args) {
        Cursor cursor = getEntryListAsCursor(where, args);
        List<UserModel> items = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                items.add(new UserModel(cursor));
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return items;
    }

    @Override
    Uri uri() {
        return ProviderDefinition.UserEntry.URI;
    }
}
