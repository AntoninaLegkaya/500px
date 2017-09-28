package com.dbbest.a500px.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dbbest.a500px.db.repository.PhotoColumns;
import com.dbbest.a500px.db.repository.UserColumns;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;
@SuppressWarnings({"PMD.UseUtilityClass", "PMD.UncommentedEmptyConstructor", "PMD.AvoidDuplicateLiterals"})
@Database(version = DatabaseDefinition.VERSION, packageName = "com.dbbest.a500px.db",
        className = "AppDatabase", fileName = DatabaseDefinition.DATABASE_NAME)
public class DatabaseDefinition {
    static final String DATABASE_NAME = "500px";
    static final int VERSION = 1;

    @Table(PhotoColumns.class)
    static final String PHOTO_TABLE = "photo";
    @Table(UserColumns.class)
    static final String USER_TABLE = "user";

    @OnUpgrade
    static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PHOTO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);

        ApplicationDatabase.getInstance(context).onCreate(db);
    }

    /**
     * Delete an existing private SQLiteDatabase associated with this Context's
     * application package.
     *
     * @return {@code true} if the database was successfully deleted; else {@code false}.
     */
    public static boolean removeDatabaseFile(Context context) {
        return context.deleteDatabase(DATABASE_NAME);
    }

}
