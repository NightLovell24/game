package com.archers.controller.net.server.map;

public class MapChecker {
    private WorldMap map;

    public MapChecker(String file) {
        map = new MapParser().parseMap(file);
        System.out.println("Map is loaded");
    }

    public boolean isInsideWorld(float x, float y) {
        return x >= map.MIN_X && x <= map.MAX_X
                && y >= map.MIN_Y && y - map.PIXELS / 2 <= map.MAX_Y;
    }

    public boolean isInsideObstacle(float x, float y) {
        Point cell = new Point((int) (x / map.PIXELS + 0.5), (int) y / map.PIXELS);
        return (map.blocked.contains(map.getObjectTile(cell))) ||
                (map.blocked.contains(map.getGroundTile(cell)));
    }

    public boolean isInsideShelter(float x, float y) {
        Point cell = new Point((int) (x / map.PIXELS + 0.5), (int) y / map.PIXELS);
        return map.shelters.contains(map.getObjectTile(cell));
    }

    class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = map.HEIGHT - 1 - y;
            this.y = x;
        }
    }
}
