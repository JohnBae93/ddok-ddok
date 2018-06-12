package edu.skku.swp3.ddokddok.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import edu.skku.swp3.ddokddok.models.Building;
import edu.skku.swp3.ddokddok.models.Restroom;

public class DBHelper extends SQLiteOpenHelper {
    private static String TAG = "DBHelper";

    private Context context;
    private static DBHelper sInstance;

    public DBHelper(Context context){
        super(context, "ddok-ddok", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");

        StringBuffer buildSTB = new StringBuffer();
        buildSTB.append(" CREATE TABLE BUILDING (");
        buildSTB.append(" ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        buildSTB.append(" NAME TEXT NOT NULL,");
        buildSTB.append(" MAXFLOOR INTEGER NOT NULL,");
        buildSTB.append(" LATITUDE REAL NOT NULL,");
        buildSTB.append(" LONGITUDE REAL NOT NULL);");
        sqLiteDatabase.execSQL(buildSTB.toString());
        Log.d(TAG, "CREATED: BUILDING TABLE");
//        Toast.makeText(context, "BUILDING TABLE 생성완료", Toast.LENGTH_SHORT).show();

        StringBuffer restSTB = new StringBuffer();
        restSTB.append(" CREATE TABLE RESTROOM (");
        restSTB.append(" ID TEXT PRIMARY KEY,");
        restSTB.append(" BID INTEGER,");
        restSTB.append(" GENDER INTEGER NOT NULL,");
        restSTB.append(" FLOOR INTEGER NOT NULL,");
        restSTB.append(" TOTAL INTEGER NOT NULL,");
        restSTB.append(" EMPTY INTEGER,");
        restSTB.append(" USED INTEGER,");
        restSTB.append(" LATITUDE REAL NOT NULL,");
        restSTB.append(" LONGITUDE REAL NOT NULL,");
        restSTB.append(" FOREIGN KEY(BID) references BUILDING(ID) ON DELETE CASCADE);");
        sqLiteDatabase.execSQL(restSTB.toString());
        Log.d(TAG, "CREATED: RESTROOM TABLE");
//        Toast.makeText(context, "RESTROOM TABLE 생성완료", Toast.LENGTH_SHORT).show();



//        StringBuffer ownSTB = new StringBuffer();
//        ownSTB.append(" CREATE TABLE OWNERSHIP (");
//        ownSTB.append(" ID INTEGER PRIMARY KEY AUTOINCREMENT,");
//        ownSTB.append(" BID TEXT,");
//        ownSTB.append(" RID TEXT,");
//        ownSTB.append(" FOREIGN KEY(BID) references BUILDING(NAME) ON DELETE CASCADE,");
//        ownSTB.append(" FOREIGN KEY(RID) references RESTROOM(ID) ON DELETE CASCADE);");
//        sqLiteDatabase.execSQL(ownSTB.toString());
//        Toast.makeText(context, "OWNERSHIP TABLE 생성완료", Toast.LENGTH_SHORT).show();
    }

    public static synchronized DBHelper getInstance(Context context){
        if(sInstance==null){
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Building> getCloseBuildings(LatLng current, int dist){
        ArrayList<Building> blist = new ArrayList<>();
        String q = "SELECT * FROM BUILDING";
        SQLiteDatabase wDB = getReadableDatabase();
        Cursor cursor = wDB.rawQuery(q, null);

        while(cursor.moveToNext()){
            float[] f = new float[3];
            Location.distanceBetween(current.latitude, current.longitude, cursor.getDouble(3), cursor.getDouble(4), f);
            if(f[0] < dist){
                Building building = new Building();
                building.setmID(cursor.getInt(0));
                building.setmName(cursor.getString(1));
                building.setmMaxFloor(cursor.getInt(2));
                LatLng ll = new LatLng(cursor.getDouble(3), cursor.getDouble(4));
                building.setmLatLng(ll);
                blist.add(building);
            }
        }
//        if(blist.size() == 0)
//            return null;
        return blist;
    }

    public ArrayList<Building> getAllBuildings(){
        ArrayList<Building> blist = new ArrayList<>();
        String q = "SELECT * FROM BUILDING";
        SQLiteDatabase wDB = getReadableDatabase();
        Cursor cursor = wDB.rawQuery(q, null);

        while(cursor.moveToNext()){
            Building building = new Building();
            building.setmID(cursor.getInt(0));
            building.setmName(cursor.getString(1));
            building.setmMaxFloor(cursor.getInt(2));
            LatLng ll = new LatLng(cursor.getDouble(3), cursor.getDouble(4));
            building.setmLatLng(ll);
            blist.add(building);
        }
        return blist;
    }

    public Building getBuildingById(int num){
        String q = "SELECT * FROM BUILDING WHERE ID=?";
        SQLiteDatabase wDB = getReadableDatabase();
        Cursor cursor = wDB.rawQuery(q, new String[]{String.valueOf(num)+""});

        Building building = new Building();
        if(cursor.moveToNext()){
            building.setmID(cursor.getInt(0));
            building.setmName(cursor.getString(1));
            building.setmMaxFloor(cursor.getInt(2));
            LatLng ll = new LatLng(cursor.getDouble(3), cursor.getDouble(4));
            building.setmLatLng(ll);
        }
        return building;
    }

    public Building getBuildingByName(String name){
        String q = "SELECT * FROM BUILDING WHERE NAME=?";
        SQLiteDatabase wDB = getReadableDatabase();
        Cursor cursor = wDB.rawQuery(q, new String[]{name+""});

        Building building = new Building();
        if(cursor.moveToNext()){
            building.setmID(cursor.getInt(0));
            building.setmName(cursor.getString(1));
            building.setmMaxFloor(cursor.getInt(2));
            LatLng ll = new LatLng(cursor.getDouble(3), cursor.getDouble(4));
            building.setmLatLng(ll);
        }
        return building;
    }

    // INTEGER: floor, String: restroom_id
    public HashMap<Integer, HashMap<String, Restroom>> getRestroomByBID(int BID, int gender){
        HashMap<Integer, HashMap<String, Restroom>> restrooms = new HashMap<>();

        String q = "SELECT * FROM RESTROOM WHERE BID=? AND GENDER=?";
        SQLiteDatabase wDB = getReadableDatabase();
        Cursor cursor = wDB.rawQuery(q, new String[]{String.valueOf(BID)+"", String.valueOf(gender)});

        Restroom restroom= new Restroom();
        while(cursor.moveToNext()){
            restroom.setmID(cursor.getString(0));
            restroom.setmBID(cursor.getInt(1));
            restroom.setmFloor(cursor.getInt(2));
            restroom.setGender(cursor.getInt(3));
            restroom.setTotal(cursor.getInt(4));
            LatLng ll = new LatLng(cursor.getDouble(7), cursor.getDouble(8));
            restroom.setmLatLng(ll);

            if(restrooms.get(restroom.getmFloor()) != null){
                restrooms.get(restroom.getmFloor()).put(restroom.getmID(), restroom);
            }else{
                HashMap<String, Restroom> temp = new HashMap<>();
                temp.put(restroom.getmID(), restroom);
                restrooms.put(restroom.getmFloor(), temp);
            }
        }
        return restrooms;
    }
}
