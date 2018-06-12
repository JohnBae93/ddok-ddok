package edu.skku.swp3.ddokddok.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import edu.skku.swp3.ddokddok.models.Building;
import edu.skku.swp3.ddokddok.models.Restroom;

public class DBHelper extends SQLiteOpenHelper {
    private static String TAG = "DBHelper";

    private Context context;
    private static DBHelper sInstance;
    private String mDBName;
    private String mDBPath;
    private LatLng mCurrent;

    public DBHelper(Context context){
        super(context, "ddok-ddok.db", null, 1);
        this.context = context;
        this.mDBName = "ddok-ddok.db";
        mDBPath = "/data/data/"+context.getPackageName()+"/databases/";
    }

    private boolean checkExist(){
        SQLiteDatabase dbExists = null;
        try{
            String myPath = mDBPath+mDBName;
            dbExists = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
            e.printStackTrace();
        }catch (Exception ep){
            ep.printStackTrace();
        }
        if(dbExists!=null){
            dbExists.close();
            return true;
        }else{
            return false;
        }
    }

    private void copyDatabase() throws IOException{
        InputStream is = context.getAssets().open(mDBName);
        OutputStream os = new FileOutputStream(mDBPath+mDBName);

        byte[] buffer = new byte[4096];
        int length;
        while((length = is.read(buffer)) > 0){
            os.write(buffer, 0, length);
        }
        os.flush();
        os.close();
        is.close();
        this.close();
    }

    public void importAnyway() throws IOException{
        this.getReadableDatabase();

        try{
            copyDatabase();
        }catch (IOException e){
            e.printStackTrace();
//            throw new Error("Error coping database");
        }
    }

    public void importIfNotExist() throws IOException{
        boolean dbExists = checkExist();

        if(dbExists){
            // do nothing
        }else{
            this.getReadableDatabase();

            try{
                copyDatabase();
            }catch (IOException e){
                throw new Error("Error coping database");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
//
//        StringBuffer buildSTB = new StringBuffer();
//        buildSTB.append(" CREATE TABLE BUILDING (");
//        buildSTB.append(" ID INTEGER PRIMARY KEY AUTOINCREMENT,");
//        buildSTB.append(" NAME TEXT NOT NULL,");
//        buildSTB.append(" MAXFLOOR INTEGER NOT NULL,");
//        buildSTB.append(" LATITUDE REAL NOT NULL,");
//        buildSTB.append(" LONGITUDE REAL NOT NULL);");
//        sqLiteDatabase.execSQL(buildSTB.toString());
//        Log.d(TAG, "CREATED: BUILDING TABLE");
////        Toast.makeText(context, "BUILDING TABLE 생성완료", Toast.LENGTH_SHORT).show();
//
//        StringBuffer restSTB = new StringBuffer();
//        restSTB.append(" CREATE TABLE RESTROOM (");
//        restSTB.append(" ID TEXT PRIMARY KEY,");
//        restSTB.append(" BID INTEGER,");
//        restSTB.append(" GENDER INTEGER NOT NULL,");
//        restSTB.append(" FLOOR INTEGER NOT NULL,");
//        restSTB.append(" TOTAL INTEGER NOT NULL,");
//        restSTB.append(" EMPTY INTEGER,");
//        restSTB.append(" USED INTEGER,");
//        restSTB.append(" LATITUDE REAL NOT NULL,");
//        restSTB.append(" LONGITUDE REAL NOT NULL,");
//        restSTB.append(" FOREIGN KEY(BID) references BUILDING(ID) ON DELETE CASCADE);");
//        sqLiteDatabase.execSQL(restSTB.toString());
//        Log.d(TAG, "CREATED: RESTROOM TABLE");
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

    class ascendingBuilding implements Comparator<Building> {
        @Override
        public int compare(Building b1, Building  b2){
            float d1 = getDist(mCurrent.latitude, mCurrent.longitude, b1.getmLatLng().latitude, b1.getmLatLng().longitude);
            float d2 = getDist(mCurrent.latitude, mCurrent.longitude, b2.getmLatLng().latitude, b2.getmLatLng().longitude);
            if(d1 > d2){
                return 1;
            }else if(d1<d2){
                return -1;
            }else{
                return 0;
            }
        }
    }

    private float getDist(double lat1, double long1, double lat2, double long2){
        float[] f = new float[3];
        Location.distanceBetween(lat1, long1, lat2, long2, f);
        return f[0];
    }

    public ArrayList<Building> getCloseBuildings(LatLng current, int dist){
        mCurrent = current;
        ArrayList<Building> blist = new ArrayList<>();
        String q = "SELECT * FROM BUILDING";
        SQLiteDatabase wDB = getReadableDatabase();
        Cursor cursor = wDB.rawQuery(q, null);

        while(cursor.moveToNext()){
            float gap_dist = getDist(current.latitude, current.longitude, cursor.getDouble(3), cursor.getDouble(4));
            if(gap_dist < dist){
                Building building = new Building();
                building.setmID(cursor.getInt(0));
                building.setmName(cursor.getString(1));
                building.setmMaxFloor(cursor.getInt(2));
                LatLng ll = new LatLng(cursor.getDouble(3), cursor.getDouble(4));
                building.setmLatLng(ll);
                blist.add(building);
            }
//            float[] f = new float[3];
//            Location.distanceBetween(current.latitude, current.longitude, cursor.getDouble(3), cursor.getDouble(4), f);
//            if(f[0] < dist){
//                Building building = new Building();
//                building.setmID(cursor.getInt(0));
//                building.setmName(cursor.getString(1));
//                building.setmMaxFloor(cursor.getInt(2));
//                LatLng ll = new LatLng(cursor.getDouble(3), cursor.getDouble(4));
//                building.setmLatLng(ll);
//                blist.add(building);
//            }
        }

        // blist 정렬해서 리턴
        Collections.sort(blist, new ascendingBuilding());
        int limit = 3;
        if(blist.size() > limit) {
            blist.subList(limit, blist.size()).clear();
        }
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
