package com.cs200.checkers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    CheckersBoard b;
    CheckersBoardView cbView;
    Thread drwThread;

    TextView brdTxt;
    TextView msgTxt;
    TextView txtTurn;

    EditText editTxtMoves;

    String msg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent intent = getIntent();
        String wPcs = intent.getStringExtra(CreateBoardActivity.WHITE_PCS);
        String bPcs = intent.getStringExtra(CreateBoardActivity.BLAXK_PCS);

        b = new CheckersBoard();
        if(!(wPcs.isEmpty() || bPcs.isEmpty()))
        {
            b = new CheckersBoard(bPcs,wPcs);
        }

        cbView = (CheckersBoardView)findViewById(R.id.cb_view);
        brdTxt = (TextView)findViewById(R.id.txt_board);
        msgTxt = (TextView) findViewById(R.id.txt_msg);
        txtTurn = (TextView) findViewById(R.id.txt_turn);
        editTxtMoves = (EditText) findViewById(R.id.edit_txt_move);

        msg = "";

        updateView();
    }

    public void updateView() {
        cbView.setBoard(b);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                brdTxt.setText(b.ToString());
                showTurn();
                showMsg(msg);
            }
        });
    }

    public void showMsg(String msg) {
        msgTxt.setText(msg);
    }

    public void showTurn() {
        // set turn txt
        if(b.GetTurn() ==  1) {
            txtTurn.setText("White Player's Turn");
        } else if (b.GetTurn() == 0) {
            txtTurn.setText("Black Player's Turn");
        }
    }

    public void btnClick(View view) {
        EditText editTxtMoves = (EditText) findViewById(R.id.edit_txt_move);
        String moves = editTxtMoves.getText().toString().toUpperCase();

        Log.d("Move Entered", moves);

        if(!moves.isEmpty()) {
            if(moves.split("-").length < 2) {
                showMsg("Enter a valid move >_<");
            } else {
                makeMove(moves);
            }
        } else {
            showMsg("Enter a move >_<");
        }
    }

    public void makeMove(String moves) {
        CheckersMove cMove = new CheckersMove(moves);
        makeMove(cMove);
    }
    public void makeMove(CheckersMove cMove) {
        int moveValidity = b.Validate(cMove);
        if(moveValidity == 0) {
            b.Move(cMove);
            msg = "";
        } else if (moveValidity == 1) {
            int winner = b.Winner();
            if(winner == 1) {
                msg = "Black player has won :)";
            } else if (winner == 2) {
                msg = "White player has won :)";
            }
        } else if (moveValidity == 2) {
            msg = "Wrong piece, its not your turn >_<";
        } else if (moveValidity == 3) {
            msg = "You gotta pick a piece to move >_<";
        } else if (moveValidity == 4) {
            msg = "Can't move there >_<";
        } else if (moveValidity == 5) {
            msg = "Can't jump like that >_<";
        } else {
            msg = "Step too small or too large >_<";
        }
        updateView();
    }

}
