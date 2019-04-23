package main;

import java.io.Serializable;

public abstract class Part implements Serializable {

    protected int tier;

    public Part(int tier) {
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    public int getCost() {
        return getCost(tier);
    }

    public abstract int getCost(int tier);

    public abstract String getName();

    public void upgrade() {
        this.tier++;
    }
}
