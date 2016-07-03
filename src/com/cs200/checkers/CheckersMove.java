package com.cs200.checkers;

import java.util.ArrayList;

public class CheckersMove {
    ArrayList<CheckersSimpleMove> SimpleMoves = new ArrayList<CheckersSimpleMove>();

    public CheckersMove() {
        SimpleMoves = new ArrayList<CheckersSimpleMove>();
    }

    public CheckersMove(String simpleMoves) {
        ArrayList<String> moves = StringUtils.StringSplit(simpleMoves,'-');
        String prev = "";
        for (String m : moves) {
            if (!prev.isEmpty()) {
                CheckersSimpleMove sMove = new CheckersSimpleMove(prev,m);
                SimpleMoves.add(sMove);
                prev = m;
            } else {
                prev = m;
            }
        }
    }

    public CheckersMove(ArrayList<CheckersPosition> positions) {
        CheckersPosition prev = null;
        for (CheckersPosition pos : positions) {
            if(prev != null) {
                CheckersSimpleMove sMove = new CheckersSimpleMove(prev,pos);
                SimpleMoves.add(sMove);
                prev = pos;
            } else {
                prev = pos;
            }
        }
    }

    public CheckersMove(CheckersSimpleMove simpleMove) {
        SimpleMoves.add(simpleMove);
    }

    public ArrayList<CheckersSimpleMove> GetSimpleMoves() {
        return SimpleMoves;
    }

}

