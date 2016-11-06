package com.github.fyr;

/**
 * Created by RussBuss on 11/5/2016.
 */
public class DataToday {
    protected String pace;
    protected String terrain;
    protected int distance;

    protected DataToday(){

    }

    protected void setPace(String newPace){
        this.pace = newPace;
    }

    protected void setTerrain(String newTerrain){
        this.terrain = newTerrain;
    }

    protected void setDistance(int newDistance){
        this.distance = newDistance;
    }

    protected String getPace(){
        return this.pace;
    }

    protected String getTerrain(){
        return this.terrain;
    }

    protected int getDistance(){
        return this.distance;
    }

    protected boolean isEqual(Object o){
        return (((DataToday) o).getDistance() == this.distance) && (((DataToday) o).getPace().equals(this.pace)) && (((DataToday) o).getTerrain().equals(this.terrain));
    }

}
