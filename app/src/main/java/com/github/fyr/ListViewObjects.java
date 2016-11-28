package com.github.fyr;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RussBuss on 11/27/2016.
 */

public class ListViewObjects implements Parcelable {
    public String email;
    public String name;

    protected ListViewObjects(Parcel in) {
        email = in.readString();
        name = in.readString();
    }

    public static final Creator<ListViewObjects> CREATOR = new Creator<ListViewObjects>() {
        @Override
        public ListViewObjects createFromParcel(Parcel in) {
            return new ListViewObjects(in);
        }

        @Override
        public ListViewObjects[] newArray(int size) {
            return new ListViewObjects[size];
        }
    };

    public ListViewObjects() {

    }

    public void setEmail(String e){
        this.email = e;
    }
    public String getEmail(){
        return this.email;
    }
    public void setName(String n){
        this.name = n;
    }
    public String getName(){
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(name);
    }
}
