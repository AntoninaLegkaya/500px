package com.dbbest.a500px.simpleDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressWarnings({"PMD.UseUtilityClass", "PMD.UncommentedEmptyConstructor", "PMD.AvoidDuplicateLiterals"})
public class PhotoDatabase extends SQLiteOpenHelper {

    public static final String PHOTO_TABLE =
            "CREATE TABLE photo (" + PhotoEntry.COLUMN_USER_ID + " INTEGER," +
                    PhotoEntry.COLUMN_USER_NAME + " TEXT," +
                    PhotoEntry.COLUMN_PREVIEW_URL + " TEXT," +
                    PhotoEntry.COLUMN_PHOTO_URL + " TEXT," +
                    PhotoEntry.COLUMN_URL_DEFAULT + " TEXT," +
                    PhotoEntry.COLUMN_URL_LARGE + " TEXT," +
                    PhotoEntry.COLUMN_URL_SMALL + " TEXT," +
                    PhotoEntry.COLUMN_URL_TINY + " TEXT," +
                    PhotoEntry._ID + " TEXT PRIMARY KEY ON CONFLICT REPLACE)";
    private static final int DATABASE_VERSION = 1;

    public PhotoDatabase(Context context) {
        super(context.getApplicationContext(), "500px", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PHOTO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PHOTO_TABLE);
        onCreate(db);
    }
}
