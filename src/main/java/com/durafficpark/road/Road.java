package com.durafficpark.road;

import com.durafficpark.Traffic.Car;

import java.util.ArrayList;
import java.util.HashMap;

// the distance to a specific node with the distance for it, start node is the node that holds this
public class Road {

    private Node startNode; // the start node which the road begins at
    private Node endNode;    // the next node which this road ends at
    private double distance;    // the total length of the road, in metres
    private double speedLimit;  // the speed limit for this road, in m/s

    private ArrayList<Car> cars;

    public Road(Node startNode, Node endNode, double distance, double speedLimit){
        this.startNode = startNode;
        this.endNode = endNode;
        this.distance = distance;
        this.speedLimit = speedLimit;
        cars = new ArrayList<>();
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
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

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }
}
