package main;

import static main.ConsoleCode.*;

public enum Tile {

    AIR("  ", DEEP_SKY_BLUE_1_BACKGROUND, 0, 1),
    TUNNEL("  ", DARK_RED_BACKGROUND, 0, 1),
    DIRT("  ", DIRT_BACKGROUND, 0, 2),
    STONE(WHITE_BOLD + "()", MEDIUM_PURPLE_4_BACKGROUND, 6, 100),
    LAVA(RED_BOLD_BRIGHT + "~_", RED_BACKGROUND, 0, 2),
    IRONIUM("  ", DIRT.backgroundColor, 1, 4),
    BRONZIUM("  ", DIRT.backgroundColor, 1, 8),
    SILVERIUM("  ", DIRT.backgroundColor, 2, 16),
    GOLDIUM("  ", DIRT.backgroundColor, 2, 32),
    PLATINIUM("  ", DIRT.backgroundColor, 3, 64),
    EINSTEINIUM("  ", DIRT.backgroundColor, 3, 128),
    EMERALD("  ", DIRT.backgroundColor, 4, 256),
    RUBY("  ", DIRT.backgroundColor, 4, 512),
    DIAMOND("  ", DIRT.backgroundColor, 5, 1024),
    AMAZONITE("  ", DIRT.backgroundColor, 5, 2048);

    public final String tileString;
    public final ConsoleCode backgroundColor;
    public final int drillTier;
    public final int fuelDrain;

    Tile(String tileString, ConsoleCode backgroundColor, int drillTier, int fuelDrain) {
        this.tileString = tileString;
        this.backgroundColor = backgroundColor;
        this.drillTier = drillTier;
        this.fuelDrain = fuelDrain;
    }

    public Mineral getMineral() {
        switch (this) {
            case IRONIUM:
                return Mineral.IRONIUM;
            case BRONZIUM:
                return Mineral.BRONZIUM;
            case SILVERIUM:
                return Mineral.SILVERIUM;
            case GOLDIUM:
                return Mineral.GOLDIUM;
            case PLATINIUM:
                return Mineral.PLATINIUM;
            case EINSTEINIUM:
                return Mineral.EINSTEINIUM;
            case EMERALD:
                return Mineral.EMERALD;
            case RUBY:
                return Mineral.RUBY;
            case DIAMOND:
                return Mineral.DIAMOND;
            case AMAZONITE:
                return Mineral.AMAZONITE;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return backgroundColor.toString() + (getMineral() == null ? tileString : getMineral()) + RESET;
    }
}
