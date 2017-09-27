package com.dbbest.a500px.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dbbest.a500px.db.repository.UserColumns;

public class AppDatabase extends SQLiteOpenHelper {


    public static final String PHOTO_TABLE = "CREATE TABLE photo ("
            + PhotoColumns.USER_ID + " TEXT,"
            + PhotoColumns.ID + " TEXT PRIMARY KEY ON CONFLICT REPLACE,"
            + " FOREIGN KEY (user_id) REFERENCES user (_id))";

    public static final String USER_TABLE = "CREATE TABLE user ("
            + UserColumns.USER_NAME + " TEXT,"
            + UserColumns.ID + " TEXT PRIMARY KEY ON CONFLICT REPLACE)";

    private static final int DATABASE_VERSION = 1;
    private static volatile AppDatabase instance;

    private Context context;

    private AppDatabase(Context context) {
        super(context.getApplicationContext(), "500px", null, DATABASE_VERSION);
        this.context = context.getApplicationContext();
    }

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = new AppDatabase(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PHOTO_TABLE);
        db.execSQL(USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        com.dbbest.a500px.db.DatabaseDefinition.onUpgrade(context, db, oldVersion, newVersion);
    }
}
