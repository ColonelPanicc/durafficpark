package com.durraficpark.osm;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class OSMWay extends OSMObject{

    private ArrayList<String> nodes;

    public OSMWay(String id){
        super(id);
        this.nodes = new ArrayList<>();
    }

    public ArrayList<String> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<String> nodes) {
        this.nodes = nodes;
    }

    @Override
    public JSONObject getJSON() {
        try {
            JSONObject wayJSON = new JSONObject();
            JSONParser parser = new JSONParser();

            wayJSON.put("id", id);

            JSONArray nodesJSON = new JSONArray();

            for (String node : nodes)
                nodesJSON.add(node);

            wayJSON.put("nodes", nodesJSON);

            wayJSON.put("tags", parser.parse(gson.toJson(tags)));

            return wayJSON;
        }
        catch (ParseException e){
            System.err.println("Error caught in parsing gson object");
        }
        return null;
    }
}
