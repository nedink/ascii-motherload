package main;

enum ConsoleCode {

    // ------------------------ BASE ------------------------

    ESCAPE("\033"),

    // ------------------------ COLOR ------------------------

    // Reset
    RESET("\033[0m"),  // Text Reset

    // Regular Colors
    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m"),
    BLUE("\033[0;34m"),
    PURPLE("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m"),

    // Bold
    BLACK_BOLD("\033[1;30m"),
    RED_BOLD("\033[1;31m"),
    GREEN_BOLD("\033[1;32m"),
    YELLOW_BOLD("\033[1;33m"),
    BLUE_BOLD("\033[1;34m"),
    PURPLE_BOLD("\033[1;35m"),
    CYAN_BOLD("\033[1;36m"),
    WHITE_BOLD("\033[1;37m"),

    // Underline
    BLACK_UNDERLINED("\033[4;30m"),
    RED_UNDERLINED("\033[4;31m"),
    GREEN_UNDERLINED("\033[4;32m"),
    YELLOW_UNDERLINED("\033[4;33m"),
    BLUE_UNDERLINED("\033[4;34m"),
    PURPLE_UNDERLINED("\033[4;35m"),
    CYAN_UNDERLINED("\033[4;36m"),
    WHITE_UNDERLINED("\033[4;37m"),

    // Background
    BLACK_BACKGROUND("\033[40m"),
    RED_BACKGROUND("\033[41m"),
    GREEN_BACKGROUND("\033[42m"),
    YELLOW_BACKGROUND("\033[43m"),
    BLUE_BACKGROUND("\033[44m"),
    PURPLE_BACKGROUND("\033[45m"),
    CYAN_BACKGROUND("\033[46m"),
    WHITE_BACKGROUND("\033[47m"),

    // High Intensity
    BLACK_BRIGHT("\033[0;90m"),
    RED_BRIGHT("\033[0;91m"),
    GREEN_BRIGHT("\033[0;92m"),
    YELLOW_BRIGHT("\033[0;93m"),
    BLUE_BRIGHT("\033[0;94m"),
    PURPLE_BRIGHT("\033[0;95m"),
    CYAN_BRIGHT("\033[0;96m"),
    WHITE_BRIGHT("\033[0;97m"),

    // Bold High Intensity
    BLACK_BOLD_BRIGHT("\033[1;90m"),
    RED_BOLD_BRIGHT("\033[1;91m"),
    GREEN_BOLD_BRIGHT("\033[1;92m"),
    YELLOW_BOLD_BRIGHT("\033[1;93m"),
    BLUE_BOLD_BRIGHT("\033[1;94m"),
    PURPLE_BOLD_BRIGHT("\033[1;95m"),
    CYAN_BOLD_BRIGHT("\033[1;96m"),
    WHITE_BOLD_BRIGHT("\033[1;97m"),

    // High Intensity backgrounds
    BLACK_BACKGROUND_BRIGHT("\033[0;100m"),
    RED_BACKGROUND_BRIGHT("\033[0;101m"),
    GREEN_BACKGROUND_BRIGHT("\033[0;102m"),
    YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),
    BLUE_BACKGROUND_BRIGHT("\033[0;104m"),
    PURPLE_BACKGROUND_BRIGHT("\033[0;105m"),
    CYAN_BACKGROUND_BRIGHT("\033[0;106m"),
    WHITE_BACKGROUND_BRIGHT("\033[0;107m"),

    // Other
    OTHER("\033[38;5;126m"),
    PCF("\033[1m[31m"),

    // Foreground
    GOLD_1("\033[38;5;220m"),
    MEDIUM_PURPLE_4("\033[38;5;60m"),
    LIGHT_STEEL_BLUE_3("\033[38;5;146m"),
    DARK_ORANGE("\033[38;5;208m"),
    CLOSE_WHITE("\033[38;5;230m"),
    PLATINIUM("\033[38;5;50m"),
    EINSTEINIUM("\033[38;5;200m"),
    EMERALD("\033[38;5;10m"),
    AMAZONITE("\033[38;5;225m"),

    // Background
    DIRT_BACKGROUND("\033[48;5;94m"),
    ORANGE_4_BACKGROUND("\033[48;5;94m"),
    ORANGE_5_BACKGROUND("\033[48;5;58m"),
    DEEP_SKY_BLUE_1_BACKGROUND("\033[48;5;39m"),
    DARK_RED_BACKGROUND("\033[48;5;52m"),
    LIGHT_STEEL_BLUE_3_BACKGROUND("\033[48;5;146m"),
    GREEN_3_BACKGROUND("\033[48;5;34m"),
    CHARTRUSE_2_BACKGROUND("\033[48;5;82m"),

    ORANGE_3_BACKGROUND("\033[48;5;172m"),
    ORANGE_RED_1_BACKGROUND("\033[48;5;202m"),
    DARK_ORANGE_BACKGROUND("\033[48;5;208m"),
    MEDIUM_PURPLE_4_BACKGROUND("\033[48;5;60m"),
    GOLD_1_BACKGROUND("\033[48;5;220m"),

    // ------------------------ SPECIAL ------------------------

//    YELLOW_BACKGROUND("\033[43m"),
    WARNING("\033[0;30;103m"),
    GOLD_1_BOLD("\033"),

    // ------------------------ FX ------------------------

    BLINK("\033[5m"),
    TERMINALBELL("\007"),

    // ------------------------ NAVIGATION ------------------------

    CLEAR_HOME("\033[2J"),
    HOME("\033[H"),

    ;


    private String string;

    ConsoleCode(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings)
            sb.append(s);
        string = sb.toString();
    }

    public static String moveCursorTo(int line, int col) {
        return "\033[" + line + ";" + col + "H";
    }

    @Override
    public String toString() {
        return string;
    }
}
