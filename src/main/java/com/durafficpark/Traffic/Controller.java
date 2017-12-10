package com.durafficpark.Traffic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Jama.Matrix;
import com.durafficpark.road.Node;
import com.durafficpark.road.Road;
import com.google.gson.Gson;

public class Controller {

    private List<Car> cars;
    private Map map;
    private final Matrix M;

    private float a,b,c;
    private float dt;
    private float saveGap, runtime;

    private double maxOffset = 150;

    public Controller(float a, float b, float c, float dt, float runtime, float saveGap, float density) {
        cars = new ArrayList<>();
        this.a = a;
        this.b = b;
        this.c = c;
        this.runtime = runtime;
        this.saveGap = saveGap;
        this.dt = dt;
        M = new Matrix(new double[][]{{1, dt, dt * dt / 2},
                {0, 1, dt},
                {0, 0, 0}});

        //TODO setup
        map = new Map(-1.6016, -1.5445, 54.7831, 54.7609);

//        Node node1 = new Node(54.77, -1.57);
//        Node node2 = new Node(54.765, -1.58);
//        Node node3 = new Node(54.765, -1.56);
//        List<Road> roads = new ArrayList<>();
//        node1.addRoad(node2, 100, 13.4);
//        node2.addRoad(node3, 300, 13.4);
//        node2.addRoad(node3, 300, 13.4);
//        node2.addRoad(node3, 300, 13.4);
//        node3.addRoad(node1, 300, 13.4);
//        node3.addRoad(node1, 300, 13.4);
//        node3.addRoad(node1, 300, 13.4);
//        roads.addAll(node1.getAdjacentRoads());
//        roads.addAll(node2.getAdjacentRoads());
//        roads.addAll(node3.getAdjacentRoads());
//        map = new Map(roads);

        for (Road road : map.getAllRoads()) {
            double length = road.getDistance();
            int count = (int) (density * length);
            for (int i = 0; i < count; i++) {
                Car car = new Car(3, Math.random() * length, 0);
                car.setRoad(road);
                cars.add(car);
                road.addCar(car);
            }
        }
    }

    public String[] run(){
        List<String> strings = new ArrayList<>();
        float nextSave = saveGap;
        for(float f = 0; f < runtime; f += dt){
            runStep();
            if(f > nextSave){
                nextSave += saveGap;
                String frame = generateMapRepr(f);
                strings.add(frame);
            }
        }
        return strings.toArray(new String[strings.size()]);
    }

    public static void main(String[] args) {
        Controller cont = new Controller(1, 3, 10, 0.8f, 10, 3, 0.01f);
        System.out.println("constructed");
        cont.run();
    }

    private String generateMapRepr(float time){
        Gson gson = new Gson();
        List<Road> roads = map.getAllRoads();
        double[][] values = new double[roads.size()][5];
        for(int i = 0; i < roads.size(); i++){
            values[i][0] = roads.get(i).getStartNode().getLatitude();
            values[i][1] = roads.get(i).getStartNode().getLongitude();
            values[i][2] = roads.get(i).getEndNode().getLatitude();
            values[i][3] = roads.get(i).getEndNode().getLongitude();
            values[i][4] = roads.get(i).getTrafficDensity();
        }
        return gson.toJson(new JsonOut(values, time));
    }

    private void runStep(){
        cars.parallelStream().forEach(car -> {
            Pair carInfront = getCarInfront(car);
            double s = carInfront.offset + carInfront.car.pos.get(0, 0) - car.pos.get(0, 0) - carInfront.car.length;
            car.pos.set(2, 0, F(car.pos.get(1,0), carInfront.car.pos.get(1,0), s));
        });
        applyMatrix();
        cars.parallelStream().forEach(Car::updatePosition);
    }

    private Pair getCarInfront(Car car){
        Road road = car.getRoad();
        return getNextCarOnRoad(road, 0, car, true);
    }

    private Pair getNextCarOnRoad(Road road, double offset, Car currentCar, boolean choice){
        if(offset > maxOffset){
            return new Pair(new Car(currentCar.length, 0, currentCar.pos.get(1,0)), offset);
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
            Road nextRoad = currentCar.getNextRoad(road, choice);
            if(nextRoad == null){
                return new Pair(new Car(currentCar.length, 0, currentCar.pos.get(1,0)), offset + road.getDistance());
            }
            if(nextRoad.choice) {
                nextRoad.choice = false;
                choice = false;
            }
            return getNextCarOnRoad(nextRoad, offset + road.getDistance(), currentCar, choice);
        }
        return new Pair(closestCar, offset);
    }

//    private Road getNextRoad(Road road, Car currentCar, int[] choiceCount){
//        List<Road> roads = road.getEndNode().getAdjacentRoads();
//        if(roads.size() == 1){
//            return roads.get(0);
//        }else if(roads.size() == 0){
//            return null;
//        }
//        if(choiceCount[0] != 0){
//            return null;
//        }
//        choiceCount[0]++;
//        int choice = currentCar.getChoice();
//        if(choice == -1) {
//            choice = makeChoice(roads, road);
//            currentCar.setChoice(choice);
//        }
//        return roads.get(choice);
//    }

//    private int makeChoice(List<Road> roads, Road road){
//        Node startNode = road.getStartNode();
//        int size = roads.size();
//        int randomElement = 0;
//        for(int i = 0; i < choiceLimit; i++){
//            randomElement = (int)(Math.random() * size);
//            if(!roads.get(randomElement).getEndNode().equals(startNode)){
//                return randomElement;
//            }
//        }
//        return randomElement;
//    }

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

    protected class Pair{
        protected double offset;
        protected Car car;
        protected Pair(Car car, double offset){
            this.car = car;
            this.offset = offset;
        }
    }
}
