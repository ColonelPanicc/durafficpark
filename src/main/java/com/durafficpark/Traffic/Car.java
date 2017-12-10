package com.durafficpark.Traffic;

import Jama.Matrix;
import com.durafficpark.road.Road;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Car {

    protected Matrix pos, pos2;
    protected double length;
    private Road road;
    private int choice;

    public Car(double length, double pos, double vel){
        this.pos = new Matrix(3,1);
        this.pos.set(0,0, pos);
        this.pos.set(1,0,vel);
        this.length = length;
        choice = -1;
    }

    protected void setRoad(Road road){
        this.road = road;
    }

    protected void completePosTransfer(){
        pos = pos2;
    }

    protected Road getRoad(){
        return road;
    }

    protected int getChoice(){
        return choice;
    }

    public double getLength(){
        return length;
    }

    protected void setChoice(int choice){
        this.choice = choice;
    }

    protected void updatePosition(){
        if(pos.get(0,0) > road.getDistance()){
            double position = pos.get(0,0);
            pos.set(0,0, position - road.getDistance());
            road = getNextRoad(road, true);
            if(road.choice){
                road.choice = false;
                choice = -1;
            }
        }
    }

    protected Road getNextRoad(Road currentRoad, boolean choiceAllowed){
        List<Road> roads = currentRoad.getEndNode().getAdjacentRoads();
        List<Road> filtered = roads.stream().filter(road1 -> road1.getEndNode().equals(currentRoad.getStartNode())).collect(Collectors.toList());
        if(filtered.size() == 0){
            if(roads.size() == 0){
                return null;
            }
            if(roads.size() == 1){
                return roads.get(0);
            }
            if(!choiceAllowed){
                return null;
            }
            if(choice == -1){
                choice = makeChoice(roads.size());
            }
            Road ret = roads.get(choice);
            ret.choice = true;
            return ret;
        }else if(filtered.size() == 1){
            return filtered.get(0);
        }
        if(!choiceAllowed){
            return null;
        }
        if(choice == -1){
            choice = makeChoice(filtered.size());
        }
        Road ret = filtered.get(choice);
        ret.choice = true;
        return ret;
    }

    private int makeChoice(int max){
        return (int)(Math.random() * max);
    }

}
