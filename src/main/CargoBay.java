package main;

import java.util.HashMap;
import java.util.Map;

public class CargoBay extends Part {

    private Map<Mineral, Integer> haul = new HashMap<>();

    CargoBay(int tier) {
        super(tier);
        for (Mineral m : Mineral.values()) {
            haul.put(m, 0);
        }
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

    public int getCarryingCapacity() {
        switch (tier) {
            case 1: return 10;
            case 2: return 15;
            case 3: return 25;
            case 4: return 40;
            case 5: return 70;
            case 6: return 120;
            default: return 0;
        }
    }

    public Map<Mineral, Integer> getHaul() {
        return haul;
    }

    @Override
    public String getName() {
        return "Cargo Bay";
    }

    public int getCurrentLoad() {
        return haul.values().stream().mapToInt(i -> i).sum();
    }

    public boolean addMineral(Mineral m) {
        if (getCurrentLoad() >= getCarryingCapacity())
            return false;
        haul.put(m, haul.get(m) + 1);
        return true;
    }

    public long getTotalSellValue() {
        return haul.keySet().stream().mapToLong(mineral -> haul.get(mineral) * mineral.sellValue).sum();
    }

    public String guageGraphic() {
        StringBuilder sb = new StringBuilder();
        float percent = (float) getCurrentLoad() / getCarryingCapacity();
        sb.append("CARGO BAY ").append(Main.tierString(tier)).append(" [").append(percent > 0.5 ? percent > 0.8 ? ConsoleCode.RED_BACKGROUND : ConsoleCode.YELLOW_BACKGROUND : ConsoleCode.GREEN_BACKGROUND);
        for (int i = 0; i < 30; i++) {
            if (i < percent * 30) {
                sb.append(percent > 0.5 ? percent > 0.8 ? ConsoleCode.RED_BACKGROUND : ConsoleCode.YELLOW_BACKGROUND : ConsoleCode.GREEN_BACKGROUND)
                        .append(" ");
            }
            else {
                sb.append(percent > 0.5 ? percent > 0.8 ? ConsoleCode.RED : ConsoleCode.YELLOW : ConsoleCode.GREEN)
                        .append(i % 3 == 1 ? "/" : " ");
            }
        }
        sb.append(ConsoleCode.RESET).append("] ").append(getCurrentLoad()).append("/").append(getCarryingCapacity())
                .append(" (").append((int) (100 * (getCurrentLoad() / (float) getCarryingCapacity()))).append("%)");
        return sb.toString();
    }

    public String haulGraphic() {
        StringBuilder sb = new StringBuilder();
        for (Mineral m : Mineral.values()) {
            if (haul.get(m) > 0) {
                sb
                        .append(m)
                        .append(" ")
                        .append(m.color)
                        .append(haul.get(m))
                        .append(" x $")
                        .append(m.sellValue)
                        .append(" = $")
                        .append(m.sellValue * haul.get(m))
                        .append(ConsoleCode.RESET);
                if (Main.player.getRow() == 0) ;
//                    sb
//                            .append(" x")
//                    .append(haul.get(m))
            }

            sb.append("                                         \n");
        }
        return sb.toString();
    }
}
