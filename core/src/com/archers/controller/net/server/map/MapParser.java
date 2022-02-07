package com.archers.controller.net.server.map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

public class MapParser {
    public WorldMap parseMap(String file) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(file));
            Element root = doc.getDocumentElement();
            WorldMap map = new WorldMap();
            parseDimensions(map, root);
            createCollections(map);

            Element tileset = (Element) root.getElementsByTagName("tileset").item(0);
            parseTilesProperties(map, tileset);

            Element layer1 = (Element) root.getElementsByTagName("layer").item(0);
            parseLayer(map, layer1, map.objectLayer);

            Element layer2 = (Element) root.getElementsByTagName("layer").item(1);
            parseLayer(map, layer2, map.groundLayer);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void parseDimensions(WorldMap map, Element root) {
        map.WIDTH = Integer.parseInt(root.getAttribute("width"));
        map.HEIGHT = Integer.parseInt(root.getAttribute("height"));
        map.PIXELS = Integer.parseInt(root.getAttribute("tilewidth"));

        map.MIN_X = 0;
        map.MAX_X = (map.WIDTH - 1) * map.PIXELS;
        map.MIN_Y = 0;
        map.MAX_Y = (map.HEIGHT - 1) * map.PIXELS;
    }

    private void createCollections(WorldMap map) {
        map.blocked = new HashSet<>();
        map.shelters = new HashSet<>();

        map.groundLayer = new int[map.HEIGHT][map.WIDTH];
        map.objectLayer = new int[map.HEIGHT][map.WIDTH];
    }

    private void parseTilesProperties(WorldMap map, Element tileset) {
        NodeList tiles = tileset.getElementsByTagName("tile");
        for (int i = 0; i < tiles.getLength(); i++) {
            Element tile = (Element) tiles.item(i);
            int id = Integer.parseInt(tile.getAttribute("id"));
            Element props = (Element) tile.getElementsByTagName("properties").item(0);
            NodeList properties = props.getElementsByTagName("property");
            for (int j = 0; j < properties.getLength(); j++) {
                Element prop = (Element) properties.item(j);
                if ("blocked".equals(prop.getAttribute("name")) &&
                        "true".equals(prop.getAttribute("value"))) {
                    map.blocked.add(id);
                }
                if ("shelter".equals(prop.getAttribute("name")) &&
                        "true".equals(prop.getAttribute("value"))) {
                    map.shelters.add(id);
                }
            }
        }
    }

    private void parseLayer(WorldMap map, Element layer, int[][] array) {
        Element data = (Element) layer.getElementsByTagName("data").item(0);
        Scanner scanner = new Scanner(data.getTextContent());
        scanner.nextLine();
        for (int i = 0; i < map.HEIGHT; i++) {
            String line = scanner.nextLine();
            String[] tiles = line.split(",");
            for (int j = 0; j < map.WIDTH; j++) {
                array[i][j] = Integer.parseInt(tiles[j]);
            }
        }
    }

}
