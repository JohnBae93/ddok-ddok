package edu.skku.swp3.ddokddok.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import edu.skku.swp3.ddokddok.models.Building;
import edu.skku.swp3.ddokddok.models.Restroom;

public class BuildingHandler {      // 데이터베이스 대신 모든 빌딩 데이터를 담을 공간
    ArrayList<Building> mEveryBuilding;
    public BuildingHandler() {
        mEveryBuilding = new ArrayList<>();

        // 빌딩 정보를 담아 초기화하기 ////////////////////////////////////////////////////////////////
        String buildingName = "제2공학관 27";
        LatLng loc_27 = new LatLng(37.295192, 126.977455);
        int max_floor_27 = 5;
        Building building_27 = new Building(buildingName, loc_27, max_floor_27);

        int floor=1;
        int num_room = 5;
        char gender='M';
        int num=1;
        LatLng restroom_loc = new LatLng(37.295192, 126.977455);
        String restroomID = buildingName+"_f"+String.valueOf(floor)+"_"+String.valueOf(gender)+String.valueOf(num);
        Restroom f1_man_1 = new Restroom(restroomID, restroom_loc, floor, num_room);
        building_27.add_restroom(floor, f1_man_1);

        mEveryBuilding.add(building_27);
    }
    public ArrayList<Building> getClosestBuildings(LatLng current, int dist){  // dist in meter
        ArrayList<Building> closeBuildings = new ArrayList<>();
        for(Building building : mEveryBuilding){
            float[] f = new float[3];
            Location.distanceBetween(current.latitude, current.longitude, building.getmLatLng().latitude, building.getmLatLng().longitude, f);
            if(f[0] < dist){
                closeBuildings.add(building);
            }
        }
        return closeBuildings;
    }
}
