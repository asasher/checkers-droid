package com.cs200.checkers;

public class CheckersSimpleMove {
    private CheckersPosition Start;
    private CheckersPosition End;

    public CheckersSimpleMove(String start, String end) {
        Start = new CheckersPosition(start);
        End = new CheckersPosition(end);
    }

    public CheckersSimpleMove(CheckersPosition start, CheckersPosition end) {
        Start = start;
        End = end;
    }

    public CheckersPosition GetStart() {
        return Start;
    }
    public CheckersPosition GetEnd() {
        return End;
    }
}
