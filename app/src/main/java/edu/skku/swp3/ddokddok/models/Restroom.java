package edu.skku.swp3.ddokddok.models;

import com.google.android.gms.maps.model.LatLng;

public class Restroom {
    private LatLng mLatLng;
    private String mID;
    private int mBID;
    private int mFloor;
    private int empty;
    private int used;
    private int total;
    private int gender;  // 여: 1, 남: 2

    public Restroom(String mID, int mBID, int mFloor, int total, int empty, int used, int gender, LatLng mLatLng) {
        this.mLatLng = mLatLng;
        this.mID = mID;
        this.mBID = mBID;
        this.mFloor = mFloor;
        this.empty = empty;
        this.used = used;
        this.total = total;
        this.gender = gender;
    }

    public Restroom(){}

    public void setGender(int gender) {
        this.gender = gender;
    }
    public void setmLatLng(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }
    public void setmID(String mID) {
        this.mID = mID;
    }
    public void setmBID(int mBID) {
        this.mBID = mBID;
    }
    public void setmFloor(int mFloor) {
        this.mFloor = mFloor;
    }
    public void setEmpty(int empty) {
        this.empty = empty;
    }
    public void setUsed(int used) {
        this.used = used;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    public LatLng getmLatLng(){
        return mLatLng;
    }
    public int getGender() {
        return gender;
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
    public int getmBID() {
        return mBID;
    }

    public void personIn(){
        empty--;
        used++;
    }
    public void personOut(){
        empty++;
        used--;
    }
}
