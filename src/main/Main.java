package main;

import java.util.Scanner;

public class Main {

    static Player player;

    public static void main(String[] args) {

        player = new Player();

        Scanner sc = new Scanner(System.in);
        String command;

        String output;

        while (!(command = sc.next()).equals("q")) {
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

    static void up() {

    }

    static void down() {

    }

    static void left() {

    }

    static void right() {

    }
}
