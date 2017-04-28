package com.mymapview.presentation.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Arrays;

//structure for local disk memory improvement. Using that we are passing the PNG compression step.
public final class BitmapData {

    private final byte[] bitmapBytes;

    public BitmapData(byte[] bitmapBytes) {
        this.bitmapBytes = bitmapBytes;
    }

    public byte[] getBitmapBytes() {
        return bitmapBytes;
    }

    public Bitmap convertToBitmap() {
        if (bitmapBytes == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitmapData that = (BitmapData) o;

        return Arrays.equals(bitmapBytes, that.bitmapBytes);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bitmapBytes);
    }
}
