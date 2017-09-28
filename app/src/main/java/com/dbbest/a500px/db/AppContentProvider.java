package com.dbbest.a500px.db;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.simonvt.schematic.utils.SelectionBuilder;

import java.util.ArrayList;

public class AppContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.dbbest.a500px.provider.AppContentProvider";
    private static final int PHOTO_URI = 0;
    private static final int USER_URI = 1;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, "photo", PHOTO_URI);
        MATCHER.addURI(AUTHORITY, "user", USER_URI);
    }

    private SQLiteOpenHelper database;

    @Override
    public boolean onCreate() {
        database = ApplicationDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = database.getReadableDatabase();
        switch (MATCHER.match(uri)) {
            case PHOTO_URI: {
                SelectionBuilder builder = getBuilder("PhotoEntry");
                String table = "photo";
                final String groupBy = null;
                final String having = null;
                final String limit = null;
                Cursor cursor = builder.table(table)
                        .where(selection, selectionArgs)
                        .query(db, projection, groupBy, having, sortOrder, limit);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            case USER_URI: {
                SelectionBuilder builder = getBuilder("UserEntry");
                String table = "user";
                final String groupBy = null;
                final String having = null;
                final String limit = null;
                Cursor cursor = builder.table(table)
                        .where(selection, selectionArgs)
                        .query(db, projection, groupBy, having, sortOrder, limit);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }

    }

    private SelectionBuilder getBuilder(String table) {
        SelectionBuilder builder = new SelectionBuilder();
        return builder;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {

            case PHOTO_URI: {
                return "vnd.hmni.item/photo";
            }
            case USER_URI: {
                return "vnd.hmni.item/user";
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = database.getReadableDatabase();
        switch (MATCHER.match(uri)) {

            case PHOTO_URI: {
                final long id = db.insertOrThrow("photo", null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
            case USER_URI: {

                final long id = db.insertOrThrow("user", null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }

        }
    }

    private long[] insertValues(SQLiteDatabase db, String table, ContentValues[] values) {
        long[] ids = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            ContentValues cv = values[i];
            db.insertOrThrow(table, null, cv);
        }
        return ids;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            switch (MATCHER.match(uri)) {
                case PHOTO_URI: {
                    long[] ids = insertValues(db, "photo", values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
                case USER_URI: {
                    long[] ids = insertValues(db, "user", values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return values.length;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> ops) throws OperationApplicationException {
        ContentProviderResult[] results;
        final SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            results = super.applyBatch(ops);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return results;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = database.getReadableDatabase();
        switch (MATCHER.match(uri)) {
            case PHOTO_URI: {
                SelectionBuilder builder = getBuilder("photo");
                builder.where(selection, selectionArgs);
                final int count = builder
                        .table("photo")
                        .delete(db);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;

            }
            case USER_URI: {
                SelectionBuilder builder = getBuilder("user");
                builder.where(selection, selectionArgs);
                final int count = builder
                        .table("user")
                        .delete(db);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;

            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }

        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = database.getReadableDatabase();
        switch (MATCHER.match(uri)) {
            case PHOTO_URI: {
                SelectionBuilder builder = getBuilder("photo");
                builder.where(selection, selectionArgs);
                final int count = builder
                        .table("photo")
                        .update(db, values);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;

            }
            case USER_URI: {
                SelectionBuilder builder = getBuilder("user");
                builder.where(selection, selectionArgs);
                final int count = builder
                        .table("user")
                        .update(db, values);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;

            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }

        }
    }
}
