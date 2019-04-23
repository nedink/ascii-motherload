package main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static main.Direction.*;

public class Main {

    static final int WINDOW_W = 21, WINDOW_H = 15;
    static final int FUEL_PRICE = 1;

    static Player player;
    static World world;
    static String command;
    static String output;
    static Scanner sc;
    static Drill drill;
    static FuelTank fuelTank;
    static CargoBay cargoBay;
    static Map<Item, Integer> items;
    static long $$$;
    static String error = ConsoleCode.RESET + "                                                                    \n";
    static boolean running;
    static boolean saving = false;
    static Confirmation confirmation = null;
    static boolean confirmResult = false;

    public static void main(String[] args) throws IOException {

        newGame();

//        confirmation = Confirmation.LOAD;
        loadSave();

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

            if (!player.alive) return;

            error = ConsoleCode.RESET + "                                                                    \n";

            // get input
            command = sc.nextLine().trim();

            if (confirmation != null) {
                confirmResult = false;
                switch (command) {
                    case "yes":
                        confirmation.getSuccessAction().run();
                        break;
                    case "y":
                        confirmation.getSuccessAction().run();
                        break;
                    default:
                        confirmation.getFailureAction().run();
                        break;
                }
                continue;
            }

            Tile minedTile = null;
            switch (command) {
                case "new":
                    confirmation = Confirmation.NEW_GAME;
                    System.out.print("\r" + confirmation.getMessage() + "                                                                    ");
                    break;
                case "quit":
                    confirmation = Confirmation.QUIT;
                    System.out.print("\r" + confirmation.getMessage() + "                                                                    ");
                    break;
                case "exit":
                    confirmation = Confirmation.QUIT;
                    System.out.print("\r" + confirmation.getMessage() + "                                                                    ");
                    break;
                case "q":
                    confirmation = Confirmation.QUIT;
                    System.out.print("\r" + confirmation.getMessage() + "                                                                    ");
                    break;
                case "save":
                    save();
                    break;
                case "load":
                    confirmation = Confirmation.LOAD;
                    System.out.print("\r" + confirmation.getMessage());
                    break;
                case "x":
                    useItem(Item.BOMB);
                    break;
                case "z":
                    useItem(Item.DYNAMITE);
                    break;
                case "t":
                    useItem(Item.TELEPORTER);
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
                        sellMinerals();
                        break;
                    case "e":
                        sellMinerals();
                        break;
                    case "fuel":
                        refuel();
                        break;
                    case "f":
                        refuel();
                        break;
                    case "drill":
                        upgradePart(drill);
                        break;
                    case "hull":
                        upgradePart(null);
                        break;
                    case "tank":
                        upgradePart(fuelTank);
                        break;
                    case "radiator":
                        upgradePart(null);
                        break;
                    case "cargo":
                        upgradePart(cargoBay);
                        break;
                    case "bomb":
                        buyItem(Item.BOMB);
                        break;
                    case "dyna":
                        buyItem(Item.DYNAMITE);
                        break;
                    case "tele":
                        buyItem(Item.TELEPORTER);
                    default:
                        break;
                }
            }

            // check fuel guage
            if (fuelTank.getFuelRatio() == 0.2f) {
                error = ConsoleCode.TERMINALBELL + (ConsoleCode.WARNING + "WARNING - Fuel reserves low!              \n");
            }
            else if (fuelTank.getFuelRatio() == 0.1f) {
                error = ConsoleCode.TERMINALBELL + (ConsoleCode.WARNING + "WARNING - Fuel reserves critical!         \n");
            }
            else if (fuelTank.getFuel() <= 0) {
                error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "GAME OVER! (Out of fuel)                  \n");
                player.alive = false;
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

    static void newGame() {
        world = new World();
        player = new Player();
        drill = new Drill(1);
        cargoBay = new CargoBay(1);
        fuelTank = new FuelTank(1);
        items = new HashMap<>();
        for (Item item : Item.values()) items.put(item, 0);
        $$$ = 0;
    }

