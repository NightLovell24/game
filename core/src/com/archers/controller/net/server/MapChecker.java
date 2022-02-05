package com.archers.controller.net.server;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapChecker {
    private final TiledMapTileLayer groundLayer;
    private final TiledMapTileLayer objectLayer;

    public final int WIDTH;
    public final int HEIGHT;

    public final int PIXELS;

    public final int MIN_X;
    public final int MAX_X;

    public final int MIN_Y;
    public final int MAX_Y;

    public MapChecker(TiledMap map) {
        MapProperties prop = map.getProperties();
        WIDTH = prop.get("width", Integer.class);
        HEIGHT = prop.get("height", Integer.class);
        PIXELS = prop.get("tilewidth", Integer.class);
        MIN_X = 0;
        MAX_X = (WIDTH - 1) * PIXELS;
        MIN_Y = 0;
        MAX_Y = (HEIGHT - 1) * PIXELS;

        groundLayer = (TiledMapTileLayer) map.getLayers().get(0);
        objectLayer = (TiledMapTileLayer) map.getLayers().get(1);
        System.out.println("Map is loaded");
    }

    public boolean isInsideWorld(float x, float y) {
        return x >= MIN_X && x + PIXELS / 2 <= MAX_X
                && y >= MIN_Y && y + PIXELS / 2 <= MAX_Y;
    }

    public boolean isInsideObstacle(float x, float y) {
        TiledMapTileLayer.Cell cell1 = objectLayer.getCell((int) (x / PIXELS + 0.5), (int) y / PIXELS);
        TiledMapTileLayer.Cell cell2 = objectLayer.getCell((int) (x / PIXELS + 1.5), (int) y / PIXELS);

        TiledMapTileLayer.Cell cell3 = groundLayer.getCell((int) (x / PIXELS + 0.5), (int) y / PIXELS);
        TiledMapTileLayer.Cell cell4 = groundLayer.getCell((int) (x / PIXELS + 1.5), (int) y / PIXELS);
        return (cell1 != null && cell1.getTile().getProperties().containsKey("blocked")) ||
                (cell2 != null && cell2.getTile().getProperties().containsKey("blocked")) ||
                (cell3 != null && cell3.getTile().getProperties().containsKey("blocked")) ||
                (cell4 != null && cell4.getTile().getProperties().containsKey("blocked"));
    }

    public boolean isInsideShelter(float x, float y) {
        TiledMapTileLayer.Cell cell = objectLayer.getCell((int) (x / PIXELS + 1), (int) y / PIXELS);
        return cell != null && cell.getTile().getProperties().containsKey("shelter");
    }
}
