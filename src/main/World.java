package main;

import java.io.Serializable;
import java.util.*;

import static main.Tile.*;

public class World implements Serializable {

    public static final int GEN_RADIUS = 4;

    protected Map<String, Tile> tiles = new HashMap(); // row -> col

    public World() {
    }

    public Tile getTile(int row, int col) {
        Tile tile = tiles.get(row + "," + col);
        if (tile == null)
            return genTiles(row, col);
        return tile;
    }

    private Tile genTiles(int row, int col) {
        for (int r = row - GEN_RADIUS; r < row + GEN_RADIUS; r++) {
            for (int c = col - GEN_RADIUS; c < col + GEN_RADIUS; c++) {
                final Tile newTile = genTile(r, c);
                tiles.compute(r + "," + c, (rc, tile) -> tile == null ? newTile : tile);
            }
        }
        return tiles.get(row + "," + col);
    }

    private Tile genTile(int row, int col) {
        if (row <= 0)
            return AIR;

        Random r = new Random();

//        double log = Math.log(row);
        double log = row;

        if (r.nextDouble() < 1d / 300d * (log - 200)) return LAVA;
        if (r.nextDouble() < 1d / 300d * (log - 100)) return STONE;
        if (r.nextDouble() < 2d / 3d) return DIRT;
        if (r.nextDouble() < 1 / 100d * (log - 350)) {return AMAZONITE;}
        if (r.nextDouble() < 1 / 100d * (log - 300)) {return DIAMOND;}
        if (r.nextDouble() < 1 / 100d * (log - 250)) {return RUBY;}
        if (r.nextDouble() < 1 / 100d * (log - 200)) {return EMERALD;}
        if (r.nextDouble() < 1 / 100d * (log - 150)) {return EINSTEINIUM;}
        if (r.nextDouble() < 1 / 100d * (log - 110)) {return PLATINIUM;}
        if (r.nextDouble() < 1 / 100d * (log - 70)) {return GOLDIUM;}
        if (r.nextDouble() < 1 / 100d * (log - 40)) {return SILVERIUM;}
        if (r.nextDouble() < 1 / 100d * (log - 10)) {return BRONZIUM;}
        if (r.nextDouble() < 1 / 100d * (log + 20)) {return IRONIUM;}

        return DIRT;
    }

    public void setTile(int row, int col, Tile tile) {
        tiles.put(row + "," + col, tile);
    }
}
