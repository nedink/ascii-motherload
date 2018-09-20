package main;

import java.util.Scanner;

public class Main {

    static Player player;
    static Tiles tiles;
    static String command;
    static String output;
    static Scanner sc;

    public static void main(String[] args) {

        player = new Player();
        tiles.genWorld();

        sc = new Scanner(System.in);

        while (!(command = sc.next()).equals("q")) {

            // display world
            printWorld();

            // get input
            switch (command) {
                case "w":
                    up();
                    break;
                case "s":
                    down();
                    break;
                case "a":
                    left();
                    break;
                case "d":
                    right();
                    break;
                default:
                    break;
            }

        }
        // ----- WHILE -----
    }

    static void printWorld() {
        for (int i = 0; i < tiles.height(); ++i) {
            for (int j = 0; j < tiles.width(); ++j) {

            }
        }
    }

    static void up() {
        // check left

    }

    static void down() {

    }

    static void left() {

    }

    static void right() {

    }
}
