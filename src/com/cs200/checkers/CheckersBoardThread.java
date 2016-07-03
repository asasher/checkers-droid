package com.cs200.checkers;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Asher
 * Date: 12/17/13
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
class CheckersBoardThread extends Thread {
    SurfaceHolder holder;
    CheckersBoardView boardView;

    Canvas canvas;

    boolean isRunning;

    public CheckersBoardThread(SurfaceHolder holder, CheckersBoardView boardView)
    {
        this.holder = holder;
        this.boardView = boardView;
        isRunning = false;
        canvas = null;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public  void run() {
        while (isRunning) {
            canvas = null;

            try {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                	boardView.draw(canvas);
                }
            } catch (Exception ex) {
                // do nothing
            } finally {
                if(canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

        }

    }

}
