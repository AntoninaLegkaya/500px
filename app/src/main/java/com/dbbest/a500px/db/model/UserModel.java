package com.dbbest.a500px.db.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.dbbest.a500px.db.DBUtil;
import com.dbbest.a500px.db.repository.ContentEntry;
import com.dbbest.a500px.db.repository.UserColumns;
import com.dbbest.a500px.net.model.User;

public class UserModel implements ContentEntry {

    private final Integer id;
    private final String name;

    public UserModel(Cursor cursor) {
        id = DBUtil.getInteger(cursor, UserColumns.ID);
        name = DBUtil.getString(cursor, UserColumns.USER_NAME);
    }

    public UserModel(User user) {
        this.id = user.getId();
        this.name = user.getFullname();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public ContentValues values() {
        ContentValues values = new ContentValues();
        values.put(UserColumns.ID, id);
        values.put(UserColumns.USER_NAME, name);
        return values;
    }
}
