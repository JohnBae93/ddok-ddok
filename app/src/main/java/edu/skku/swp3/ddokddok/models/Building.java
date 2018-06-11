package edu.skku.swp3.ddokddok.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Building {
    private HashMap<Integer, HashMap<String, Restroom>> mRestInfo;   // <floor, <RestroomID, Location>>
    private String mName;       // 빌딩 이름
    private LatLng mLatLng;     // 빌딩 위치
    private int mMaxFloor;

    public Building(String mName, LatLng mLatLng, int mMaxFloor) {
        this.mRestInfo = new HashMap<>();
        this.mName = mName;
        this.mLatLng = mLatLng;
        this.mMaxFloor = mMaxFloor;
    }

//    public Building(){}
    public int getAvailableTotal(){
        int cnt=0;
        for(Integer floor : mRestInfo.keySet()){
            for(String restroomID : mRestInfo.get(floor).keySet()){
                cnt+=mRestInfo.get(floor).get(restroomID).getEmpty();
            }
        }
        return cnt;
    }
    public void add_restroom(int floor, Restroom restroomINFO){
        if(mRestInfo.get(floor)==null){
            HashMap<String, Restroom> temp_restroomINFO = new HashMap<>();
            temp_restroomINFO.put(restroomINFO.getmID(), restroomINFO);
            mRestInfo.put(floor, temp_restroomINFO);
        }else{
//            HashMap<String, Restroom> temp = mRestInfo.get(floor);
            mRestInfo.get(floor).put(restroomINFO.getmID(), restroomINFO);
        }
    }

    public HashMap<Integer, HashMap<String, Restroom>> getmRestInfo() {
        return mRestInfo;
    }
    public String getmName() {
        return mName;
    }
    public LatLng getmLatLng() {
        return mLatLng;
    }
    public int getmMaxFloor() {
        return mMaxFloor;
    }
}
