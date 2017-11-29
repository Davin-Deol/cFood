package ca.bcit.ass1.cfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by E on 2017-11-22.
 */

public class QueryDB {

    String zoneCoords = null;
    String[] zoneLat = null;
    String[] zoneLong = null;
    String zoneDesc = null;
    float centerLat = 0.0f;
    float centerLong = 0.0f;

    ArrayList<String> shopsNames = new ArrayList<String>();
    ArrayList<String> shopsX = new ArrayList<String>();
    ArrayList<String> shopsY = new ArrayList<String>();

    ArrayList<String> busStopNames = new ArrayList<String>();
    ArrayList<String> busStopX = new ArrayList<String>();
    ArrayList<String> busStopY = new ArrayList<String>();

    ArrayList<String> recNames = new ArrayList<String>();
    ArrayList<String> recX = new ArrayList<String>();
    ArrayList<String> recY = new ArrayList<String>();

    ArrayList<String> schoolsNames = new ArrayList<String>();
    ArrayList<String> schoolsX = new ArrayList<String>();
    ArrayList<String> schoolsY = new ArrayList<String>();

    ArrayList<String> parksNames = new ArrayList<String>();
    String[] parksLat = null;
    String[] parksLong = null;
    ArrayList<String> parksCoords = new ArrayList<String>();

    private DBHelper openHelper;
    SQLiteDatabase db;
    private String TAG = PopulateDB.class.getSimpleName();
    Context context;
    Activity activity;
    String zoneName;

    String[][] zoneLongAll = new String[35][];
    String[][] zoneLatAll  = new String[35][];
    float[][] centerLatAll  = new float[17][1];
    float[][] centerLongAll  = new float[17][1];
    ArrayList<String> zoneNames = new ArrayList<String>();


    public QueryDB(Context context, Activity activity, String zoneName) {
        this.context = context;
        this.activity = activity;
        this.zoneName = zoneName;
        openHelper = new DBHelper(context);
    }

    public QueryDB(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        openHelper = new DBHelper(context);
    }

    public void retrieveAllData() {
        getAllZones();
        getShops();
        getParks();
        getBusStops();
        getSchools();
        getRecreation();
    }

    public void retrieveAllZoneData() {
        getZone(zoneName);
        getShops();
        getParks();
        getBusStops();
        getSchools();
        getRecreation();
    }

    private void getAllZones() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllZones(db);
        int count = 0;

        while(cursor.moveToNext()) {

            zoneNames.add(cursor.getString(1));
            zoneCoords = cursor.getString(2);
            centerLongAll [count][0] = Float.parseFloat(cursor.getString(4));
            centerLatAll [count][0] = Float.parseFloat(cursor.getString(5));

            try {
                JSONArray array = new JSONArray(zoneCoords);
                JSONArray array2 = new JSONArray(array.getJSONArray(0).toString());
                zoneLatAll [count] = new String[array2.length()];
                zoneLongAll [count] = new String[array2.length()];
                // zoneCoords = new String[array2.length()][2];
                int length = array2.length();
                for (int i = 0; i < length; i++) {
                    JSONArray array3 = new JSONArray(array2.getJSONArray(i).toString());
                    zoneLongAll [count][i] = array3.get(1).toString();
                    zoneLatAll [count][i] = array3.get(0).toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //zoneLong2.add(zoneLong);
            //zoneLat2.add(zoneLat);

            //zoneAll[count][0] = zoneLong1;
            count++;
        }

        cursor.close();
    }

    private void getSchools() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllSchools(db);
        String name = null;
        String xValue = null;
        String yValue = null;

        while (cursor.moveToNext() ) {
            name = cursor.getString(1);
            xValue = cursor.getString(2);
            yValue = cursor.getString(3);
            schoolsNames.add(name);
            schoolsX.add(xValue);
            schoolsY.add(yValue);
        }
        cursor.close();
    }

    private void getRecreation() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllRec(db);
        String name = null;
        String xValue = null;
        String yValue = null;

        while (cursor.moveToNext() ) {
            name = cursor.getString(1);
            xValue = cursor.getString(2);
            yValue = cursor.getString(3);
            recNames.add(name);
            recX.add(xValue);
            recY.add(yValue);
        }
        cursor.close();
    }

    private void getBusStops() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllBusStops(db);
        String name = null;
        String xValue = null;
        String yValue = null;

        while (cursor.moveToNext() ) {
            name = cursor.getString(1);
            xValue = cursor.getString(2);
            yValue = cursor.getString(3);
            busStopNames.add(name);
            busStopX.add(xValue);
            busStopY.add(yValue);
        }
        cursor.close();
    }

    private void getParks() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllParks(db);
        String name = null;
        String xValue = null;
        String yValue = null;
        int count = 0;

        while (cursor.moveToNext() ) {
            parksCoords.add(cursor.getString(2));
            if(cursor.getString(1) == null || cursor.getString(1) == "") {
                parksNames.add("Park");
            } else {
                parksNames.add(cursor.getString(1));
            }
            count++;
        }
        cursor.close();

        parksLat = new String[parksCoords.size()];
        parksLong = new String[parksCoords.size()];
        for (int j = 0; j < parksCoords.size(); j++) {
            try {
                JSONArray array = new JSONArray(parksCoords.get(j));
                JSONArray array2 = new JSONArray(array.getJSONArray(0).toString());
                int length = array2.length();
                JSONArray array3 = new JSONArray(array2.getJSONArray(0).toString());
                parksLat[j] = array3.get(1).toString();
                parksLong[j] = array3.get(0).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getShops() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllShops(db);
        String name = null;
        String xValue = null;
        String yValue = null;

        while (cursor.moveToNext() ) {
            name = cursor.getString(1);
            xValue = cursor.getString(2);
            yValue = cursor.getString(3);
            shopsNames.add(name);
            shopsX.add(xValue);
            shopsY.add(yValue);
        }
        cursor.close();
    }

    public String getDesc(String zone) {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getZoneCoords(db, zone);

        if (cursor.moveToFirst() ) {
            zoneDesc = cursor.getString(3);
        }
        cursor.close();
        return zoneDesc;
    }

    private void getZone(String zone) {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getZoneCoords(db, zone);

        if (cursor.moveToFirst() ) {
            zoneCoords = cursor.getString(2);
            //zoneDesc = cursor.getString(3);
            centerLong = Float.parseFloat(cursor.getString(4));
            centerLat = Float.parseFloat(cursor.getString(5));
        }
        cursor.close();

        try {
            JSONArray array = new JSONArray(zoneCoords);
            JSONArray array2 = new JSONArray(array.getJSONArray(0).toString());
            zoneLat = new String[array2.length()];
            zoneLong = new String[array2.length()];
            // zoneCoords = new String[array2.length()][2];
            int length = array2.length();
            for(int i = 0; i < length; i++) {
                JSONArray array3 = new JSONArray(array2.getJSONArray(i).toString());
                zoneLong[i] = array3.get(1).toString();
                zoneLat[i] = array3.get(0).toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
