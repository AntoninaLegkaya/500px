package com.dbbest.a500px.simpleDb;

import android.database.Cursor;
import android.support.annotation.Nullable;

public final class DBUtil {

    private DBUtil() {
    }

    @Nullable
    public static int getInteger(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        return cursor.getInt(columnIndex);
    }

    @Nullable
    public static String getString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        return cursor.getString(columnIndex);
    }
}
