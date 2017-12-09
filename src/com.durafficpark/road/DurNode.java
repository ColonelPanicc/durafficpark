package com.durafficpark.road;

import java.util.ArrayList;
import java.util.HashMap;

// a node on the map, with the available nodes that can be traversed to from this node
public class DurNode {

    private double latitude;
    private double longitude;

    private ArrayList<DurRoad> adjacentRoads;   // an arraylist of the roads which can be traversed from this node

    // constructor, requiring a latitude and longitude value to be set for it
    public DurNode(double lat, double lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    public DurNode[] getAdjacentNodes(){
        
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DurNode
                && (latitude == ((DurNode) obj).latitude) && (longitude == ((DurNode) obj).longitude);
    }
}
