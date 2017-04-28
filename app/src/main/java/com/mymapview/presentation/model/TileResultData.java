package com.mymapview.presentation.model;

import android.graphics.Bitmap;


public final class TileResultData {

    private final NewTileData newTileData;
    private final Bitmap bitmap;

    public TileResultData(NewTileData newTileData, Bitmap bitmap) {
        this.newTileData = newTileData;
        this.bitmap = bitmap;
    }

    public NewTileData getNewTileData() {
        return newTileData;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public String toString() {
        return "Bitmap downloaded for x= " +
                newTileData.getTilePosition().getX() + " y = " + newTileData.getTilePosition().getY();
    }
}
