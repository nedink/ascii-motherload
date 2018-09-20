package main;

public class Tiles {

    Tile[][] tiles;

    Tiles() {

    }

    void genWorld() {
        // top block air
        tiles = new Tile[10][100];

    }

    int width() {
        return tiles == null ? 0 : tiles.length;
    }

    int height() {
        return tiles == null ? 0 : tiles[0].length;
    }
}
