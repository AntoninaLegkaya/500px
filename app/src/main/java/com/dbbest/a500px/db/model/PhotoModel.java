package com.dbbest.a500px.db.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.dbbest.a500px.net.responce.Image;
import com.dbbest.a500px.net.responce.Photo;
import com.dbbest.a500px.simpleDb.DBUtil;
import com.dbbest.a500px.simpleDb.PhotoEntry;

import java.util.List;

public class PhotoModel {

    private Integer id;
    private Integer userId;
    private String previewUrl;
    private String photoUrl;
    private String name;
    private String avDefUri;
    private String avLargeUri;
    private String avSmallUri;
    private String avTinyUri;

    public PhotoModel(Cursor cursor) {
        id = DBUtil.getInteger(cursor, PhotoEntry._ID);
        userId = DBUtil.getInteger(cursor, PhotoEntry.COLUMN_USER_ID);
        previewUrl = DBUtil.getString(cursor, PhotoEntry.COLUMN_PREVIEW_URL);
        photoUrl = DBUtil.getString(cursor, PhotoEntry.COLUMN_PHOTO_URL);
        name = DBUtil.getString(cursor, PhotoEntry.COLUMN_USER_NAME);
        avDefUri = DBUtil.getString(cursor, PhotoEntry.COLUMN_URL_DEFAULT);
        avLargeUri = DBUtil.getString(cursor, PhotoEntry.COLUMN_URL_LARGE);
        avSmallUri = DBUtil.getString(cursor, PhotoEntry.COLUMN_URL_SMALL);
        avTinyUri = DBUtil.getString(cursor, PhotoEntry.COLUMN_URL_TINY);
    }

    public PhotoModel(Photo photo) {
        this.id = photo.getId();
        this.userId = photo.getUserId();
        List<Image> images = photo.getImages();
        if (!images.isEmpty() && images.size() == 2) {
            this.previewUrl = images.get(0).getHttpsUrl();
            this.photoUrl = images.get(1).getHttpsUrl();
        }
        this.name = photo.getUser().getFullname();
        this.avDefUri = (photo.getUser()).getAvatars().getDefault().getHttps();
        this.avLargeUri = (photo.getUser()).getAvatars().getLarge().getHttps();
        this.avSmallUri = (photo.getUser()).getAvatars().getSmall().getHttps();
        this.avTinyUri = (photo.getUser()).getAvatars().getTiny().getHttps();
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
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
        values.put(PhotoEntry.COLUMN_PREVIEW_URL, previewUrl);
        values.put(PhotoEntry.COLUMN_PHOTO_URL, photoUrl);
        values.put(PhotoEntry.COLUMN_URL_DEFAULT, avDefUri);
        values.put(PhotoEntry.COLUMN_URL_LARGE, avLargeUri);
        values.put(PhotoEntry.COLUMN_URL_SMALL, avSmallUri);
        values.put(PhotoEntry.COLUMN_URL_TINY, avTinyUri);
        return values;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
