package com.mymapview.presentation.model;


import static java.lang.Runtime.getRuntime;

public final class Constants {

    public static String BASE_URL = "http://b.tile.opencyclemap.org/cycle";

    public static final int ZOOM = 16;

    public static final int TILE_SIZE = 256;

    public static final TilePosition START_COORDINATE = new TilePosition(39617, 20487);

    public static final int X_TILE_COUNT = 100;

    public static final int Y_TILE_COUNT = 100;

    public static final int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);

    //page 176 Goransson A. - Efficient Android Threading - 2014 (RUS)
    public static final int OPTIMAL_THREADS_COUNT = getRuntime().availableProcessors() + 1;

    public static final int FLING_DURATION = 2000;

    public static final int FLING_DELAY = 50;

    public static final int FLING_VELOCITY = 3000;

    private Constants() {
    }
}
