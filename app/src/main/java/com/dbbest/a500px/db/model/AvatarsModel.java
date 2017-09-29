package com.dbbest.a500px.db.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.dbbest.a500px.db.DBUtil;
import com.dbbest.a500px.db.repository.AvatarsColumns;
import com.dbbest.a500px.db.repository.ContentEntry;
import com.dbbest.a500px.net.model.Avatars;

public class AvatarsModel implements ContentEntry {

    private final Integer id;
    private final String defaultUrl;
    private final String largeUrl;
    private final String smallUrl;
    private final String tinyUrl;


    public AvatarsModel(Cursor cursor) {
        id = DBUtil.getInteger(cursor, AvatarsColumns.ID);
        defaultUrl = DBUtil.getString(cursor, AvatarsColumns.URL_DEFAULT);
        largeUrl = DBUtil.getString(cursor, AvatarsColumns.URL_LARGE);
        smallUrl = DBUtil.getString(cursor, AvatarsColumns.URL_SMALL);
        tinyUrl = DBUtil.getString(cursor, AvatarsColumns.URL_TINY);
    }

    public AvatarsModel(Avatars avatar, Integer user_id) {
        this.id = user_id;
        this.defaultUrl = avatar.getDefault().getHttps();
        this.largeUrl = avatar.getLarge().getHttps();
        smallUrl = avatar.getSmall().getHttps();
        tinyUrl = avatar.getTiny().getHttps();
    }

    @Override
    public Integer getId() {
        return id;
    }


    public String getDefaultUrl() {
        return defaultUrl;
    }

    public String getLargeUrl() {
        return largeUrl;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public String getTinyUrl() {
        return tinyUrl;
    }

    @Override
    public ContentValues values() {

        ContentValues values = new ContentValues();
        values.put(AvatarsColumns.ID, id);
        values.put(AvatarsColumns.URL_DEFAULT, defaultUrl);
        values.put(AvatarsColumns.URL_LARGE, largeUrl);
        values.put(AvatarsColumns.URL_SMALL, smallUrl);
        values.put(AvatarsColumns.URL_TINY, tinyUrl);
        return values;
    }
}

