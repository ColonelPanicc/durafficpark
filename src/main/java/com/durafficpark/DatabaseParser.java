package com.durafficpark;

import com.durafficpark.road.MapBuilder;
import com.durafficpark.road.Road;
import com.google.gson.Gson;
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

        // define an empty array list of roads
        ArrayList<Road> roads = new ArrayList<>();

        // for each json object, create a road object which uses this json object
        for (JSONObject jsonObject: jsonObjects)
            roads.add(new Road(jsonObject));

        // return the array list of roads
        return roads;
    }
}
