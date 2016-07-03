package com.cs200.checkers;

public class CheckersPosition{
    private int Row;
    private int Col;

    public CheckersPosition() {
        Row = -1;
        Col = -1;
    }
    public CheckersPosition(int row, int col) {
        Row = row;
        Col = col;
    }
    public CheckersPosition(String pos) {
        Row = 8 - (pos.charAt(1) - '0');
        Col = pos.charAt(0) - 'A';
    }
    public void setPosition(int row, int col) {
        Row = row;
        Col = col;
    }
    public int GetRow() {
        return Row;
    }
    public int GetCol() {
        return Col;
    }
    public Boolean Equals (CheckersPosition p) {
        if (Row == p.GetRow() && Col == p.GetCol()) {
            return true;
        } else {
            return false;
        }
    }
}
