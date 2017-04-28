package com.mymapview.presentation.model.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database_name";
    public static final int DATABASE_VERSION = 1;
    public static final String DB_TABLE = "table_tiles";
    public static final String KEY_ID = "tile_id";
    public static final String KEY_TILE = "tile_data";
    public static final String[] COLUMNS = {KEY_ID, KEY_TILE};
    private static final String CREATE_TABLE_TILES = "CREATE TABLE IF NOT EXISTS " + DB_TABLE + "("
            + KEY_ID + " INTEGER,"
            + KEY_TILE + " BLOB);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TILES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
