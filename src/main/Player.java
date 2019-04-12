package main;

import java.io.Serializable;

public class Player implements Serializable {

    private static final String string = "MM";
    private static final ConsoleCode color = ConsoleCode.WHITE_BOLD_BRIGHT;

    private int row, col;
    boolean alive = true;

    Player() {
        row = col = 0;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void move(Direction d) {
        switch (d) {
            case UP:
                row--;
                break;
            case DOWN:
                row++;
                break;
            case LEFT:
                col--;
                break;
            case RIGHT:
                col++;
                break;
        }
    }

    @Override
    public String toString() {
        return color + string + ConsoleCode.RESET;
    }
}
