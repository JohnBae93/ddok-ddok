package edu.skku.swp3.ddokddok.models;

import com.google.android.gms.maps.model.LatLng;

public class Restroom {
    private LatLng mLatLng;
    private String mID;
    private int mFloor;
    private int empty;
    private int used;
    private int total;

    public Restroom(String mID, LatLng mLatLng, int mFloor, int total) {
        this.mLatLng = mLatLng;
        this.mFloor = mFloor;
        this.total = total;
        this.empty = total;
        this.used = 0;
    }
    public void personIn(){
        empty--;
        used++;
    }
    public void personOut(){
        empty++;
        used--;
    }

    public LatLng getmLatLng(){
        return mLatLng;
    }
    public String getmID() {
        return mID;
    }
    public int getmFloor() {
        return mFloor;
    }
    public int getEmpty() {
        return empty;
    }
    public int getUsed() {
        return used;
    }
    public int getTotal() {
        return total;
    }
}
