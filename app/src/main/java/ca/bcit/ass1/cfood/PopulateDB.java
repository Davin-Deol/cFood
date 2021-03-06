package ca.bcit.ass1.cfood;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by E on 2017-11-22.
 */

public class PopulateDB {

    private DBHelper openHelper;
    SQLiteDatabase db;
    private String TAG = PopulateDB.class.getSimpleName();
    Context context;
    Activity activity;


    String zoneCoords = null;
    ArrayList<String> zoneNames = new ArrayList<String>();
    String[][] zoneAll = new String[16][];
    String[][] zoneLong = new String[16][];
    String[][] zoneLat = new String[16][];

    public PopulateDB(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void init() {
        openHelper = new DBHelper(context);
    db = openHelper.getWritableDatabase();
    ArrayList<String[][]> jsonResult = getJSONString();
    String[][] zones = jsonResult.get(0);
    String[][] bus = jsonResult.get(1);
    String[][] shops = jsonResult.get(2);
    String[][] parks = jsonResult.get(3);
    String[][] rec = jsonResult.get(4);
    String[][] schools = jsonResult.get(5);

    for(int i = 0; i < zones.length; i++) {

        zoneCoords = zones[i][1];

        float north = 0;
        float south = 50;
        float east = -123;
        float west = 0;

        float centerLong = 0;
        float centerLat = 0;

        try {
            JSONArray array = new JSONArray(zoneCoords);
            JSONArray array2 = new JSONArray(array.getJSONArray(0).toString());
            zoneLat[i] = new String[array2.length()];
            zoneLong[i] = new String[array2.length()];

            int length = array2.length();
            for (int j = 0; j < length; j++) {
                JSONArray array3 = new JSONArray(array2.getJSONArray(j).toString());
                zoneLong[i][j] = array3.get(1).toString();
                zoneLat[i][j] = array3.get(0).toString();

                //calculate the center point of the polygon
                if(Float.parseFloat(zoneLong[i][j]) > north) {
                    north = Float.parseFloat(zoneLong[i][j]);
                }
                if(Float.parseFloat(zoneLong[i][j]) < south) {
                    south = Float.parseFloat(zoneLong[i][j]);
                }
                if(Float.parseFloat(zoneLat[i][j]) > east) {
                    east = Float.parseFloat(zoneLat[i][j]);
                }
                if(Float.parseFloat(zoneLat[i][j]) < west) {
                    west = Float.parseFloat(zoneLat[i][j]);
                }
            }
            centerLat = south + ((north - south)/2);
            centerLong = west + ((east - west)/2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.beginTransaction();
        try {
            openHelper.insertZone(db, zones[i][0], zones[i][1], zones[i][2], centerLong, centerLat);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    for(int i = 0; i < parks.length; i++) {
        db.beginTransaction();
        try {
            openHelper.insertPark(db, parks[i][0], parks[i][1]);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    for(int i = 0; i < shops.length; i++) {
        db.beginTransaction();
        try {
            openHelper.insertShop(db, shops[i][0], shops[i][1], shops[i][2]);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    for(int i = 0; i < bus.length; i++) {
        db.beginTransaction();
        try {
            openHelper.insertBusStop(db, bus[i][0], bus[i][1], bus[i][2]);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    for(int i = 0; i < rec.length; i++) {
        db.beginTransaction();
        try {
            openHelper.insertRec(db, rec[i][0], rec[i][1], rec[i][2]);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    for(int i = 0; i < schools.length; i++) {
        db.beginTransaction();
        try {
            openHelper.insertSchool(db, schools[i][0], schools[i][1], schools[i][2]);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
        public ArrayList<String> importJSON() {
            ArrayList<String> jsonList = new ArrayList();
            try {
                AssetManager assets = context.getAssets();
                String [] allFiles = assets.list("");

                for(int i = 0; i < allFiles.length; i++) {
                    if(allFiles[i].contains(".json")) {
                        InputStream is = context.getAssets().open(allFiles[i]);
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        jsonList.add(new String(buffer, "UTF-8"));
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return jsonList;
        }

        //Get JSON strings from the files, for all datasets
        public ArrayList<String[][]> getJSONString() {

            ArrayList<String[][]> allJson = new ArrayList<String[][]>();
            ArrayList <String> jsonImport = importJSON();
            Log.e(TAG, "Response from url: " + jsonImport);
            String[][] zoneArray = null;
            String[][] parksArray = null;
            String[][] busArray = null;
            String[][] recreationArray = null;
            String[][] schoolsArray = null;
            String[][] shoppingArray = null;

            try {
                JSONArray zoneJSONArray = new JSONArray(jsonImport.get(5));
                zoneArray = new String[zoneJSONArray.length()][3];
                for (int i = 0; i < zoneJSONArray.length(); i++) {
                    JSONObject obj = zoneJSONArray.getJSONObject(i);

                    String category = obj.getString("NEIGH_NAME");
                    String coords = obj.getJSONObject("json_geometry")
                            .getJSONArray("coordinates").toString();
                    String description = obj.getString("DESCRIPTION");

                    zoneArray[i][0] = category;
                    zoneArray[i][1] = coords;
                    zoneArray[i][2] = description;
                }

                JSONArray busJSONArray = new JSONArray(jsonImport.get(0));
                busArray = new String[busJSONArray.length()][3];
                for (int i = 0; i < busJSONArray.length(); i++) {
                    JSONObject obj = busJSONArray.getJSONObject(i);

                    String category = obj.getString("BUSSTOPNUM");
                    String longitude = obj.getString("X");
                    String latitude = obj.getString("Y");

                    busArray[i][0] = category;
                    busArray[i][1] = latitude;
                    busArray[i][2] = longitude;
                }

                JSONArray shopJSONArray = new JSONArray(jsonImport.get(1));
                shoppingArray = new String[shopJSONArray.length()][3];
                for (int i = 0; i < shopJSONArray.length(); i++) {
                    JSONObject obj = shopJSONArray.getJSONObject(i);

                    String category = obj.getString("BLDGNAM");
                    String longitude = obj.getString("X");
                    String latitude = obj.getString("Y");

                    shoppingArray[i][0] = category;
                    shoppingArray[i][1] = latitude;
                    shoppingArray[i][2] = longitude;
                }

                JSONArray parksJSONArray = new JSONArray(jsonImport.get(2));
                parksArray = new String[parksJSONArray.length()][2];
                for (int i = 0; i < parksJSONArray.length(); i++) {
                    JSONObject obj = parksJSONArray.getJSONObject(i);

                    String category = obj.getString("Name");
                    String coords = obj.getJSONObject("json_geometry")
                            .getJSONArray("coordinates").toString();

                    parksArray[i][0] = category;
                    parksArray[i][1] = coords;
                }

                JSONArray recJSONArray = new JSONArray(jsonImport.get(3));
                recreationArray = new String[recJSONArray.length()][3];
                for (int i = 0; i < recJSONArray.length(); i++) {
                    JSONObject obj = recJSONArray.getJSONObject(i);

                    String category = obj.getString("Name");
                    String longitude = obj.getString("X");
                    String latitude = obj.getString("Y");

                    recreationArray[i][0] = category;
                    recreationArray[i][1] = latitude;
                    recreationArray[i][2] = longitude;
                }

                JSONArray schoolsJSONArray = new JSONArray(jsonImport.get(4));
                schoolsArray = new String[schoolsJSONArray.length()][3];
                for (int i = 0; i < schoolsJSONArray.length(); i++) {
                    JSONObject obj = schoolsJSONArray.getJSONObject(i);

                    String category = obj.getString("BLDGNAM");
                    String longitude = obj.getString("X");
                    String latitude = obj.getString("Y");

                    schoolsArray[i][0] = category;
                    schoolsArray[i][1] = latitude;
                    schoolsArray[i][2] = longitude;
                }

                allJson.add(zoneArray);
                allJson.add(busArray);
                allJson.add(shoppingArray);
                allJson.add(parksArray);
                allJson.add(recreationArray);
                allJson.add(schoolsArray);

            }catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return allJson;
        }

}
