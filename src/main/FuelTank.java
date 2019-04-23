package main;

public class FuelTank extends Part {

    private int fuel;
    private int drain;

    public FuelTank(int tier) {
        super(tier);
        fill(getCapacity());
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
            case 7: return 500000;
            default: return 0;
        }
    }

    public int getCapacity() {
        switch (tier) {
            case 1: return 100;
            case 2: return 250;
            case 3: return 1000;
            case 4: return 2500;
            case 5: return 10000;
            case 6: return 50000;
            default: return 0;
        }
    }

    public int getFuel() {
        return fuel;
    }

    public int getDrain() {
        return drain;
    }

    public void setDrain(int drain) {
        this.drain = drain;
    }

    @Override
    public String getName() {
        return "Fuel Tank";
    }

    public float getFuelRatio() {
        return ((float) fuel) / (float) getCapacity();
    }

    public void decFuel(int gallons) {
        int drain = Math.min(fuel, gallons);
        setDrain(drain);
        this.fuel -= drain;
    }

    public void fill(int gallons) {
        fuel = Math.min(getCapacity(), fuel + gallons);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        fill(getCapacity());
    }

    public long getFillPrice() {
        return (getCapacity() - fuel) * 5;
    }

    // TODO
    public String guageGraphic() {
        StringBuilder sb = new StringBuilder();
        sb.append("FUEL ").append(Main.tierString(tier)).append(" [");
        for (int i = 0; i < 10; i++) {
            if (i < Math.ceil(getFuelRatio() * 10))
                sb.append(getFuelRatio() <= 0.5f ? getFuelRatio() <= 0.2f ? "" + ConsoleCode.BLINK + ConsoleCode.RED_BACKGROUND_BRIGHT :
                                                   ConsoleCode.YELLOW_BACKGROUND_BRIGHT : ConsoleCode.GREEN_BACKGROUND_BRIGHT)
                        .append(" ")
                        .append(ConsoleCode.RESET);
            else
                sb.append(getFuelRatio() <= 0.5f ? getFuelRatio() <= 0.2f ? "" + ConsoleCode.BLINK + ConsoleCode.RED_BOLD_BRIGHT :
                                                   ConsoleCode.YELLOW_BOLD_BRIGHT : ConsoleCode.GREEN_BOLD_BRIGHT)
                        .append('/');
        }
        sb.append(ConsoleCode.RESET)
                .append("] ")
                .append(getFuel()).append("/").append(getCapacity()).append(" (")
                .append((int) (100 * getFuelRatio()))
                .append("%) ");
        if (drain > 0) {
            sb.append(ConsoleCode.RED_BRIGHT).append("-").append(drain).append(ConsoleCode.RESET);
        }
        sb.append("                      ");
        return sb.toString();
    }
}
