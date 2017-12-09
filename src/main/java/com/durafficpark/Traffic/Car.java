package com.durafficpark.Traffic;

import Jama.Matrix;

public class Car {

    protected Matrix pos, pos2;
    protected double length;

    public Car(double length){
        pos = new Matrix(3,1);
        this.length = length;
    }

    protected void completePosTransfer(){
        pos = pos2;
    }

}
