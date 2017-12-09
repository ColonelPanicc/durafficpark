package com.durafficpark.Traffic;

import java.util.ArrayList;
import java.util.List;
import Jama.Matrix;

public class Controller {

    private List<Car> cars;
    private Map map;

    private float a,b,c;

    public Controller(float a, float b, float c){
        cars = new ArrayList<Car>();
        this.a = a;
        this.b = b;
        this.c = c;
    }

    private float F(float v, float vFollowing, float s){
        return a*(Math.abs(s) - (c * v)) + b*(vFollowing - v);
    }

}
