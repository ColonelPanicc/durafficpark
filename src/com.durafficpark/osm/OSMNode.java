package com.durafficpark.osm;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Reader;


public class OSMNode extends OSMObject {

    private double lat;
    private double lon;

    public OSMNode(String id, double lat, double lon){
        super(id);
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public JSONObject getJSON() {
        try {
            JSONObject nodeJSON = new JSONObject();
            JSONParser parser = new JSONParser();

            nodeJSON.put("id", id);
            nodeJSON.put("lat", lat);
            nodeJSON.put("lon", lon);
            nodeJSON.put("tags", parser.parse(gson.toJson(tags)));
            return nodeJSON;
        }
        catch (ParseException e){
            System.err.println("Error caught in parsing gson object");
            return null;
        }
    }
}
