package com.mymapview.presentation.model.cache;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCache implements Cache<Integer, Bitmap> {

    private Context mContext;

    public FileCache(Context context) {
        mContext = context;
    }

    @Override
    public Bitmap get(Integer key) {
        Bitmap myBitmap = null;
        String filePath = mContext.getFilesDir().getPath() + "/" + String.valueOf(key);
        File file = new File(filePath);
        if (file.exists()) {
            myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        }

        return myBitmap;
    }

    @Override
    public Bitmap put(Integer key, Bitmap value) {
        FileOutputStream out = null;
        try {
            String filePath = mContext.getFilesDir().getPath() + "/" + String.valueOf(key);
            File file = new File(filePath);
            out = new FileOutputStream(file);
            value.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Bitmap remove(Integer key) {
        return null;
    }
}
