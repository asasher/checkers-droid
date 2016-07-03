package com.cs200.checkers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class CheckersBoardView extends SurfaceView implements SurfaceHolder.Callback {

    final int PADDING = 2/160;

    Bitmap boardBitmap;
    int bWidth,bHeight,bX,bY,padX,padY,cellW,cellH;

    Bitmap whitePieceBitmap;
    Bitmap blackPieceBitmap;
    Bitmap whitePieceBitmapCrowned;
    Bitmap blackPieceBitmapCrowned;
    int pWidth,pHeight;

    CheckersBoardThread boardThread;
    SurfaceHolder holder;

    CheckersBoard board;

    boolean isTouched;
    double touchX;
    double touchY;

    CheckersPosition dragStartPos;
    CheckersPosition dragEndPos;

    ArrayList<CheckersPosition> touchPositions;

    MainActivity mainActivity;

    public CheckersBoardView(Context context, AttributeSet attr) {
        super(context, attr);

        mainActivity = (MainActivity)context;

        board = new CheckersBoard();

        Resources res = getResources();
        boardBitmap = BitmapFactory.decodeResource(res,R.drawable.checkers_board);
        whitePieceBitmap = BitmapFactory.decodeResource(res,R.drawable.checkers_piece_white);
        blackPieceBitmap = BitmapFactory.decodeResource(res,R.drawable.checkers_piece_black);
        whitePieceBitmapCrowned = BitmapFactory.decodeResource(res,R.drawable.checkers_piece_white_crowned);
        blackPieceBitmapCrowned = BitmapFactory.decodeResource(res,R.drawable.checkers_piece_black_crowned);

        holder = getHolder();
        holder.addCallback(this);
        boardThread = new CheckersBoardThread(holder,this);

        isTouched = false;
        touchX = 0;
        touchY = 0;

        touchPositions = new ArrayList<CheckersPosition>();

        dragStartPos = null;
        dragEndPos = null;
    }

    public void setBoard(CheckersBoard board)
    {
        this.board = board;
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE); // erase stuff
        drawBoard(canvas);
    }

    public void drawBoard(Canvas canvas) {
        bWidth = boardBitmap.getWidth();
        bHeight = boardBitmap.getHeight();

        pWidth = whitePieceBitmap.getWidth();
        pHeight = whitePieceBitmap.getHeight();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        padX = PADDING*width;
        padY = PADDING*height;

        bX = (width - bWidth)/2;
        bY = (height - bHeight)/2;

        canvas.drawBitmap(boardBitmap,bX,bY,null);

        cellW = bWidth/8;
        cellH = bHeight/8;

        int x;
        int y;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                CheckersPiece p = board.GetPieceAt(r, c);

                x = bX + padX + c*cellW;
                y = bY + padY + r*cellH;

                if(p.getTouched())
                {
                    if(isTouched) {
                        // piece draggin
                        p.setAnimation(touchX,touchY,Animatable.FAST);
                        Log.d("Dragging",touchX + "," + touchY);
                    } else {
                        // piece dropped
                        p.setTouched(false);

                        CheckersMove cMove = new CheckersMove(new CheckersSimpleMove(dragStartPos,dragEndPos));
                        // CheckersMove cMove = new CheckersMove(touchPositions);

                        String msg = dragStartPos.GetRow() + "," + dragStartPos.GetCol();
                        msg += " to ";
                        msg += dragEndPos.GetRow() + "," + dragEndPos.GetCol();

                        /*String msg = "";
                        for (CheckersPosition pos : touchPositions) {
                            msg += pos.GetRow() + "," + pos.GetCol() + " - ";
                        }*/

                        Log.d("Drag Move",msg);

                        mainActivity.makeMove(cMove);
                    }
                } else {
                    p.setAnimation(x,y,Animatable.SLOW);
                }

                x = (int)p.getX();
                y = (int)p.getY();

                if(!p.GetIsNull() && !p.GetIsCaptured()) {
                    if(p.GetIsBlack()) {
                        if(p.GetIsCrowned()) {
                            canvas.drawBitmap(blackPieceBitmapCrowned,x,y,null);
                        } else {
                            canvas.drawBitmap(blackPieceBitmap,x,y,null);
                        }
                    } else {
                        if(p.GetIsCrowned()) {
                            canvas.drawBitmap(whitePieceBitmapCrowned,x,y,null);
                        } else {
                            canvas.drawBitmap(whitePieceBitmap,x,y,null);
                        }

                    }
                }
            }
        }
    }

    public void resize(int width, int height) {
        int size = Math.min(width,height);
        int pSize = size/8;
        boardBitmap = Bitmap.createScaledBitmap(boardBitmap,size,size,true);
        whitePieceBitmap = Bitmap.createScaledBitmap(whitePieceBitmap,pSize,pSize,true);
        blackPieceBitmap = Bitmap.createScaledBitmap(blackPieceBitmap,pSize,pSize,true);
        whitePieceBitmapCrowned = Bitmap.createScaledBitmap(whitePieceBitmapCrowned,pSize,pSize,true);
        blackPieceBitmapCrowned = Bitmap.createScaledBitmap(blackPieceBitmapCrowned,pSize,pSize,true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        touchX = (double)event.getX();
        touchY = (double)event.getY();

        int x;
        int y;
        if(!isTouched) {
            // if not already dragging see if a piece is touched
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    CheckersPiece p = board.GetPieceAt(r, c);
                    x = (int)p.getX();
                    y = (int)p.getY();

                    if(!p.GetIsNull() && !p.GetIsCaptured()) {
                        if(touchX > x && touchX < x + pWidth &&
                                touchY > y && touchY < y + pHeight) {
                            // piece is touched
                            p.setTouched(true);
                            dragStartPos = p.getPosition();
                            // touchPositions = new ArrayList<CheckersPosition>();
                            // AddTouchedPos(p.getPosition());
                            break;
                        }
                    }
                }
            }
        } else {
            // check for potential drop pos
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    x = bX + padX + c*cellW;
                    y = bY + padY + r*cellH;

                    if(touchX > x && touchX < x + pWidth &&
                            touchY > y && touchY < y + pHeight) {
                        //drop pos found
                        dragEndPos = new CheckersPosition(r,c);
                        // AddTouchedPos(new CheckersPosition(r,c));
                        break;
                    }
                }
            }
        }

        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_MOVE) {
            isTouched = true;
        } else {
            isTouched = false;
        }

        return true; // handled
    }

    public void AddTouchedPos(CheckersPosition pos) {
        int numPos = touchPositions.size();
        if(numPos > 0) {
            CheckersPosition lastPos = touchPositions.get(numPos - 1);
            if(!lastPos.Equals(pos)) {
                touchPositions.add(pos);
                if ( board.Validate(new CheckersMove(touchPositions)) > 0)
                {
                    touchPositions.remove(touchPositions.size()-1);
                }
            }
        } else {
            touchPositions.add(pos);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //To change body of implemented methods use File | Settings | File Templates.
        boardThread.setRunning(true);
        boardThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //To change body of implemented methods use File | Settings | File Templates.
        resize(width,height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //To change body of implemented methods use File | Settings | File Templates.
        boolean retry = true;
        boardThread.setRunning(false);
        while (retry) {
            try {
                boardThread.join();
                retry = false;
            } catch (Exception ex) {
                // do nothing
            }
        }

        boardThread = null;
    }
}
