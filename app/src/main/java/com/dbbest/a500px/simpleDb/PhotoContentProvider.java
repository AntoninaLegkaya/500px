package com.dbbest.a500px.simpleDb;

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

import com.dbbest.a500px.BuildConfig;

import java.util.ArrayList;


public class PhotoContentProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.PhotoContentProvider";
    private static final int PHOTO_ENTRY_URI = 0;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, PhotoEntry.TABLE_NAME, PHOTO_ENTRY_URI);
    }

    private SQLiteOpenHelper database;

    @Override
    public boolean onCreate() {
        database = new PhotoDatabase(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase db = database.getReadableDatabase();
        if (MATCHER.match(uri) == PHOTO_ENTRY_URI && getContext() != null) {
            Cursor cursor = db.query(
                    PhotoEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder

            );
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI  in query" + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if (MATCHER.match(uri) == PHOTO_ENTRY_URI) {
            return "vnd.hmni.item/photo";
        } else {
            throw new IllegalArgumentException("Unknown URI get type " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        if (MATCHER.match(uri) == PHOTO_ENTRY_URI && getContext() != null) {
            final long id = db.insertOrThrow(PhotoEntry.TABLE_NAME, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        } else {
            throw new IllegalArgumentException("Unknown URI by insert " + uri);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            if (MATCHER.match(uri) == PHOTO_ENTRY_URI && getContext() != null) {
                insertValues(db, PhotoEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
            } else {
                throw new IllegalArgumentException("Unknown URI by bulkInsert " + uri);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return values.length;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        if (MATCHER.match(uri) == PHOTO_ENTRY_URI && getContext() != null) {
            final int count = db.delete(
                    PhotoEntry.TABLE_NAME, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        } else {
            throw new IllegalArgumentException("Unknown URI by delete " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        if (MATCHER.match(uri) ==
                PHOTO_ENTRY_URI) {
            final int count = db.update(
                    PhotoEntry.TABLE_NAME, values, selection, selectionArgs);
            if (count > 0 && getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return count;
        } else {
            throw new IllegalArgumentException("Unknown URI by update" + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> ops) throws OperationApplicationException {
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

    private long[] insertValues(SQLiteDatabase db, String table, ContentValues[] values) {
        long[] ids = new long[values.length];
        for (ContentValues cv : values) {
            db.insertOrThrow(table, null, cv);
        }
        return ids;
    }
}
