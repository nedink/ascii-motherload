package main;

import static main.ConsoleCode.*;

public enum Tile {

    AIR("  ", DEEP_SKY_BLUE_1_BACKGROUND, 0),
    DIRT("  ", DIRT_BACKGROUND, 0),
    TUNNEL("  ", DARK_RED_BACKGROUND, 0),
    STONE(WHITE_BOLD + "()", MEDIUM_PURPLE_4_BACKGROUND, 6),
    LAVA(RED_BOLD_BRIGHT + "~_", RED_BACKGROUND, 0),
    IRONIUM("  ", DIRT.backgroundColor, 1),
    BRONZIUM("  ", DIRT.backgroundColor, 1),
    SILVERIUM("  ", DIRT.backgroundColor, 2),
    GOLDIUM("  ", DIRT.backgroundColor, 2),
    PLATINIUM("  ", DIRT.backgroundColor, 3),
    EINSTEINIUM("  ", DIRT.backgroundColor, 3),
    EMERALD("  ", DIRT.backgroundColor, 4),
    RUBY("  ", DIRT.backgroundColor, 4),
    DIAMOND("  ", DIRT.backgroundColor, 5),
    AMAZONITE("  ", DIRT.backgroundColor, 5);

    public final String tileString;
    public final ConsoleCode backgroundColor;
    public final int drillTier;

    Tile(String tileString, ConsoleCode backgroundColor, int drillTier) {
        this.tileString = tileString;
        this.backgroundColor = backgroundColor;
        this.drillTier = drillTier;
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
