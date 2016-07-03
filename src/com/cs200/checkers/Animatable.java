package com.cs200.checkers;

/**
 * Created with IntelliJ IDEA.
 * User: Asher
 * Date: 12/18/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Animatable {

    // all values are 0-1

    double toX;
    double toY;
    double X;
    double Y;

    double speedFactor;

    public static double SLOW = 0.1;
    public static double FAST = 0.8;

    boolean isTouched;

    public Animatable() {
        X = Math.random()*1000 - 500;
        Y = Math.random()*1000 - 500;
        toX = 0;
        toY = 0;
        isTouched = false;
        speedFactor = SLOW;
    }

    public void setTouched(boolean touched) {
        isTouched = touched;
    }

    public boolean getTouched() {
        return isTouched;
    }

    public void setAnimation(double x,double y,double speed) {
        toX = x;
        toY = y;
        speedFactor = speed;
    }

    public double getX() {
        X += speedFactor*(toX - X);
        return X;
    }

    public double getY() {
        Y += 0.1*(toY - Y);
        return Y;
    }

}
