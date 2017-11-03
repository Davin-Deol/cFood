package ca.bcit.ass1.cfood;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by E on 2017-11-02.
 */

public class ResultsActivity extends ListActivity {

    private ZoningDBHelper zoningOpenHelper;
    private SimpleCursorAdapter adapter;
    private String[][] zoneArray;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final LoaderManager manager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_main);

        init();

        //lv = (ListView)findViewById(R.id.list);
        zoningOpenHelper = new ZoningDBHelper(this);
        SQLiteDatabase db = zoningOpenHelper.getWritableDatabase();
//        ZoneAdapter adapter = new ZoneAdapter(this, zoningOpenHelper.getAllZones());

//        adapter = new SimpleCursorAdapter(getBaseContext(),
//                android.R.layout.simple_list_item_2,
//                null,
//                new String[]
//                        {
//                                ZoningOpenHelper.columnZoneType,
//                                ZoningOpenHelper.columnJSONCoord,
//                        },
//                new int[]
//                        {
//                                android.R.id.text1,
//                                android.R.id.text2,
//                        },
//                0);
//        setListAdapter(adapter);
        //lv.setAdapter(adapter);
        //init();
        ZoneAdapter adapter = new ZoneAdapter(this, zoningOpenHelper.getAllZones(db));
        setListAdapter(adapter);


    }

    private void retrieveAll() {

        SQLiteDatabase db;
        Cursor cursor;

        db = zoningOpenHelper.getWritableDatabase();
        cursor = zoningOpenHelper.getAllZones(db);
        int i = 0;
        while(cursor.moveToNext())
        {
            String zoneType;
            String zoneCoord;

            zoneType = cursor.getString(0);
            zoneCoord = cursor.getString(1);

            zoneArray[i][0] = zoneType;
            zoneArray[i][1] = zoneCoord;
            i++;

            //Log.d(TAG, zoneType);
        }

        cursor.close();

    }

    private void init() {

        SQLiteDatabase db;

        db = zoningOpenHelper.getWritableDatabase();
        String[][] zone = getJSONString();
//        db.beginTransaction();
        for(int i = 0; i < zone.length; i++) {
            db.beginTransaction();
            try {
                zoningOpenHelper.insertZone(db, zone[i][0], zone[i][1]);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    public String importJSON() {
        String json = null;
        try {

            InputStream is = getAssets().open("ZONING.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String[][] getJSONString() {

        String jsonImport = importJSON();
        Log.e(TAG, "Response from url: " + jsonImport);
        String[][] zoneArray = null;
        try {

            JSONArray zoneJSONArray = new JSONArray(jsonImport);
            zoneArray = new String[zoneJSONArray.length()][2];
            for (int i = 0; i < zoneJSONArray.length(); i++) {
                JSONObject obj = zoneJSONArray.getJSONObject(i);

                String category = obj.getString("ZONECAT");
                String coords = obj.getJSONObject("json_geometry")
                        .getJSONArray("coordinates").toString();

                zoneArray[i][0] = category;
                zoneArray[i][1] = coords;

            }
        }catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
        return zoneArray;
    }

}
