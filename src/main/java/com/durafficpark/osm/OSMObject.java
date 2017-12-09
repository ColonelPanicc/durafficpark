package com.durafficpark.osm;

import com.google.gson.Gson;
import org.json.simple.JSONObject;

import java.util.HashMap;

public abstract class OSMObject {

    protected String id;
    protected Gson gson;
    protected HashMap<String, Object> tags;

    protected OSMObject(String id){
        this.id = id;
        this.tags = new HashMap<>();
        gson = new Gson();
    }

    public HashMap<String, Object> getTags() {
        return tags;
    }

    public abstract JSONObject getJSON();

}
