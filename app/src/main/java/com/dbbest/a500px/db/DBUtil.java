package com.dbbest.a500px.db;

import android.database.Cursor;
import android.support.annotation.Nullable;

public class DBUtil {

    public static String[] where(Object... args) {
        if (args == null || args.length == 0) {
            return new String[0];
        }

        String[] where = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            where[i] = args[i].toString();
        }
        return where;
    }
    @Nullable
    public static String getString(Cursor cursor, String columnName, String defaultValue) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        if (cursor.isNull(columnIndex)) {
            return defaultValue;
        } else {
            return cursor.getString(columnIndex);
        }
    }

    @Nullable
    public static String getString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        return cursor.getString(columnIndex);
    }
    public static int getInteger(Cursor cursor, String columnName, int defaultValue) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        if (cursor.isNull(columnIndex)) {
            return defaultValue;
        } else {
            return cursor.getInt(columnIndex);
        }
    }

    public static int getInteger(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        return cursor.getInt(columnIndex);
    }
}
