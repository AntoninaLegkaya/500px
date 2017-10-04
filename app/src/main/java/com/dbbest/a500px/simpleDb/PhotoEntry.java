package com.dbbest.a500px.simpleDb;

import android.net.Uri;
import android.provider.BaseColumns;


public class PhotoEntry implements BaseColumns {

    public static final String PATH_PHOTO = "photo";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_URL_DEFAULT = "def";
    public static final String COLUMN_URL_LARGE = "large";
    public static final String COLUMN_URL_SMALL = "small";
    public static final String COLUMN_URL_TINY = "tiny";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + PhotoContentProvider.AUTHORITY);

    public static final Uri URI = buildUri(PATH_PHOTO);

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }


}
