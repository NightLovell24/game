package com.archers.controller.net.server.map;

import com.archers.controller.net.server.map.MapChecker.Point;

import java.util.Set;

public class WorldMap {
    int[][] groundLayer;
    int[][] objectLayer;

    Set<Integer> blocked;
    Set<Integer> shelters;

    int WIDTH;
    int HEIGHT;

    int PIXELS;

    int MIN_X;
    int MAX_X;

    int MIN_Y;
    int MAX_Y;

    public int getGroundTile(Point p) {
        return groundLayer[p.x][p.y] - 1;
    }

    public int getObjectTile(Point p) {
        return groundLayer[p.x][p.y] - 1;
    }
}
