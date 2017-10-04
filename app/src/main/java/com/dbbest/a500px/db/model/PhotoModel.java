package com.dbbest.a500px.db.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.dbbest.a500px.net.responce.Photo;
import com.dbbest.a500px.simpleDb.DBUtil;
import com.dbbest.a500px.simpleDb.PhotoEntry;

public class PhotoModel {

    private Integer id;
    private Integer userId;
    private String imageUrl;
    private String name;
    private String avDefUri;
    private String avLargeUri;
    private String avSmallUri;
    private String avTinyUri;

    public PhotoModel(Cursor cursor) {
        id = DBUtil.getInteger(cursor, PhotoEntry._ID);
        userId = DBUtil.getInteger(cursor, PhotoEntry.COLUMN_USER_ID);
        imageUrl = DBUtil.getString(cursor, PhotoEntry.COLUMN_IMAGE_URL);
        name = DBUtil.getString(cursor, PhotoEntry.COLUMN_USER_NAME);
        avDefUri = DBUtil.getString(cursor, PhotoEntry.COLUMN_URL_DEFAULT);
        avLargeUri = DBUtil.getString(cursor, PhotoEntry.COLUMN_URL_LARGE);
        avSmallUri = DBUtil.getString(cursor, PhotoEntry.COLUMN_URL_SMALL);
        avTinyUri = DBUtil.getString(cursor, PhotoEntry.COLUMN_URL_TINY);
    }

    public PhotoModel(Photo photo) {
        this.id = photo.getId();
        this.userId = photo.getUserId();
        this.imageUrl = photo.getImageUrl();
        this.name = photo.getUser().getFullname();
        this.avDefUri = (photo.getUser()).getAvatars().getDefault().getHttps();
        this.avLargeUri = (photo.getUser()).getAvatars().getLarge().getHttps();
        this.avSmallUri = (photo.getUser()).getAvatars().getSmall().getHttps();
        this.avTinyUri = (photo.getUser()).getAvatars().getTiny().getHttps();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvDefUri() {
        return avDefUri;
    }

    public void setAvDefUri(String avDefUri) {
        this.avDefUri = avDefUri;
    }

    public String getAvLargeUri() {
        return avLargeUri;
    }

    public void setAvLargeUri(String avLargeUri) {
        this.avLargeUri = avLargeUri;
    }

    public String getAvSmallUri() {
        return avSmallUri;
    }

    public void setAvSmallUri(String avSmallUri) {
        this.avSmallUri = avSmallUri;
    }

    public String getAvTinyUri() {
        return avTinyUri;
    }

    public void setAvTinyUri(String avTinyUri) {
        this.avTinyUri = avTinyUri;
    }

    public ContentValues values() {
        ContentValues values = new ContentValues();
        values.put(PhotoEntry._ID, id);
        values.put(PhotoEntry.COLUMN_USER_ID, userId);
        values.put(PhotoEntry.COLUMN_USER_NAME, name);
        values.put(PhotoEntry.COLUMN_URL_DEFAULT, avDefUri);
        values.put(PhotoEntry.COLUMN_URL_LARGE, avLargeUri);
        values.put(PhotoEntry.COLUMN_URL_SMALL, avSmallUri);
        values.put(PhotoEntry.COLUMN_URL_TINY, avTinyUri);
        return values;
    }

}
