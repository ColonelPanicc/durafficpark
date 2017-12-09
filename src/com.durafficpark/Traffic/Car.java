package com.durafficpark.Traffic;

import Jama.Matrix;

public class Car {

    protected Matrix pos, pos2;

    public Car(){
        pos = new Matrix(3,1);
    }

    protected void completePosTransfer(){
        pos = pos2;
    }

}
