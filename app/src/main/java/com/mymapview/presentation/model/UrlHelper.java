package com.mymapview.presentation.model;

import static com.mymapview.presentation.model.Constants.BASE_URL;
import static com.mymapview.presentation.model.Constants.ZOOM;


public class UrlHelper {

    private UrlHelper() {
    }

    public static String getUrl(NewTileData newTileData) {
        TilePosition startCoordinate = newTileData.getStartCoordinate();
        TilePosition tilePosition = newTileData.getTilePosition();
        int startCoordinateX = startCoordinate.getX();
        int startCoordinatey = startCoordinate.getY();
        int imageCoordinatesX = tilePosition.getX();
        int imageCoordinatesY = tilePosition.getY();
        int newX = startCoordinateX + imageCoordinatesX;
        int newY = startCoordinatey + imageCoordinatesY;

        return BASE_URL + "/" + ZOOM + "/" + newX + "/" + newY + ".png";
    }
}
