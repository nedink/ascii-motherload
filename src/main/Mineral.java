package main;

public enum Mineral {

    IRONIUM("# ", 150, 30, 10, ConsoleCode.LIGHT_STEEL_BLUE_3),
    BRONZIUM(" #", 300, 60, 10, ConsoleCode.DARK_ORANGE),
    SILVERIUM("# ", 500, 100, 10, ConsoleCode.CLOSE_WHITE),
    GOLDIUM(" #", 1250, 250, 20, ConsoleCode.GOLD_1),
    PLATINIUM("# ", 3750, 750, 30, ConsoleCode.PLATINIUM),
    EINSTEINIUM("=\"", 10000, 2000, 40, ConsoleCode.EINSTEINIUM),
    EMERALD("`.", 25000, 5000, 40, ConsoleCode.EMERALD),
    RUBY(".'", 50000, 20000, 80, ConsoleCode.RED_BOLD_BRIGHT),
    DIAMOND(" +", 100000, 100000, 120, ConsoleCode.WHITE_BOLD_BRIGHT),
    AMAZONITE("_=", 500000, 500000, 120, ConsoleCode.AMAZONITE),

    ;

    private String tileString;
    public final long points;
    public final long sellValue;
    public final long weight;
    public final ConsoleCode color;

    Mineral(String tileString, int points, int sellValue, int weight, ConsoleCode color) {
        this.tileString = tileString;
        this.points = points;
        this.sellValue = sellValue;
        this.weight = weight;
        this.color = color;
    }

    @Override
    public String toString() {
        return color + tileString + ConsoleCode.RESET;
    }
}
