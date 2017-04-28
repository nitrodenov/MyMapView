package com.mymapview.presentation.model;


public final class NewTileData {

    private final TilePosition tilePosition;
    private final TilePosition startCoordinate;

    public NewTileData(TilePosition tilePosition, TilePosition startCoordinate) {
        this.tilePosition = tilePosition;
        this.startCoordinate = startCoordinate;
    }

    public TilePosition getTilePosition() {
        return tilePosition;
    }

    public TilePosition getStartCoordinate() {
        return startCoordinate;
    }

    @Override
    public String toString() {
        return "x= " + tilePosition.getX() + " y = " + tilePosition.getY();
    }
}
