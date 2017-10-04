package com.dbbest.a500px.simpleDb;

import android.database.Cursor;
import android.support.annotation.Nullable;

public final class DBUtil {

    private DBUtil() {
    }

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
