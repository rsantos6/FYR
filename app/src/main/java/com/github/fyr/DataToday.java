package com.github.fyr;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RussBuss on 11/5/2016.
 */
public class DataToday implements Parcelable {
    protected String pace;//this String will only be used for the review page
    protected String terrain;//this String will only be used for the review page
    protected String distance;//this String will only be used for the review page
    protected String paceForMatch;
    protected String terrainForMatch;
    protected String distanceForMatch;

    protected DataToday(){

    }

    protected DataToday(Parcel in) {
        pace = in.readString();
        terrain = in.readString();
        distance = in.readString();
    }

    public static final Creator<DataToday> CREATOR = new Creator<DataToday>() {
        @Override
        public DataToday createFromParcel(Parcel in) {
            return new DataToday(in);
        }

        @Override
        public DataToday[] newArray(int size) {
            return new DataToday[size];
        }
    };

    protected void setPace(String newPace){
        this.pace = newPace;
    }

    protected void setTerrain(String newTerrain){
        this.terrain = newTerrain;
    }

    protected void setDistance(String newDistance){
        this.distance = newDistance;
    }

    protected String getPace(){
        return this.pace;
    }

    protected String getTerrain(){
        return this.terrain;
    }

    protected String getDistance(){
        return this.distance;
    }

    protected boolean isEqual(Object o){
        return (((DataToday) o).getDistance().equals(this.distance)) && (((DataToday) o).getPace().equals(this.pace)) && (((DataToday) o).getTerrain().equals(this.terrain));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pace);
        parcel.writeString(terrain);
        parcel.writeString(distance);
    }
}
