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

    public static ArrayList<Location> getDefaultLocationList(Context context, String gender) {
        final Double[] location_21 = {37.293703, 126.976147};
        final Double[] location_27 = {37.295192, 126.977455};
        final Double[] location_31 = {37.294408, 126.974620};
        final Double[] location_33 = {37.291600, 126.977216};
        final Double[] location_85 = {37.295910, 126.975753};
        ArrayList<Location> locationList = new ArrayList<>();

        ArrayList<Double[]> locationValueList = new ArrayList<>(Arrays.asList(location_21, location_27, location_31, location_33, location_85));
        String[] locationNames = context.getResources().getStringArray(R.array.location_name);

        ArrayList<String> roomList21;
        ArrayList<String> roomList27 = new ArrayList<>();
        ArrayList<String> roomList271;
        ArrayList<String> roomList272;
        ArrayList<String> roomList273;
        ArrayList<String> roomList31;
        ArrayList<String> roomList33;
        ArrayList<String> roomList85;

        if (gender.equals("male")) {
            roomList21 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_21_M)));
            roomList271 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_27_1_M)));
            roomList272 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_27_2_M)));
            roomList273 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_27_3_M)));
            roomList31 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_31_M)));
            roomList33 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_33_M)));
            roomList85 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_85_M)));
        } else {
            roomList21 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_21_F)));
            roomList271 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_27_1_F)));
            roomList272 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_27_2_F)));
            roomList273 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_27_3_F)));
            roomList31 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_31_F)));
            roomList33 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_33_F)));
            roomList85 = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.room_list_85_F)));
        }
        roomList27.addAll(roomList271);
        roomList27.addAll(roomList272);
        roomList27.addAll(roomList273);
        ArrayList<ArrayList<String>> roomList = new ArrayList<>(Arrays.asList(roomList21, roomList27, roomList31, roomList33, roomList85));

        for (int i = 0; i < locationNames.length; i++) {
            LatLng latLng = new LatLng(locationValueList.get(i)[0], locationValueList.get(i)[1]);
            locationList.add(new Location(latLng, locationNames[i], roomList.get(i)));
        }
        return locationList;
    }
}
