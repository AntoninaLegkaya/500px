package com.dbbest.a500px.db.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.dbbest.a500px.db.DBUtil;
import com.dbbest.a500px.db.repository.ContentEntry;
import com.dbbest.a500px.db.repository.PhotoColumns;
import com.dbbest.a500px.net.model.Photo;

public class PhotoModel implements ContentEntry {

    private final Integer id;
    private final Integer user_id;

    public PhotoModel(Cursor cursor) {
        id = DBUtil.getInteger(cursor, PhotoColumns.ID);
        user_id = DBUtil.getInteger(cursor, PhotoColumns.USER_ID);
    }

    public PhotoModel(Photo photo) {
        this.id = photo.getId();
        this.user_id = photo.getUserId();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    @Override
    public ContentValues values() {
        ContentValues values = new ContentValues();
        values.put(PhotoColumns.ID, id);
        values.put(PhotoColumns.USER_ID, user_id);
        return values;
    }
}
