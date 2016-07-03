package com.cs200.checkers;

public class CheckersPiece extends Animatable{
    private CheckersPosition Pos;
    private Boolean IsCaptured;
    private Boolean IsCrowned;
    private Boolean IsBlack;
    private Boolean IsNull;

    public CheckersPiece() {
        super();
        Pos = new CheckersPosition(-1, -1);
        IsBlack = true;
        IsCaptured = true;
        IsCrowned = false;
        IsNull = true;
    }

    public CheckersPiece(int row, int col, Boolean isBlack) {
        super();
        Pos = new CheckersPosition(row, col);
        IsBlack = isBlack;
        IsCaptured = false;
        IsCrowned = false;
        IsNull = false;
    }

    public CheckersPiece(String pos, Boolean isBlack) {
        super();
        Pos = new CheckersPosition(pos);
        IsBlack = isBlack;
        IsCaptured = false;
        IsCrowned = false;
        IsNull = false;
    }

    public void SetPosition(int row, int col) {
        Pos.setPosition(row,col);
    }

    public CheckersPosition getPosition() {
        return Pos;
    }

    public  void SetPosition(CheckersPosition pos) {
        // Pos = pos;
        Pos.setPosition(pos.GetRow(),pos.GetCol());
    }

    public void SetCaptured() {
        IsCaptured = true;
    }

    public void SetCrowned() {
        IsCrowned = true;
    }

    public Boolean GetIsNull() {
        return IsNull;
    }
    public Boolean GetIsBlack() {
        return IsBlack;
    }
    public Boolean GetIsCrowned() {
        return IsCrowned;
    }
    public Boolean GetIsCaptured() {
        return IsCaptured;
    }

    public int GetRow() {
        return Pos.GetRow();
    }
    public int GetCol() {
        return Pos.GetCol();
    }

    public String ToString() {
        String toReturn;
        if (IsNull || IsCaptured) {
            toReturn = ".";
        } else {
            if (IsBlack) {
                if (IsCrowned) {
                    toReturn = "B";
                } else {
                    toReturn = "b";
                }
            } else {
                if (IsCrowned) {
                    toReturn = "W";
                } else {
                    toReturn = "w";
                }
            }
        }
        return toReturn;
    }
}
