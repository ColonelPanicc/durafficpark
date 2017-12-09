package com.durafficpark.road;

import com.durafficpark.Traffic.Car;

import java.util.HashMap;

// the distance to a specific node with the distance for it, start node is the node that holds this
public class DurRoad {

    private DurNode endNode;    // the next node which this road ends at
    private double distance;    // the total length of the road, in metres
    private double speedLimit;  // the speed limit for this road, in m/s

    private HashMap<Car, Double> cars;  // the cars on the road, with (key, displacement) values

    public DurRoad(DurNode endNode, double distance, double speedLimit){
        this.endNode = endNode;
        this.distance = distance;
        this.speedLimit = speedLimit;
        cars = new HashMap<>();
    }

    public DurNode getEndNode() {
        return endNode;
    }

    public void setEndNode(DurNode endNode) {
        this.endNode = endNode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public HashMap<Car, Double> getCars() {
        return cars;
    }

    public void setCars(HashMap<Car, Double> cars) {
        this.cars = cars;
    }
}
