package com.cs200.checkers;

import java.util.ArrayList;

public class CheckersBoard {
    private ArrayList<CheckersPiece> White = new ArrayList<CheckersPiece>();
    private ArrayList<CheckersPiece> Black = new ArrayList<CheckersPiece>();
    private CheckersPiece NullPiece = new CheckersPiece();
    int Turn = 1;  // for white 0 for black

    public CheckersBoard() {
        int index = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 8; c++) {
                if ((r % 2) == 0) {
                    if ((c % 2) == 1) {
                        White.add(index, new CheckersPiece(r, c, false));
                        Black.add(index, new CheckersPiece(7-r, 7-c, true));
                        index++;
                    }
                } else {
                    if ((c % 2) == 0) {
                        White.add(index, new CheckersPiece(r, c, false));
                        Black.add(index, new CheckersPiece(7-r, 7-c, true));
                        index++;
                    }
                }
            }
        }
    }

    public CheckersBoard(String blackPos, String whitePos) {
        ArrayList<String> bPos = StringUtils.StringSplit(blackPos, ',');
        ArrayList<String> wPos = StringUtils.StringSplit(whitePos, ',');

        for (int i = 0; i < bPos.size(); i++) {
            Black.add(i,new CheckersPiece(bPos.get(i), true));
        }

        for (int i = 0; i < wPos.size(); i++) {
            White.add(i, new CheckersPiece(wPos.get(i), false));
        }
    }

    public CheckersPiece GetPieceAt(int row, int col) {
        for (CheckersPiece p : White) {
            if (p.GetRow() == row && p.GetCol() == col) {
                return p;
            }
        }
        for (CheckersPiece p : Black) {
            if (p.GetRow() == row && p.GetCol() == col) {
                return p;
            }
        }
        return NullPiece;
    }

    public int GetTurn() {
        return Turn;
    }

    public CheckersPiece GetPieceAt(CheckersPosition pos) {
        return GetPieceAt(pos.GetRow(), pos.GetCol());
    }

    public String ToString() {
        String toReturn = "";
        for (int r = 0; r < 8; r++) {
            toReturn += (8 - r) + " ";
            for (int c = 0; c < 8; c++) {
                CheckersPiece p = GetPieceAt(r, c);
                toReturn += p.ToString();
            }
            toReturn += '\n';
        }
        toReturn += "  ABCDEFGH";
        return toReturn;
    }

    public void SimpleMoveNoKill(CheckersSimpleMove simpleMove) {
        GetPieceAt(simpleMove.GetStart()).SetPosition(simpleMove.GetEnd());
    }

    public void SimpleMove(CheckersSimpleMove simpleMove) {
        int dR = simpleMove.GetStart().GetRow() - simpleMove.GetEnd().GetRow();

        int dC = simpleMove.GetStart().GetCol() - simpleMove.GetEnd().GetCol();

        if (Math.abs(dR) == 2) {
            int mR = simpleMove.GetStart().GetRow() - dR/2;
            int mC = simpleMove.GetStart().GetCol() - dC/2;
            GetPieceAt(mR,mC).SetCaptured();
        }

        SimpleMoveNoKill(simpleMove);

        if (simpleMove.GetEnd().GetRow() <= 0 ||
                simpleMove.GetEnd().GetRow() >= 7) {
            GetPieceAt(simpleMove.GetEnd()).SetCrowned();
        }
    }

    public int Validate(CheckersMove moves) {
        int res = 0;
        // int moveCount = 0;
        ArrayList<CheckersSimpleMove> simpleMoves = moves.GetSimpleMoves();
        for (CheckersSimpleMove m : simpleMoves) {
            res = IsMoveValid(m);
            if (res > 0) {
                break;
            } else {
                // SimpleMoveNoKill(m);
                // moveCount++;
            }
        }
        /*for (int i = 0; i < moveCount; i++) {
            CheckersSimpleMove m = simpleMoves.get(moveCount - 1 - i);
            Log.d("Reverse Move",m.GetStart().GetRow() + "," + m.GetStart().GetCol() + " to " + m.GetEnd().GetRow() + "," + m.GetEnd().GetCol());

            Log.d("Reverse Move",this.ToString());
            CheckersSimpleMove rSMove = new CheckersSimpleMove(m.GetEnd(),m.GetStart());
            Log.d("Reverse Move", this.ToString());

            Log.d("Reverse Move",rSMove.GetStart().GetRow() + "," + rSMove.GetStart().GetCol() + " to " + rSMove.GetEnd().GetRow() + "," + rSMove.GetEnd().GetCol());
            SimpleMoveNoKill(rSMove);
        }*/
        return res;
    }

    public int IsMoveValid(CheckersSimpleMove simpleMove) {
        int res = 0;

        if (Winner() > 0) {
            res = 1;
        } else {
            CheckersPiece startPiece = GetPieceAt(simpleMove.GetStart());
            CheckersPiece endPiece = GetPieceAt(simpleMove.GetEnd());

            if (startPiece.GetIsNull()) {
                // res = 3; // no need
            } else if (!endPiece.GetIsNull()) {
                res = 4;
            } else if ((startPiece.GetIsBlack() && Turn == 1) ||
                    (!startPiece.GetIsBlack() && Turn == 0)) {
                res = 2;
            } else {
                int dCol = simpleMove.GetEnd().GetCol() - simpleMove.GetStart().GetCol();
                int dRow = simpleMove.GetEnd().GetRow() - simpleMove.GetStart().GetRow();

                if (dRow == 0 || dCol == 0 ||
                        Math.abs(dRow) > 2 ||
                        Math.abs(dCol) > 2) {
                    res = 6;
                } else if (Math.abs(dRow) == 2 || Math.abs(dCol) == 2) {
                    int midR = simpleMove.GetStart().GetRow() + dRow/2;
                    int midC = simpleMove.GetStart().GetCol() + dCol/2;
                    CheckersPiece midPiece = GetPieceAt(midR, midC);

                    if (midPiece.GetIsNull() || midPiece.GetIsBlack() == startPiece.GetIsBlack()) {
                        res = 5;
                    }
                } else if (!startPiece.GetIsCrowned()) {
                    if ((startPiece.GetIsBlack() && dRow > 0) ||
                            (!startPiece.GetIsBlack() && dRow < 0)) {
                        res = 6;
                    }
                }
            }
        }
        return res;
    }

    public void Move(CheckersMove checkersMove) {
        for (CheckersSimpleMove m : checkersMove.GetSimpleMoves()) {
            SimpleMove(m);
        }
        if(Turn == 1) {
            Turn = 0;
        } else {
            Turn = 1;
        }
    }

    public int Winner() {
        int res = 0;

        Boolean isBRem = false;
        Boolean isWRem = false;

        for (CheckersPiece b : Black) {
            if (!b.GetIsCaptured()) {
                isBRem = true;
                break;
            }
        }
        for (CheckersPiece w : White) {
            if (!w.GetIsCaptured()) {
                isWRem = true;
                break;
            }
        }

        if (isBRem && !isWRem) {
            res = 1;
        } else if (!isBRem && isWRem) {
            res = 2;
        }

        return res;
    }
}
