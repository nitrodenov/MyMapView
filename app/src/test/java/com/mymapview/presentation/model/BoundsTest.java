package com.mymapview.presentation.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoundsTest {

    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
    public static final int TILE_SIZE = 100;

    @Test
    public void noScroll() throws Exception {
        Bounds bounds = new Bounds(0, 0, 0, 0, TILE_SIZE, 0, 0);

        Bounds b1 = bounds.getNewBounds(0, 0, WIDTH, HEIGHT);
        assertEquals(0, b1.getLeft());
        assertEquals(0, b1.getTop());
        assertEquals(5, b1.getRight());
        assertEquals(8, b1.getBottom());
        assertEquals(0, b1.getX());
        assertEquals(0, b1.getY());
    }

    @Test
    public void scrollX() throws Exception {
        Bounds bounds = new Bounds(0, 0, 0, 0, TILE_SIZE, 0, 0);

        Bounds b1 = bounds.getNewBounds(10, 0, WIDTH, HEIGHT);
        assertEquals(-1, b1.getLeft());
        assertEquals(0, b1.getTop());
        assertEquals(5, b1.getRight());
        assertEquals(8, b1.getBottom());
        assertEquals(-90, b1.getX());
        assertEquals(0, b1.getY());
    }

    @Test
    public void scrollY() throws Exception {
        Bounds bounds = new Bounds(0, 0, 0, 0, TILE_SIZE, 0, 0);

        Bounds b1 = bounds.getNewBounds(0, 10, WIDTH, HEIGHT);
        assertEquals(0, b1.getLeft());
        assertEquals(-1, b1.getTop());
        assertEquals(5, b1.getRight());
        assertEquals(8, b1.getBottom());
        assertEquals(0, b1.getX());
        assertEquals(-90, b1.getY());
    }

    @Test
    public void scrollXY() throws Exception {
        Bounds bounds = new Bounds(0, 0, 0, 0, TILE_SIZE, 0, 0);

        Bounds b1 = bounds.getNewBounds(10, 10, WIDTH, HEIGHT);
        assertEquals(-1, b1.getLeft());
        assertEquals(-1, b1.getTop());
        assertEquals(5, b1.getRight());
        assertEquals(8, b1.getBottom());
        assertEquals(-90, b1.getX());
        assertEquals(-90, b1.getY());
    }

    @Test
    public void scrollMoreTileSize() throws Exception {
        Bounds bounds = new Bounds(0, 0, 0, 0, TILE_SIZE, 0, 0);

        Bounds b1 = bounds.getNewBounds(101, 101, WIDTH, HEIGHT);
        assertEquals(-2, b1.getLeft());
        assertEquals(-2, b1.getTop());
        assertEquals(4, b1.getRight());
        assertEquals(7, b1.getBottom());
        assertEquals(-99, b1.getX());
        assertEquals(-99, b1.getY());
    }
}