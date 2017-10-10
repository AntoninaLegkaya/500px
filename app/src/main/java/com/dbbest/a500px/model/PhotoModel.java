package com.dbbest.a500px.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.dbbest.a500px.net.responce.Image;
import com.dbbest.a500px.net.responce.Photo;
import com.dbbest.a500px.data.DBUtil;
import com.dbbest.a500px.data.PhotoEntry;

import java.util.List;

public class PhotoModel {

    private final int userId;
    private int id;
    private String previewUrl;
    private String photoUrl;
    private String name;

    public PhotoModel(Cursor cursor) {
        id = DBUtil.getInteger(cursor, PhotoEntry._ID);
        userId = DBUtil.getInteger(cursor, PhotoEntry.COLUMN_USER_ID);
        previewUrl = DBUtil.getString(cursor, PhotoEntry.COLUMN_PREVIEW_URL);
        photoUrl = DBUtil.getString(cursor, PhotoEntry.COLUMN_PHOTO_URL);
        name = DBUtil.getString(cursor, PhotoEntry.COLUMN_USER_NAME);
    }

    public PhotoModel(Photo photo) {
        this.id = photo.getId();
        this.userId = photo.getUserId();
        List<Image> images = photo.getImages();
        if (!images.isEmpty() && images.size() == 2) {
            this.previewUrl = images.get(0).getHttpsUrl();
            this.photoUrl = images.get(1).getHttpsUrl();
        }
        this.name = photo.getUser().getFullName();
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idPhoto) {
        this.id = idPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        this.name = s;
    }

    public ContentValues values() {
        ContentValues values = new ContentValues();
        values.put(PhotoEntry._ID, id);
        values.put(PhotoEntry.COLUMN_USER_ID, userId);
        values.put(PhotoEntry.COLUMN_USER_NAME, name);
        values.put(PhotoEntry.COLUMN_PREVIEW_URL, previewUrl);
        values.put(PhotoEntry.COLUMN_PHOTO_URL, photoUrl);
        return values;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

}
