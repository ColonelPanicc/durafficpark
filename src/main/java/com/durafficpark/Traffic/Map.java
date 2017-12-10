package com.durafficpark.Traffic;

import com.durafficpark.DatabaseParser;
import com.durafficpark.road.Road;

import java.util.List;

public class Map {

    private List<Road> roads;

    public Map(double left, double right, double top, double bottom){
        roads = DatabaseParser.getBoxData(left, right, top, bottom);
    }

    public List<Road> getAllRoads(){
        return roads;
    }

    public List<Road> getRoadChoices(Road road){
        return road.getEndNode().getAdjacentRoads();
    }

}
