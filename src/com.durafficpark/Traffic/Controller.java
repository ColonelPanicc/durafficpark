package com.durafficpark.Traffic;

import java.util.ArrayList;
import java.util.List;
import Jama.Matrix;

public class Controller {

    private List<Car> cars;
    private Map map;
    private final Matrix M;

    private float a,b,c;
    private float dt;

    public Controller(float a, float b, float c, float dt){
        cars = new ArrayList<>();
        this.a = a;
        this.b = b;
        this.c = c;
        M = new Matrix(new double[][]{{1, dt, dt*dt/2},
                                        {0, 1, dt},
                                        {0, 0, 0}});

    }

    private void runStep(){
        //TODO get acceleration for each vehicle
        applyMatrix();
    }

    private float F(float v, float vFollowing, float s){
        return a*(Math.abs(s) - (c * v)) + b*(vFollowing - v);
    }

    private void applyMatrix(){
        cars.parallelStream().forEach(this::applyMatrixInd);
        cars.parallelStream().forEach(Car::completePosTransfer);
    }

    private void applyMatrixInd(Car car){
        car.pos2 = M.times(car.pos);
    }

}
