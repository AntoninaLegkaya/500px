package com.dbbest.a500px.db.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.dbbest.a500px.db.DBUtil;
import com.dbbest.a500px.db.repository.ContentEntry;
import com.dbbest.a500px.db.repository.PhotoColumns;
import com.dbbest.a500px.net.model.Photo;

public class PhotoModel implements ContentEntry {

    private final Integer id;
    private final Integer userId;
    private final String imageUrl;

    public PhotoModel(Cursor cursor) {
        id = DBUtil.getInteger(cursor, PhotoColumns.ID);
        userId = DBUtil.getInteger(cursor, PhotoColumns.USER_ID);
        imageUrl = DBUtil.getString(cursor, PhotoColumns.IMAGE_URL);
    }

    public PhotoModel(Photo photo) {
        this.id = photo.getId();
        this.userId = photo.getUserId();
        this.imageUrl = photo.getImageUrl();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public ContentValues values() {
        ContentValues values = new ContentValues();
        values.put(PhotoColumns.ID, id);
        values.put(PhotoColumns.USER_ID, userId);
        values.put(PhotoColumns.IMAGE_URL, imageUrl);
        return values;
    }

}
