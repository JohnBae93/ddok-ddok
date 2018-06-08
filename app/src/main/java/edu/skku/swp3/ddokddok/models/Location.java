package edu.skku.swp3.ddokddok.models;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import edu.skku.swp3.ddokddok.R;

public class Location {
    private LatLng latLng;
    private String name;
    private ArrayList<String> roomList;

    public Location(LatLng latLng, String name, ArrayList<String> roomList) {
        this.latLng = latLng;
        this.name = name;
        this.roomList = roomList;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getRoomList() {
        return roomList;
    }

    public void setRoomList(ArrayList<String> roomList) {
        this.roomList = roomList;
    }

    public static ArrayList<Location> getDefaultLocationList(Context context) {
        final Double[] location_21 = {37.293703, 126.976147};
        final Double[] location_27 = {37.295192, 126.977455};
        final Double[] location_31 = {37.294408, 126.974620};
        final Double[] location_33 = {37.291600, 126.977216};
        final Double[] location_85 = {37.295910, 126.975753};
        ArrayList<Location> locationList = new ArrayList<>();

        ArrayList<Double[]> locationValueList = new ArrayList<>(Arrays.asList(location_21, location_27, location_31, location_33, location_85));
        String[] locationNames = context.getResources().getStringArray(R.array.location_name);
        ArrayList<String> roomList21 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_21)));
        ArrayList<String> roomList27 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_27)));
        ArrayList<String> roomList31 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_31)));
        ArrayList<String> roomList33 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_33)));
        ArrayList<String> roomList85 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_85)));
        ArrayList<ArrayList<String>> roomList = new ArrayList<>(Arrays.asList(roomList21,roomList27,roomList31,roomList33,roomList85));
        for (int i = 0; i < locationNames.length; i++) {
            LatLng latLng = new LatLng(locationValueList.get(i)[0], locationValueList.get(i)[1]);
            locationList.add(new Location(latLng, locationNames[i],roomList.get(i)));
        }
        return locationList;
    }
}
