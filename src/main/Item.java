package main;

public enum Item {

    BOMB(2000, "bomb", "bombs"),
    DYNAMITE(10000, "dynamite", "dynamite"),
    TELEPORTER(5000, "teleporter", "teleporters"),

    ;

    public final long PRICE;
    public final String NAME;
    public final String PLURAL;

    Item(long price, String name, String plural) {
        PRICE = price;
        NAME = name;
        PLURAL = plural;
    }
}
