package com.durafficpark.Traffic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Jama.Matrix;
import com.durafficpark.road.Node;
import com.durafficpark.road.Road;

public class Controller {

    private List<Car> cars;
    private Map map;
    private final Matrix M;

    private float a,b,c;
    private float dt;

    private double maxOffset = 150;
    private int choiceLimit = 3;

    public Controller(float a, float b, float c, float dt, float runtime, float saveGap, float density){
        cars = new ArrayList<>();
        this.a = a;
        this.b = b;
        this.c = c;
        M = new Matrix(new double[][]{  {1, dt, dt*dt/2},
                                        {0, 1, dt},
                                        {0, 0, 0}});

        //TODO setup
        //map = new Map(left, right, top, bottom);

        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();
        List<Road> roads = new ArrayList<>();
        roads.add(new Road(node1,node2, 100, 13.4));
        roads.add(new Road(node2,node3, 300, 13.4));
        roads.add(new Road(node2,node3, 300, 13.4));
        roads.add(new Road(node2,node3, 300, 13.4));
        roads.add(new Road(node3,node1, 300, 13.4));
        roads.add(new Road(node3,node1, 300, 13.4));
        roads.add(new Road(node3,node1, 300, 13.4));
        map = new Map(roads);

        for(Road road: map.getAllRoads()){
            double length = road.getDistance();
            int count = (int)(density * length);
            for(int i = 0; i < count; i++){
                Car car = new Car(3, Math.random() * length, 0);
                cars.add(car);
                road.addCar(car);
            }
        }

        float nextSave = saveGap;
        for(float f = 0; f < runtime; f += dt){
            runStep();
            if(f > nextSave){
                nextSave += saveGap;
                generateMapRepr(f);
            }
        }
    }

    private generateMapRepr(float time){
        List<Road> roads = map.getAllRoads();
        double[][] values = new double[roads.size()][5];
        for(int i = 0; i < roads.size(); i++){
            values[i][0] = roads.get(i).getStartNode().getLatitude();
            values[i][1] = roads.get(i).getStartNode().getLongitude();
            values[i][2] = roads.get(i).getEndNode().getLatitude();
            values[i][3] = roads.get(i).getEndNode().getLongitude();
            values[i][4] = roads.get(i).getTrafficDensity();
        }

    }

    private void runStep(){
        cars.parallelStream().forEach(car -> {
            Pair carInfront = getCarInfront(car);
            double s = carInfront.offset + carInfront.car.pos.get(0, 0) - car.pos.get(0, 0) - carInfront.car.length;
            car.pos.set(2, 0, F(car.pos.get(1,0), carInfront.car.pos.get(1,0), s));
        });
        applyMatrix();
    }

    private Pair getCarInfront(Car car){
        Road road = car.getRoad();
        return getNextCarOnRoad(road, 0, car, new int[]{0});
    }

    private Pair getNextCarOnRoad(Road road, double offset, Car currentCar, int[] choiceCount){
        if(offset > maxOffset){
            return null;
        }
        List<Car> cars = road.getCars();
        double shortestDist = Double.MAX_VALUE;
        Car closestCar = null;
        for(Car car : cars){
            double dist = offset + car.pos.get(1,0);
            if(dist > currentCar.pos.get(1,0) && dist < shortestDist){
                shortestDist = dist;
                closestCar = car;
            }
        }
        if(closestCar == null){
            Road nextRoad = getNextRoad(road, currentCar, choiceCount);
            if(nextRoad == null){
                return new Pair(new Car(currentCar.length, 0, currentCar.pos.get(1,0)), offset + road.getDistance());
            }
            return getNextCarOnRoad(nextRoad, offset + road.getDistance(), currentCar, choiceCount);
        }
        return new Pair(closestCar, offset);
    }

    private Road getNextRoad(Road road, Car currentCar, int[] choiceCount){
        List<Road> roads = road.getEndNode().getAdjacentRoads();
        if(roads.size() == 1){
            return roads.get(0);
        }else if(roads.size() == 0){
            return null;
        }
        if(choiceCount[0] != 0){
            return null;
        }
        choiceCount[0]++;
        int choice = currentCar.getChoice();
        if(choice == -1) {
            choice = makeChoice(roads, road);
            currentCar.setChoice(choice);
        }
        return roads.get(choice);
    }

    private int makeChoice(List<Road> roads, Road road){
        Node startNode = road.getStartNode();
        int size = roads.size();
        int randomElement = 0;
        for(int i = 0; i < choiceLimit; i++){
            randomElement = (int)(Math.random() * size);
            if(!roads.get(randomElement).getEndNode().equals(startNode)){
                return randomElement;
            }
        }
        return randomElement;
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

    private class Pair{
        private double offset;
        private Car car;
        private Pair(Car car, double offset){
            this.car = car;
            this.offset = offset;
        }
    }
}
