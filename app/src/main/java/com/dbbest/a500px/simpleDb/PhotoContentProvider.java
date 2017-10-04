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
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dbbest.a500px.App;
import com.dbbest.a500px.BuildConfig;
import com.dbbest.a500px.db.model.PhotoModel;

import net.simonvt.schematic.utils.SelectionBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;


public class PhotoContentProvider extends ContentProvider {


    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.PhotoContentProvider";
    private static final int PHOTO_ENTRY_URI = 0;
    private static final String TABLE_PHOTO_NAME = "photo";

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_PHOTO_NAME, PHOTO_ENTRY_URI);
    }

    private SQLiteOpenHelper database;


    @Override
    public boolean onCreate() {
        database = new PhotoDatabase(getContext());

        return false;
    }

    private SelectionBuilder getBuilder() {
        return new SelectionBuilder();
    }

    private long[] insertValues(SQLiteDatabase db, String table, ContentValues[] values) {
        long[] ids = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            ContentValues cv = values[i];
            db.insertOrThrow(table, null, cv);
        }
        return ids;
    }

    public int bulk(List<PhotoModel> entries) {
        if (entries == null || entries.isEmpty()) {
            return 0;
        }
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (PhotoModel entry : entries) {
            Timber.i("Entry  id: %d", entry.getId());
            ContentProviderOperation operation;
            if (exists(entry)) {
                ContentValues values = entry.values();
                if (values.containsKey(PhotoEntry._ID)) {
                    values.remove(PhotoEntry._ID);
                }
                operation = ContentProviderOperation.newUpdate(PhotoEntry.URI)
                        .withSelection(PhotoEntry._ID + "=?", DBUtil.where(entry.getId()))
                        .withValues(entry.values())
                        .build();
            } else {
                operation = ContentProviderOperation.newInsert(PhotoEntry.URI)
                        .withValues(entry.values())
                        .build();
            }
            operations.add(operation);
        }
        try {
            return App.instance().getContentResolver().applyBatch(AUTHORITY, operations).length;
        } catch (RemoteException | OperationApplicationException e) {
            Timber.e(e, "Failed to apply batch for profile response");
        }

        return 0;
    }


    public int bulk(PhotoModel[] entries) {
        if (entries == null || entries.length == 0) {
            return 0;
        }
        return bulk(Arrays.asList(entries));
    }

    public boolean exists(Integer id) {
        Cursor cursor = App.instance().getContentResolver().query(PhotoEntry.URI, null, PhotoEntry._ID + "=?",
                new String[]{String.valueOf(id)}, null);
        boolean result = cursor != null && cursor.getCount() > 0;
        closeCursor(cursor);
        return result;
    }

    void closeCursor(@Nullable Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public boolean exists(PhotoModel entry) {
        return exists(entry.getId());
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            if (MATCHER.match(uri) == PHOTO_ENTRY_URI) {
                insertValues(db, TABLE_PHOTO_NAME, values);
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

    @Nullable
    @Override
    public String getType(Uri uri) {
        if (MATCHER.match(uri) == PHOTO_ENTRY_URI) {
            return "vnd.hmni.item/photo";
        } else {
            throw new IllegalArgumentException("Unknown URI get type " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = database.getReadableDatabase();
        if (MATCHER.match(uri) == PHOTO_ENTRY_URI) {
            SelectionBuilder builder = getBuilder();
            String table = TABLE_PHOTO_NAME;
            Cursor cursor = builder.table(table)
                    .where(selection, selectionArgs)
                    .query(db, projection, null, null, sortOrder, null);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI  in query" + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        if (MATCHER.match(uri) == PHOTO_ENTRY_URI) {
            final long id = db.insertOrThrow(TABLE_PHOTO_NAME, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        } else {
            throw new IllegalArgumentException("Unknown URI by insert " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        if (MATCHER.match(uri) == PHOTO_ENTRY_URI) {
            SelectionBuilder builder = getBuilder();
            builder.where(selection, selectionArgs);
            final int count = builder
                    .table(TABLE_PHOTO_NAME)
                    .delete(db);
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
            SelectionBuilder builder = getBuilder();
            builder.where(selection, selectionArgs);
            final int count = builder.table(TABLE_PHOTO_NAME)
                    .update(db, values);
            if (count > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return count;
        } else {
            throw new IllegalArgumentException("Unknown URI by update" + uri);
        }
    }
}