    static void save() throws IOException {
        System.out.print("\rSaving...                              ");
        saving = true;
        File saveFile = new File(".save");
        saveFile.createNewFile();
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(saveFile));
        outputStream.writeObject(world);
        outputStream.writeObject(player);
        outputStream.writeObject($$$);
        outputStream.writeObject(drill);
        outputStream.writeObject(cargoBay);
        outputStream.writeObject(fuelTank);
        outputStream.close();
        System.out.print("\rSaved!                                    ");
        saving = false;
    }

    static void saveAndQuit() throws IOException {
        save();
        running = false;
    }

    static void loadSave() throws IOException {
        File saveFile = new File(".save");
        if (!saveFile.exists()) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "No save file exists.                       \n");
            return;
        }
        System.out.print("\rLoading save...                                     ");
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(saveFile));
        try {
            world = (World) inputStream.readObject();
            player = (Player) inputStream.readObject();
            $$$ = (long) inputStream.readObject();
            drill = (Drill) inputStream.readObject();
            cargoBay = (CargoBay) inputStream.readObject();
            fuelTank = (FuelTank) inputStream.readObject();
        }
        catch (Exception e) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "Save file corrupted.                       \n");
        }
        finally {
            inputStream.close();
            System.out.println("\rSave loaded.                                                   ");
        }
    }

    static Tile move(Direction direction) {
        if (direction == UP && player.getRow() == 0 || fuelTank.getFuel() <= 0)
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
        fuelTank.decFuel(opposingTile.fuelDrain);

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

    static void useItem(Item item) {
        if (items.get(item) <= 0) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "No " + item.PLURAL + " remaining!");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 28 - item.PLURAL.length(); i++) sb.append(" ");
            error += sb.toString() + ConsoleCode.RESET + "\n";
            return;
        }

        switch (item) {
            case BOMB:
                for (int i = 1; i >= 0; i--) {
                    if (player.getRow() > i) {
                        world.setTile(player.getRow() - i, player.getCol() - 1, Tile.TUNNEL);
                        world.setTile(player.getRow() - i, player.getCol(), Tile.TUNNEL);
                        world.setTile(player.getRow() - i, player.getCol() + 1, Tile.TUNNEL);
                    }
                }
                world.setTile(player.getRow() + 1, player.getCol() - 1, Tile.TUNNEL);
                world.setTile(player.getRow() + 1, player.getCol(), Tile.TUNNEL);
                world.setTile(player.getRow() + 1, player.getCol() + 1, Tile.TUNNEL);
                break;
            case DYNAMITE:
                for (int i = 3; i >= 0; i--) {
                    if (player.getRow() > i) {
                        world.setTile(player.getRow() - i, player.getCol() - 3, Tile.TUNNEL);
                        world.setTile(player.getRow() - i, player.getCol() - 2, Tile.TUNNEL);
                        world.setTile(player.getRow() - i, player.getCol() - 1, Tile.TUNNEL);
                        world.setTile(player.getRow() - i, player.getCol(), Tile.TUNNEL);
                        world.setTile(player.getRow() - i, player.getCol() + 1, Tile.TUNNEL);
                        world.setTile(player.getRow() - i, player.getCol() + 2, Tile.TUNNEL);
                        world.setTile(player.getRow() - i, player.getCol() + 3, Tile.TUNNEL);
                    }
                }
                for (int i = 1; i <= 3; i++) {
                    world.setTile(player.getRow() + i, player.getCol() - 3, Tile.TUNNEL);
                    world.setTile(player.getRow() + i, player.getCol() - 2, Tile.TUNNEL);
                    world.setTile(player.getRow() + i, player.getCol() - 1, Tile.TUNNEL);
                    world.setTile(player.getRow() + i, player.getCol(), Tile.TUNNEL);
                    world.setTile(player.getRow() + i, player.getCol() + 1, Tile.TUNNEL);
                    world.setTile(player.getRow() + i, player.getCol() + 2, Tile.TUNNEL);
                    world.setTile(player.getRow() + i, player.getCol() + 3, Tile.TUNNEL);
                }
                break;
            case TELEPORTER:
                player.setRow(0);
                break;
            default:
                break;
        }

        items.put(item, items.get(item) - 1);
    }

    static void sellMinerals() {
        if (cargoBay.getCurrentLoad() == 0) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "No items to sellMinerals.                         " + ConsoleCode.RESET + "\n");
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
        if ($$$ < FUEL_PRICE) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "Not enough cash!                          " + ConsoleCode.RESET + "\n");
            return;
        }
        if (fuelTank.getFuel() == fuelTank.getCapacity()) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.GREEN_BRIGHT + "Tank is already full." + ConsoleCode.RESET + "                     \n");
            return;
        }
        int gallons = Math.min(fuelTank.getCapacity() - fuelTank.getFuel(), (int) $$$ / FUEL_PRICE);
        fuelTank.fill(gallons);
        $$$ -= gallons * FUEL_PRICE;
        error = ConsoleCode.GREEN_BRIGHT + "Bought " + gallons + " gallons of fuel for $" + gallons * FUEL_PRICE + "." + ConsoleCode.RESET + "                  \n";
    }

    static void upgradePart(Part part) {
        if ($$$ < part.getCost(part.tier + 1)) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "Not enough cash!                          " + ConsoleCode.RESET + "\n");
            return;
        }
        $$$ -= part.getCost(part.tier + 1);
        part.upgrade();
        error = ConsoleCode.GREEN_BRIGHT +
                "Upgraded " +
                part.getName() +
                " from " +
                ConsoleCode.RESET +
                tierString(part.getTier() - 1) +
                ConsoleCode.GREEN_BRIGHT +
                " -> " +
                ConsoleCode.RESET +
                tierString(part.getTier()) +
                ConsoleCode.GREEN_BRIGHT +
                " for $" +
                part.getCost() + "." +
                ConsoleCode.RESET + "                  \n";
    }

    static void buyItem(Item item) {
        if ($$$ < Item.BOMB.PRICE) {
            error = ConsoleCode.TERMINALBELL + (ConsoleCode.RED_BACKGROUND_BRIGHT + "Not enough cash!                          " + ConsoleCode.RESET + "\n");
            return;
        }
        $$$ -= item.PRICE;
        items.put(item, items.get(item) + 1);
        error = ConsoleCode.GREEN_BRIGHT + "Bought 1 " + item.NAME + " for $" + item.PRICE + ConsoleCode.RESET + "                  \n";
    }

    static String tierString(int tier) {
        switch (tier) {
            case 1: return ConsoleCode.WHITE_BOLD_BRIGHT + "I" + ConsoleCode.RESET;
            case 2: return ConsoleCode.GREEN_BOLD_BRIGHT + "II" + ConsoleCode.RESET;
            case 3: return ConsoleCode.BLUE_BOLD_BRIGHT + "III" + ConsoleCode.RESET;
            case 4: return ConsoleCode.PURPLE_BOLD_BRIGHT + "IV" + ConsoleCode.RESET;
            case 5: return ConsoleCode.YELLOW_BOLD_BRIGHT + "V" + ConsoleCode.RESET;
            case 6: return ConsoleCode.CYAN_BOLD_BRIGHT + "VI" + ConsoleCode.RESET;
            default: return "";
        }
    }

    static String shopsGraphic() {
        StringBuilder sb = new StringBuilder();
        if (player.getRow() > 0) {
            for (int i = 0; i < 15; i++)
                sb.append("                                                                                        \n");
            return sb.toString();
        }
        return sb
                .append("sellMinerals | e - SELL MINERALS ")
                .append(ConsoleCode.GREEN_BRIGHT).append("+").append(cargoBay.getTotalSellValue()).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("fuel | f - REFUEL ").append(fuelTank.getFillPrice() > $$$ ? ConsoleCode.RED : ConsoleCode.RESET)
                .append("-$").append(fuelTank.getFillPrice()).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("\n")
                .append("drill - UPGRADE DRILL ")
                .append(tierString(drill.getTier())).append(" -> ").append(tierString(drill.getTier() + 1))
                .append(drill.getCost(drill.getTier() + 1) > $$$ ? ConsoleCode.RED : ConsoleCode.RESET)
                .append(" -$").append(drill.getCost(drill.getTier() + 1)).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("hull - UPGRADE HULL ")
                .append(tierString(-1)).append(" -> ").append(tierString(-1))
                .append(ConsoleCode.RED)
                .append(" -$").append(0).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("engine - UPGRADE ENGINE ")
                .append(tierString(-1)).append(" -> ").append(tierString(-1))
                .append(ConsoleCode.RED)
                .append(" -$").append(0).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("tank - UPGRADE FUEL TANK ")
                .append(tierString(fuelTank.getTier())).append(" -> ").append(tierString(fuelTank.getTier() + 1))
                .append(fuelTank.getCost(fuelTank.getTier() + 1) > $$$ ? ConsoleCode.RED : ConsoleCode.RESET)
                .append(" -$").append(fuelTank.getCost(fuelTank.getTier() + 1)).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("radiator - UPGRADE RADIATOR ")
                .append(tierString(-1)).append(" -> ").append(tierString(-1))
                .append(ConsoleCode.RED)
                .append(" -$").append(0).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("cargo - UPGRADE CARGO BAY ")
                .append(tierString(cargoBay.getTier())).append(" -> ").append(tierString(cargoBay.getTier() + 1))
                .append(cargoBay.getCost(cargoBay.getTier() + 1) > $$$ ? ConsoleCode.RED : ConsoleCode.RESET)
                .append(" -$").append(cargoBay.getCost(cargoBay.getTier() + 1)).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("\n")
                .append("bomb - BUY BOMB (Clears a small area around you; press 'x' to use) ").append(Item.BOMB.PRICE > $$$ ? ConsoleCode.RED : ConsoleCode.RESET).append("-$" + Item.BOMB.PRICE).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("dyna - BUY DYNAMITE (Clears a large area around you; press 'z' to use) ").append(Item.DYNAMITE.PRICE > $$$ ? ConsoleCode.RED : ConsoleCode.RESET).append("-$" + Item.BOMB.PRICE).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("tele - BUY TELEPORTER (Instantly returns you to the surface; press 't' to use) ").append(Item.TELEPORTER.PRICE > $$$ ? ConsoleCode.RED : ConsoleCode.RESET).append("-$" + Item.BOMB.PRICE).append(ConsoleCode.RESET)
                .append("                                          \n")
                .append("                                          \n")
                .toString();
    }
}
