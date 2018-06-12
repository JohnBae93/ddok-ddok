package edu.skku.swp3.ddokddok.models;

import java.util.HashMap;

/**
 * Created by John on 2018-06-12.
 */

public class SensorState {
    private static SensorState instance = null;

    private HashMap<String, Boolean> fire;

    public static SensorState getInstance() {
        if (instance == null) {
            instance = new SensorState();
            instance.fire = new HashMap<>();
        }
        return instance;
    }

    public Boolean getState(String key) {
        try{
            return this.fire.get(key);
        }catch (Exception e){
            return Boolean.FALSE;
        }
    }

    public void setState(String key, Boolean value) {
        this.fire.put(key, value);
    }
}
