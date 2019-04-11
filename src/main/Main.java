package main;

import java.util.Scanner;

import static main.Direction.*;

public class Main {

    static final int WINDOW_W = 21, WINDOW_H = 15;

    static Player player;
    static World world;
    static String command;
    static String output;
    static Scanner sc;
    static Drill drill;
    static FuelTank fuelTank;
    static long $$$ = 0;
    static CargoBay cargoBay;
    static String error = ConsoleCode.RESET + "                                                                    \n";
    static boolean running;

    public static void main(String[] args) {

        player = new Player();
        drill = new Drill(1);
        cargoBay = new CargoBay(1);
        fuelTank = new FuelTank(1);
        world = new World();

        sc = new Scanner(System.in);
        running = true;

        System.out.print(ConsoleCode.CLEAR_HOME);

        while (running) {
            StringBuilder out = new StringBuilder()
                    .append(ConsoleCode.HOME)
                    // top area
                    .append(drawHeading())
                    // things
                    .append(drawIndicators())
                    // error
                    .append(error).append(ConsoleCode.RESET)
                    // world
                    .append(drawWindow())
                    // prompt
                    .append(drawPrompt());

            System.out.print(out);

            if (!player.alive)
                System.exit(0);

            error = ConsoleCode.RESET + "                                                                    \n";

            // get input
            command = sc.nextLine().trim();

            Tile minedTile = null;
            switch (command) {
                case "exit":
                    running = false;
                    break;
                case "quit":
                    running = false;
                    break;
                case "q":
                    running = false;
                    break;
                case "w":
                    minedTile = move(UP);
                    break;
                case "s":
                    minedTile = move(DOWN);
                    break;
                case "a":
                    minedTile = move(LEFT);
                    break;
                case "d":
                    minedTile = move(RIGHT);
                    break;
                case "ww":
                    for (int i = 0; i < 10; i++) minedTile = move(UP);
                    break;
                case "ss":
                    for (int i = 0; i < 10; i++)
                        minedTile = move(DOWN);
                    break;
                default:
                    break;
            }

            if (player.getRow() == 0) {
                switch (command) {
                    case "sell":
                        sell();
                        break;
                    case "e":
                        sell();
                        break;
                    case "fuel":
                        refuel();
                        break;
                    case "f":
                        refuel();
                        break;
                    case "drill":
                        upgrade(drill);
                        break;
                    case "hull":
                        upgrade(null);
                        break;
                    case "tank":
                        upgrade(fuelTank);
                        break;
                    case "radiator":
                        upgrade(null);
                        break;
                    case "cargo":
                        upgrade(cargoBay);
                        break;
                    default:
                        break;
                }
            }

            // check fuel guage
            if (fuelTank.getFuelRatio() == 0.2f) {
                error = ConsoleCode.TERMINALBELL + (ConsoleCode.WARNING + "WARNING - Fuel reserves low!              \n");
            }
            else if (fuelTank.getFuelRatio() == 0.1f) {
                // "                                          "
                // "GAME OVER! Fuel depleted.                 "
                error = ConsoleCode.TERMINALBELL + (ConsoleCode.WARNING + "WARNING - Fuel reserves critical!         \n");
            }
            else if (fuelTank.getFuel() <= 0) {
                player.alive = false;
                error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "GAME OVER! Fuel depleted.                 \n");
            }
        }
    }

    static StringBuilder drawIndicators() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 41 - String.valueOf($$$).length(); i++)
            sb.append(" ");
        sb
                .append(ConsoleCode.YELLOW_BRIGHT).append("$").append(ConsoleCode.YELLOW_BOLD_BRIGHT).append($$$).append(ConsoleCode.RESET)
                .append("\n")
                .append(cargoBay.guageGraphic())
                .append("\n")
                .append(fuelTank.guageGraphic())
                .append("\n");
        return sb;
    }

    static StringBuilder drawWindow() {

        // get world window
        String[][] world = new String[WINDOW_H][WINDOW_W];
        for (int r = 0; r < WINDOW_H; r++) {
            for (int c = 0; c < WINDOW_W; c++) {
                // add tile
                int row = r + player.getRow() - WINDOW_H / 2, col = c + player.getCol() - WINDOW_W / 2;
                if (row == player.getRow() && col == player.getCol()) {
                    System.out.println(player);
                    Tile tile = Main.world.getTile(player.getRow(), player.getCol() - 1);
                    world[r][c] = (tile == Tile.AIR ? Tile.AIR.backgroundColor.toString() : Tile.TUNNEL.backgroundColor) + player.toString();
                }
                else
                    world[r][c] = "" + Main.world.getTile(row, col);
            }
        }

        // to tileString
        StringBuilder sb = new StringBuilder();

        // add world
        for (int r = 0; r < WINDOW_H; r++) {
            for (int c = 0; c < WINDOW_W; c++) {
                sb.append(world[r][c] == null ? "--" : world[r][c]);
            }
            sb.append('\n');
        }

        // send
        return sb;
    }

    static String drawHeading() {
        StringBuilder sb = new StringBuilder();
        sb.append(cargoBay.haulGraphic());
        sb.append(shopsGraphic());
        return sb.toString();
    }

    static String drawPrompt() {
        return "                                                   \r> ";
    }

    static Tile move(Direction direction) {
        if (direction == UP && player.getRow() == 0)
            return null;

        Tile opposingTile = world.getTile(
                direction == UP ? player.getRow() - 1 :
                direction == DOWN ? player.getRow() + 1 :
                direction == LEFT ? player.getRow() :
                player.getRow(),
                direction == UP ? player.getCol() :
                direction == DOWN ? player.getCol() :
                direction == LEFT ? player.getCol() - 1 :
                player.getCol() + 1);

        if (opposingTile.drillTier > drill.getTier()) {
            error = new StringBuilder()
                    .append(ConsoleCode.TERMINALBELL)
                    .append(ConsoleCode.RED_BACKGROUND_BRIGHT)
                    .append("The rock is too thick here.                \n")
                    .toString();
            return null;
        }

        player.move(direction);
        fuelTank.decFuel();

        if (opposingTile != Tile.AIR && opposingTile != Tile.TUNNEL)
            world.setTile(player.getRow(), player.getCol(), Tile.TUNNEL);

        Mineral mineral = opposingTile.getMineral();
        if (mineral != null) {
            if (!cargoBay.addMineral(mineral)) {
                error = new StringBuilder()
                        .append(ConsoleCode.TERMINALBELL)
                        .append(ConsoleCode.WARNING)
                        .append("Cargo bay is full!                        \n")
                        .toString();
            }
        }

        return opposingTile;
    }

    static String tierString(int tier) {
        switch (tier) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            default: return "";
        }
    }

    static String shopsGraphic() {
        StringBuilder sb = new StringBuilder();
        if (player.getRow() > 0) {
            for (int i = 0; i < 9; i++)
                sb.append("                                                                                        \n");
            return sb.toString();
        }
        return sb
                .append("fuel | f - REFUEL (cost: $").append((fuelTank.getCapacity() - fuelTank.getFuel()) * 5).append(")")
                .append("                                          \n")
                .append("sell | e - SELL MINERALS (total: $").append(cargoBay.getTotalSellValue()).append(")")
                .append("                                          \n")
                .append("drill - UPGRADE DRILL ")
                .append(tierString(drill.getTier())).append(" -> ").append(tierString(drill.getTier() + 1))
                .append(" (cost: $").append(drill.getCost(drill.getTier() + 1)).append(")")
                .append("                                          \n")
                .append("hull - UPGRADE HULL ")
                .append(tierString(drill.getTier())).append(" -> ").append(tierString(drill.getTier() + 1))
                .append(" (cost: $").append(drill.getCost(drill.getTier() + 1)).append(")")
                .append("                                          \n")
                .append("engine - UPGRADE ENGINE ")
                .append(tierString(drill.getTier())).append(" -> ").append(tierString(drill.getTier() + 1))
                .append(" (cost: $").append(drill.getCost(drill.getTier() + 1)).append(")")
                .append("                                          \n")
                .append("tank - UPGRADE FUEL TANK ")
                .append(tierString(fuelTank.getTier())).append(" -> ").append(tierString(fuelTank.getTier() + 1))
                .append(" (cost: $").append(fuelTank.getCost(fuelTank.getTier() + 1)).append(")")
                .append("                                          \n")
                .append("radiator - UPGRADE RADIATOR ")
                .append(tierString(drill.getTier())).append(" -> ").append(tierString(drill.getTier() + 1))
                .append(" (cost: $").append(drill.getCost(drill.getTier() + 1)).append(")")
                .append("                                          \n")
                .append("cargo - UPGRADE CARGO BAY ")
                .append(tierString(cargoBay.getTier())).append(" -> ").append(tierString(cargoBay.getTier() + 1))
                .append(" (cost: $").append(cargoBay.getCost(cargoBay.getTier() + 1)).append(")")
                .append("                                          \n")
                .append("                                          \n")
                .toString();
    }

    static void sell() {
        if (cargoBay.getCurrentLoad() == 0) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "No items to sell.                         " + ConsoleCode.RESET + "\n");
            return;
        }
        int totalMinerals = cargoBay.getCurrentLoad();
        long sellValue = cargoBay.getTotalSellValue();
        cargoBay.getHaul().forEach((mineral, count) -> {
            $$$ += mineral.sellValue * count;
            cargoBay.getHaul().put(mineral, 0);
        });
        error = ConsoleCode.GREEN_BRIGHT + "Sold " + totalMinerals + " minerals for $" + sellValue + ConsoleCode.RESET + "                  \n";
    }

    static void refuel() {
        if ($$$ < 5) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "Not enough cash!                          " + ConsoleCode.RESET + "\n");
            return;
        }
        if (fuelTank.getFuel() == fuelTank.getCapacity()) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.GREEN_BRIGHT + "Tank is already full." + ConsoleCode.RESET + "                     \n");
            return;
        }
        int gallons = Math.min(fuelTank.getCapacity() - fuelTank.getFuel(), (int) $$$ / 5);
        fuelTank.fill(gallons);
        $$$ -= gallons * 5;
        error = ConsoleCode.GREEN_BRIGHT + "Bought " + gallons + " gallons of fuel for $" + gallons * 5 + "." + ConsoleCode.RESET + "                  \n";
    }

    static void upgrade(Part part) {
        if ($$$ < part.getCost(part.tier + 1)) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "Not enough cash!                          " + ConsoleCode.RESET + "\n");
            return;
        }
        $$$ -= part.getCost(part.tier + 1);
        part.upgrade();
        error = ConsoleCode.GREEN_BRIGHT + "Upgraded part from " + tierString(part.getTier() - 1) + " -> " + tierString(part.getTier()) + " for $" + part.getCost() + "." + ConsoleCode.RESET + "                  \n";
    }
}
