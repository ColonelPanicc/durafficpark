package com.durafficpark;

import com.durafficpark.road.MapBuilder;
import com.durafficpark.road.Node;
import com.durafficpark.road.Road;
import com.google.gson.Gson;
import org.bson.Document;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

// class which gets the formatted data within a bounding box
public class DatabaseParser {

    // gets the data within a specified box
    public static ArrayList<Road> getBoxData(double btm, double tp, double rght, double lft){

        // define an empty array list which will be used to hold all of the json objects within this bounding box
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();

        // add all of the bounding box json objects (roads)
        jsonObjects.addAll(new BoundingBoxNodes().getWithinBoundingBox(btm, tp, rght, lft));

        // define an empty array list of nodes
        ArrayList<Node> nodes  = new ArrayList<>();

        // define an empty array list of roads
        ArrayList<Road> roads = new ArrayList<>();

        // for each json object, create a road object which uses this json object
        for (JSONObject jsonObject: jsonObjects) {

            Node startNode = new Node((Document) jsonObject.get("startNode"));
            Node endNode = new Node((Document) jsonObject.get("startNode"));

            // if we already have stored this osm node (matching lat and lon), then we just need to update...
            if(nodes.contains(startNode)){
                startNode = nodes.get(nodes.indexOf(startNode));
                System.out.println("Node adjacent roads; "+startNode.getAdjacentRoads().size());
            }

            // otherwise, this is the first time we have processed this osm node and so we should save it
            else {
                nodes.add(startNode);
            }

            // same as above!
            if(nodes.contains(startNode)){
                startNode = nodes.get(nodes.indexOf(startNode));
                System.out.println("Node adjacent roads; "+startNode.getAdjacentRoads().size());
            }
            else {
                nodes.add(startNode);
            }

            double roadDistance = (double) jsonObject.get("distance");
            double speedLimit = (double) jsonObject.get("speedLimit");

            Road r = new Road(startNode, endNode, roadDistance, speedLimit);
            roads.add(r);
        }

        // return the array list of roads
        return roads;
    }
}
