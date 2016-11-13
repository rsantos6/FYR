package com.github.fyr;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RussBuss on 11/5/2016.
 */
public class UserProfile implements Parcelable {
    public String pace;//this String will only be used for the review page
    public String terrain;//this String will only be used for the review page
    public String distance;//this String will only be used for the review page
    public String paceForMatch;
    public String terrainForMatch;
    public String distanceForMatch;

    public UserProfile(){

    }

    public UserProfile(Parcel in) {
        pace = in.readString();
        terrain = in.readString();
        distance = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) { return new UserProfile[size];
        }
    };

    public void setPace(String newPace){
        this.pace = newPace;
    }

    public void setTerrain(String newTerrain){
        this.terrain = newTerrain;
    }

    public void setDistance(String newDistance){
        this.distance = newDistance;
    }

    public String getPace(){
        return this.pace;
    }

    public String getTerrain(){
        return this.terrain;
    }

    public String getDistance(){
        return this.distance;
    }

    public boolean isEqual(Object o){
        return (((UserProfile) o).getDistance().equals(this.distance)) && (((UserProfile) o).getPace().equals(this.pace)) && (((UserProfile) o).getTerrain().equals(this.terrain));
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

