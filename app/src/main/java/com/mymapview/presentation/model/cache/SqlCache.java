package com.mymapview.presentation.model.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import static com.mymapview.presentation.model.cache.DatabaseHelper.COLUMNS;
import static com.mymapview.presentation.model.cache.DatabaseHelper.DB_TABLE;
import static com.mymapview.presentation.model.cache.DatabaseHelper.KEY_ID;
import static com.mymapview.presentation.model.cache.DatabaseHelper.KEY_TILE;


public class SqlCache implements Cache<Integer, Bitmap> {

    private final SQLiteDatabase database;

    public SqlCache(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public Bitmap get(Integer key) {
        Cursor cursor = database.query(DB_TABLE, COLUMNS, KEY_ID + " = " + key,
                null, null, null, null, null);

        byte[] tileBytes = null;
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            tileBytes = cursor.getBlob(cursor.getColumnIndex(KEY_TILE));
        }
        cursor.close();
        if (tileBytes == null) {
            return null;
        }

        return BitmapFactory.decodeByteArray(tileBytes, 0, tileBytes.length);
    }

    @Override
    public Bitmap put(Integer key, Bitmap value) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        value.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, key);
        contentValues.put(KEY_TILE, byteArray);

        database.insert(DB_TABLE, null, contentValues);

        return null;
    }

    @Override
    public Bitmap remove(Integer key) {
        return null;
    }
}
