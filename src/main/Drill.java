package main;

public class Drill extends Part {

    public Drill(int tier) {
        super(tier);
    }

    @Override
    public int getCost(int tier) {
        switch (tier) {
            case 1: return 0;
            case 2: return 750;
            case 3: return 2000;
            case 4: return 5000;
            case 5: return 20000;
            case 6: return 100000;
            default: return 0;
        }
    }
}
