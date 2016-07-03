package com.cs200.checkers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: Asher
 * Date: 12/6/13
 * Time: 9:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateBoardActivity extends Activity {
    public final static String WHITE_PCS = "com.cs200.checkers.white_pcs";
    public final static String BLAXK_PCS = "com.cs200.checkers.black_pcs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.create_board_layout));
    }

    public void createBoard(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        EditText editTxtWPcs = (EditText) findViewById(R.id.edit_txt_create_w_pcs);
        EditText editTxtBPcs = (EditText) findViewById(R.id.edit_txt_create_b_pcs);

        String wPcs = editTxtWPcs.getText().toString();
        String bPcs = editTxtBPcs.getText().toString();

        intent.putExtra(WHITE_PCS,wPcs);
        intent.putExtra(BLAXK_PCS,bPcs);

        startActivity(intent);
    }
}