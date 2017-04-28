package com.mymapview.presentation.model;


public final class TilePosition {

    private final int x;
    private final int y;

    public TilePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TilePosition that = (TilePosition) o;

        return x == that.x && y == that.y;
    }

    //refer https://en.wikipedia.org/wiki/Pairing_function#Cantor_pairing_function
    @Override
    public int hashCode() {
        return ((x + y)*(x + y + 1)/2) + y;
    }

    @Override
    public String toString() {
        return "TilePosition{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
