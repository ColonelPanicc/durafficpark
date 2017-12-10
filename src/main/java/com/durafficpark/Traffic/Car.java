package com.durafficpark.Traffic;

import Jama.Matrix;
import com.durafficpark.road.Road;

public class Car {

    protected Matrix pos, pos2;
    protected double length;
    private Road road;
    private int choice;

    public Car(double length, double pos, double vel){
        this.pos = new Matrix(3,1);
        this.pos.set(0,0, pos);
        this.pos.set(1,0,vel);
        this.length = length;
        choice = -1;
    }

    protected void setRoad(Road road){
        this.road = road;
    }

    protected void completePosTransfer(){
        pos = pos2;
    }

    protected Road getRoad(){
        return road;
    }

    protected int getChoice(){
        return choice;
    }

    public double getLength(){
        return length;
    }

    protected void setChoice(int choice){
        this.choice = choice;
    }

}
