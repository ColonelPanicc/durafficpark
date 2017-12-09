package com.durafficpark.Traffic;

import java.util.ArrayList;
import java.util.List;
import Jama.Matrix;
import com.durafficpark.road.Road;

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
        M = new Matrix(new double[][]{  {1, dt, dt*dt/2},
                                        {0, 1, dt},
                                        {0, 0, 0}});
    }

    private void runStep(){
        cars.parallelStream().forEach(car -> {
            Car carInfront = getCarInfront(car);
            double s = carInfront.pos.get(0, 0) - car.pos.get(0, 0) - carInfront.length;
            car.pos.set(2, 0, F(car.pos.get(1,0), carInfront.pos.get(1,0), s));
        });
        applyMatrix();
    }

    private Car getCarInfront(Car car){
        //TODO get Car
        Road road = car.getRoad();
        List<Car> cars = road.getCars();
        return null;
    }

    private double F(double v, double vFollowing, double s){
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
