package main;

public class FuelTank extends Part {

    private int fuel;

    public FuelTank(int tier) {
        super(tier);
        fill(getCapacity());
    }

    @Override
    public int getCost(int tier) {
        switch (tier) {
            case 1: return 0;
            case 2: return 500;
            case 3: return 750;
            case 4: return 2000;
            case 5: return 5000;
            case 6: return 20000;
            case 7: return 100000;
            default: return 0;
        }
    }

    public int getCapacity() {
        switch (tier) {
            case 1: return 30;
            case 2: return 60;
            case 3: return 100;
            case 4: return 200;
            case 5: return 500;
            case 6: return 1000;
            default: return 0;
        }
    }

    public int getFuel() {
        return fuel;
    }

    public float getFuelRatio() {
        return ((float) fuel) / (float) getCapacity();
    }

    public void decFuel() {
        this.fuel--;
    }

    public void fill(int gallons) {
        fuel = Math.min(getCapacity(), fuel + gallons);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        fill(getCapacity());
    }

    // TODO
    public String guageGraphic() {
        StringBuilder sb = new StringBuilder();
        sb.append("FUEL [");
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
                .append("%)                      ");
        return sb.toString();
    }
}
