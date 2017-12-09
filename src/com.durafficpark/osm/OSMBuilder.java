package com.durafficpark.osm;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Set;

// creates an OSMObject using a json file / element
public class OSMBuilder {

    // builds a B I G arraylist of OSM objects from a B I G arraylist of json objects
    public static ArrayList<OSMObject> buildAll(ArrayList<JSONObject> jsonObjects){

        // initialise an empty array list, to hold the osm objects
        ArrayList<OSMObject> osmObjects = new ArrayList<>();

        // iterate over the json objects, building each of them to their equivalent OSM object, and storing them
        for (JSONObject jsonObject : jsonObjects)
            osmObjects.add(build(jsonObject));

        return osmObjects;
    }

    // builds a single OSM object from a given json object
    public static OSMObject build(JSONObject jsonObject){
        if(jsonObject.containsKey("nodes"))
            return buildWay(jsonObject);
        return buildNode(jsonObject);
    }

    // returns the equivalent osm way for the data in a json object
    private static OSMWay buildWay(JSONObject jsonObject){

        // instantiate the way, using the id found in the json object
        OSMWay way = new OSMWay((String) jsonObject.get("id"));

        // get the array of ordered node id's which form the osm way
        JSONArray nodes = (JSONArray) jsonObject.get("nodes");

        // iterate over each node in the array, casting it as a string (because it is one) and then adding it to the way
        for(Object node : nodes)
            way.getNodes().add((String) node);

        return way;
    }

    // returns the equivalent osm node object for the data in a json object
    private static OSMNode buildNode(JSONObject jsonObject){

        // instantiate the node, using the id, latitude, and longitude found in the json object
        OSMNode node = new OSMNode((String) jsonObject.get("id"),
                (Double) jsonObject.get("latitude"), (Double) jsonObject.get("longitude"));

        // get the json object for the node's tags
        JSONObject tags = (JSONObject) jsonObject.get("tags");

        // iterate over the keys found in the tags object, adding their (key, val) pair to the node's tags field
        for(Object tagKey : tags.keySet().toArray())
            node.getTags().put((String) tagKey, tags.get(tagKey));

        return node;
    }
}
