package com.durafficpark.road;

import com.durafficpark.Traffic.Car;
import com.durafficpark.osm.OSMBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;

// the distance to a specific node with the distance for it, start node is the node that holds this
public class Road {

    private Node startNode; // the start node which the road begins at
    private Node endNode;    // the next node which this road ends at
    private double distance;    // the total length of the road, in metres
    private double speedLimit;  // the speed limit for this road, in m/s
    public boolean choice = false;

    private ArrayList<Car> cars;

    public Road(Node startNode, Node endNode, double distance, double speedLimit){
        this.startNode = startNode;
        this.endNode = endNode;
        this.distance = distance * 1000;
        this.speedLimit = speedLimit;
        cars = new ArrayList<>();
        startNode.addRoad(this);
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();

        JSONObject startNodeJSON = new JSONObject();
        startNodeJSON.put("latitude", startNode.getLatitude());
        startNodeJSON.put("longitude", startNode.getLongitude());
        jsonObject.put("startNode", startNodeJSON);

        JSONObject endNodeJSON = new JSONObject();
        endNodeJSON.put("latitude", endNode.getLatitude());
        endNodeJSON.put("longitude", endNode.getLongitude());
        jsonObject.put("endNode", endNodeJSON);

        jsonObject.put("distance", distance);
        jsonObject.put("speedLimit", speedLimit);
        jsonObject.put("cars", "[]");

        return jsonObject;
    }

    /*

    // creates a road based on json data
    public Road(JSONObject jsonObject){
        Document bsonStartNode = (Document) jsonObject.get("startNode");
        Node startNode = new Node((double) bsonStartNode.get("latitude"), (double) bsonStartNode.get("longitude"));

        Document bsonEndNode = (Document) jsonObject.get("endNode");
        Node endNode = new Node((double) bsonEndNode.get("latitude"), (double) bsonEndNode.get("longitude"));

        this.startNode = startNode;
        this.endNode = endNode;

        this.distance = (double) jsonObject.get("distance");
        this.speedLimit = (double) jsonObject.get("speedLimit");

        this.startNode.addRoad(this);

        cars = new ArrayList<>();
    }
    */

    public double getTrafficDensity(){
        double totalLength = 0;
        for(Car car : cars){
            totalLength += car.getLength();
        }
        return totalLength/distance;
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

    public void addCar(Car car){
        cars.add(car);
    }

    public void removeCar(Car car){
        cars.remove(car);
    }
}
