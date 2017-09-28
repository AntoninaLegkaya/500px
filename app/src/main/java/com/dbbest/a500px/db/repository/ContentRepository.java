package com.dbbest.a500px.db.repository;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.dbbest.a500px.App;
import com.dbbest.a500px.db.DBUtil;
import com.dbbest.a500px.db.ProviderDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

abstract class ContentRepository<V extends ContentEntry> implements Repository<V> {

    ContentResolver resolver() {
        return App.instance().getContentResolver();
    }

    @Override
    public boolean exists(Integer id) {
        Cursor cursor = resolver().query(uri(), null, BaseColumns.ID + "=?", new String[]{String.valueOf(id)}, null);
        boolean result = cursor != null && cursor.getCount() > 0;
        closeCursor(cursor);
        return result;
    }

    @Override
    public boolean exists(V entry) {
        return exists(entry.getId());
    }

    @Override
    public int update(V entry) {
        if (exists(entry)) {
            return resolver().update(uri(), entry.values(), BaseColumns.ID + "=?", DBUtil.where(entry.getId()));

        } else {
            return put(entry) ? 1 : 0;
        }
    }

    @Override
    public int remove(V entry) {
        return remove(entry.getId());
    }

    @Override
    public int remove(Integer id) {
        return resolver().delete(uri(), BaseColumns.ID + "=?", new String[]{String.valueOf(id)});
    }

    @Override
    public int removeBatch(String where, Object... args) {
        throw new IllegalArgumentException("Not implemented for this kind of repository");
    }

    @Override
    public boolean put(V entry) {
        if (exists(entry.getId())) {
            return update(entry) > 0;
        }
        return resolver().insert(uri(), entry.values()) != null;
    }


    @Override
    public int bulk(List<V> entries) {
        if (entries == null || entries.isEmpty()) {
            return 0;
        }
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (V entry : entries) {
            Timber.i("Entry  id: %d", entry.getId());
            ContentProviderOperation operation;
            if (exists(entry)) {
                ContentValues values = entry.values();
                if (values.containsKey(BaseColumns.ID)) {
                    values.remove(BaseColumns.ID);
                }
                operation = ContentProviderOperation.newUpdate(uri())
                        .withSelection(BaseColumns.ID + "=?", DBUtil.where(entry.getId()))
                        .withValues(entry.values())
                        .build();
            } else {
                operation = ContentProviderOperation.newInsert(uri())
                        .withValues(entry.values())
                        .build();
            }
            operations.add(operation);
        }
        try {
            return resolver().applyBatch(ProviderDefinition.AUTHORITY, operations).length;
        } catch (RemoteException | OperationApplicationException e) {
            Timber.e(e, "Failed to apply batch for profile response");
        }

        return 0;
    }

    @Override
    public int bulk(V[] entries) {
        if (entries == null || entries.length == 0) {
            return 0;
        }
        return bulk(Arrays.asList(entries));
    }

    @Override
    public void removeAll() {
        resolver().delete(uri(), null, null);
    }

    abstract Uri uri();

    void closeCursor(@Nullable Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    Cursor getEntryAsCursor(Integer id) {
        return resolver().query(uri(), null, BaseColumns.ID + "=?", new String[]{String.valueOf(id)}, null);
    }

    Cursor getEntryListAsCursor(String where, Object... args) {
        String[] selectionArgs = null;
        if (args != null && args.length > 0) {
            selectionArgs = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                selectionArgs[i] = String.valueOf(args[i]);
            }
        }

        return resolver().query(uri(), null, where, selectionArgs, null);
    }
}
