package com.dbbest.a500px.simpleDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressWarnings({"PMD.UseUtilityClass", "PMD.UncommentedEmptyConstructor", "PMD.AvoidDuplicateLiterals"})
public class PhotoDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    PhotoDatabase(Context context) {
        super(context.getApplicationContext(), "500px", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PhotoEntry.SQL_CREATE_PHOTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PhotoEntry.TABLE_NAME);
        onCreate(db);
    }
}
