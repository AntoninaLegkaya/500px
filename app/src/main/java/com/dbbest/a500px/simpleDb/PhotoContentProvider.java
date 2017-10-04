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

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, "photo", PHOTO_ENTRY_URI);
    }

    private SQLiteOpenHelper database;


    @Override
    public boolean onCreate() {
        database = new PhotoDatabase(getContext());

        return false;
    }

    private SelectionBuilder getBuilder(String table) {
        SelectionBuilder builder = new SelectionBuilder();
        return builder;
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
        Cursor cursor = App.instance().getContentResolver().query(PhotoEntry.URI, null, PhotoEntry._ID + "=?", new String[]{String.valueOf(id)}, null);
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
            switch (MATCHER.match(uri)) {
                case PHOTO_ENTRY_URI: {
                    insertValues(db, "photo", values);
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

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)) {
            case PHOTO_ENTRY_URI: {
                return "vnd.hmni.item/photo";
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = database.getReadableDatabase();
        switch (MATCHER.match(uri)) {
            case PHOTO_ENTRY_URI: {
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

            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case PHOTO_ENTRY_URI: {
                final long id = db.insertOrThrow("photo", null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case PHOTO_ENTRY_URI: {
                SelectionBuilder builder = getBuilder("PhotoEntry");
                builder.where(selection, selectionArgs);
                final int count = builder
                        .table("photo")
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
        final SQLiteDatabase db = database.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case PHOTO_ENTRY_URI: {
                SelectionBuilder builder = getBuilder("PhotoEntry");
                builder.where(selection, selectionArgs);
                final int count = builder.table("photo")
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
