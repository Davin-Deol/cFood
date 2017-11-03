package ca.bcit.ass1.cfood;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Results extends AppCompatActivity {

    private ZoningDBHelper zoningOpenHelper;
    private ListView lv;
    private String TAG = MainActivity.class.getSimpleName();
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int[] arrayB = getIntent().getExtras().getIntArray("INPUTS");
        String test = "" + arrayB[0];

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        zoningOpenHelper = new ZoningDBHelper(this);
        //db = zoningOpenHelper.getWritableDatabase();

        db = zoningOpenHelper.getReadableDatabase();

        //init();

        final Neighbourhood[] resultsArray = {
                new Neighbourhood("Neighbourhood 1", getResources().getStringArray(R.array.distances)[arrayB[0]], "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 2", "Description - bad place to live", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 3", "Description - high density", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 4", "Description - far away from everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 5", "Description - close to everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 1", "Description - noisy area", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 2", "Description - bad place to live", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 3", "Description - high density", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 4", "Description - far away from everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 5", "Description - close to everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 1", "Description - noisy area", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 2", "Description - bad place to live", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 3", "Description - high density", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 4", "Description - far away from everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 5", "Description - close to everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2")
        };

        lv = (ListView)findViewById(R.id.list);
        ZoneAdapter adapter = new ZoneAdapter(this, zoningOpenHelper.getAllZones(db));
        //lv.setAdapter(adapter);

//        ListView resultsList = new ListView(this);
//        ArrayAdapter<Neighbourhood> resultsAdapter = new ArrayAdapter<Neighbourhood>(this,
//                0, resultsArray) {

//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                Neighbourhood currentResult = resultsArray[position];
//                if(convertView == null) {
//                    convertView = getLayoutInflater()
//                            .inflate(R.layout.activity_list, null, false);
//                }
//                TextView resultsNHood = (TextView)convertView.findViewById(R.id.results);
//                TextView resultsDesc = (TextView)convertView.findViewById(R.id.resultsDescription);
//                TextView resultsRate = (TextView)convertView.findViewById(R.id.resultsRating);
//
//                resultsNHood.setText(currentResult.neighbourhood);
//                resultsDesc.setText(currentResult.description);
//                resultsRate.setText(currentResult.rating);
//
//                return convertView;
//            }
//        };



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       // resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3 ) {
                TextView expandable = (TextView)view.findViewById(R.id.expandable);
                TextView resultsNHood = (TextView)view.findViewById(R.id.results);
                TextView resultsDesc = (TextView)view.findViewById(R.id.type);
                TextView resultsRate = (TextView)view.findViewById(R.id.coords);

                if ( expandable.getVisibility() == View.GONE)
                {
                    //expandedChildList.set(arg2, true);
//                    resultsNHood.setVisibility(View.GONE);
//                    resultsDesc.setVisibility(View.GONE);
//                    resultsRate.setVisibility(View.GONE);
                    expandable.setVisibility(View.VISIBLE);
                }
                else
                {
                    //expandedChildList.set(arg2, false);
//                    resultsNHood.setVisibility(View.VISIBLE);
//                    resultsDesc.setVisibility(View.VISIBLE);
//                    resultsRate.setVisibility(View.VISIBLE);
                    expandable.setVisibility(View.GONE);
                }
            }
        });
        lv.setAdapter(adapter);
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
