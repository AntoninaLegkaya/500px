package com.dbbest.a500px.simpleDb;

import android.net.Uri;


public class PhotoEntry  {


    public static final String TABLE_NAME = "photo";
    public static final String _ID = "_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_PREVIEW_URL = "preview_url";
    public static final String COLUMN_PHOTO_URL = "photo_url";

    public static final String SQL_CREATE_PHOTO_TABLE =
            "CREATE TABLE photo (" + PhotoEntry.COLUMN_USER_ID + " INTEGER," +
                    PhotoEntry.COLUMN_USER_NAME + " TEXT," +
                    PhotoEntry.COLUMN_PREVIEW_URL + " TEXT," +
                    PhotoEntry.COLUMN_PHOTO_URL + " TEXT," +
                    PhotoEntry._ID + " TEXT PRIMARY KEY ON CONFLICT REPLACE)";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + PhotoContentProvider.AUTHORITY);

    public static final Uri URI = buildUri(TABLE_NAME);

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }


}
