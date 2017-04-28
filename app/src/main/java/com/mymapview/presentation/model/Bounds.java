package com.mymapview.presentation.model;

public class Bounds {

    private final int left;
    private final int right;
    private final int top;
    private final int bottom;
    private final int tileSize;
    private final TilePosition startPoint;

    public Bounds(int left, int right, int top, int bottom, int tileSize, int x, int y) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.tileSize = tileSize;
        startPoint = new TilePosition(x, y);
    }

    public Bounds(int width, int height, int tileSize) {
        this.left = 0;
        this.right = div(width, tileSize);
        this.top = 0;
        this.bottom = div(height, tileSize);
        this.tileSize = tileSize;
        startPoint = new TilePosition(0, 0);
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "left=" + left +
                ", right=" + right +
                ", top=" + top +
                ", bottom=" + bottom +
                ", TILE_SIZE=" + tileSize +
                ", startPoint=" + startPoint +
                '}';
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getX() {
        return startPoint.getX();
    }

    public int getY() {
        return startPoint.getY();
    }

    private int div(int a, int b) {
        return a % b == 0 ? a / b : a / b + 1;
    }

    public Bounds getNewBounds(int dx, int dy, int width, int height) {
        int newX = getX() + dx;
        int x = newX;
        int left = getLeft();
        if (newX > 0) {
            int tiles = div(newX, tileSize);
            left = this.left - tiles;
            x = getX() - tiles * tileSize + dx;
        } else if ((newX + tileSize) <= 0) {
            int tiles = Math.abs(newX / tileSize);
            left = this.left + tiles;
            x = getX() + tiles * tileSize + dx;
        }

        int newY = getY() + dy;
        int y = newY;
        int top = getTop();
        if (newY > 0) {
            int tiles = div(newY, tileSize);
            top = this.top - tiles;
            y = getY() - tiles * tileSize + dy;
        } else if ((newY + tileSize) <= 0) {
            int tiles = Math.abs(newY / tileSize);
            top = this.top + tiles;
            y = getY() + tiles * tileSize + dy;
        }

        if (x > 0 || x < -tileSize) {
            throw new IllegalStateException("x illegal value");
        }
        if (y > 0 || y < -tileSize) {
            throw new IllegalStateException("y illegal value");
        }

        int right = left + div((width - x), tileSize);
        int bottom = top + div((height - y), tileSize);

        return new Bounds(left, right, top, bottom, tileSize, x, y);
    }

    public boolean contains(int x, int y) {
        return x >= left && x < right && y >= top && y < bottom;
    }
}
