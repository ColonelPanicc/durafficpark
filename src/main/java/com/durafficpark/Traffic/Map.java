package com.durafficpark.Traffic;

import com.durafficpark.road.Road;

import java.util.List;

public class Map {

    private List<Road> roads;

    public Map(){
        //TODO get roads from Sara
    }

    public List<Road> getRoadChoices(Road road){
        return road.getEndNode().getAdjacentRoads();
    }

}
