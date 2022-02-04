package com.archers.controller.net.server;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapChecker {
    private final TiledMapTileLayer objectLayer;

    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;

    public static final int PIXELS = 16;
    public static final int CHARACTER_PIXELS = 32;

    public static final int MIN_X = 0;
    public static final int MAX_X = (WIDTH - 1) * PIXELS;

    public static final int MIN_Y = 0;
    public static final int MAX_Y = (HEIGHT - 1) * PIXELS;

    public MapChecker(String file) {
        TmxMapLoader loader = new TmxMapLoader();
        TiledMap map = loader.load(file);
        objectLayer = (TiledMapTileLayer) map.getLayers().get(1);
        System.out.println("Map is loaded");
    }

    public boolean isInsideWorld(float x, float y) {
        return x >= MIN_X && x + CHARACTER_PIXELS / 2 <= MAX_X
                && y >= MIN_Y && y + CHARACTER_PIXELS / 2 <= MAX_Y;
    }

    public boolean isInsideObstacle(float x, float y) {
        TiledMapTileLayer.Cell cell1 = objectLayer.getCell((int) (x / PIXELS + 0.5), (int) y / PIXELS);
        TiledMapTileLayer.Cell cell2 = objectLayer.getCell((int) (x / PIXELS + 1.5), (int) y / PIXELS);
        return (cell1 != null && cell1.getTile().getProperties().containsKey("blocked")) ||
                (cell2 != null && cell2.getTile().getProperties().containsKey("blocked"));
    }

    public boolean isInsideShelter(float x, float y) {
        TiledMapTileLayer.Cell cell = objectLayer.getCell((int) (x / PIXELS + 1), (int) y / PIXELS);
        return cell != null && cell.getTile().getProperties().containsKey("shelter");
    }
}
