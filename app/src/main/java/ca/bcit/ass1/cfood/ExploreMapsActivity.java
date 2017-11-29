package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class ExploreMapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    SQLiteDatabase db;
    private DBHelper dbHelper;
    String zoneCoords;
    ArrayList<String> zoneNames = new ArrayList<String>();
    Toolbar toolbar;
    boolean tourMode = false;
    TourGuide mTourGuideHandler;

    String[][] zoneLong = new String[15][];
    String[][] zoneLat = new String[15][];
    float[][] centerLat = new float[15][1];
    float[][] centerLong = new float[15][1];

    LatLng[][] latLngs = new LatLng[15][];

    ArrayList<Polygon> zonePolygons = new ArrayList<Polygon>();
    Polygon prevPolygon = null;

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

    ArrayList<Marker> shopsMarkersNB = new ArrayList<Marker>();
    ArrayList<Marker> parksMarkersNB = new ArrayList<Marker>();
    ArrayList<Marker> busStopMarkersNB = new ArrayList<Marker>();
    ArrayList<Marker> recMarkersNB = new ArrayList<Marker>();
    ArrayList<Marker> schoolsMarkersNB = new ArrayList<Marker>();

    LatLng[] latLngsShopsInPolygon;
    LatLng[] latLngsParksInPolygon;
    LatLng[] latLngsBusStopsInPolygon;
    LatLng[] latLngsRecreationInPolygon;
    LatLng[] latLngsSchoolsInPolygon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_maps);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            tourMode= false;
        } else {
            tourMode= extras.getBoolean("TOUR_MODE");
        }
        toolbar = findViewById(R.id.savedZonesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbHelper = new DBHelper(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getAllZones();
        getShops();
        getParks();
        getBusStops();
        getSchools();
        getRecreation();
        if (tourMode) {
            tourExploreActivityOverview();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)  {
        mMap = googleMap;

        mMap.setOnMapClickListener(this);

        for(int i = 0; i < zoneNames.size(); i++) {
            latLngs[i] = new LatLng[zoneLat[i].length];
            for(int j = 0; j < zoneLat[i].length; j++) {
                latLngs[i][j] = new LatLng(Float.parseFloat(zoneLong[i][j]), Float.parseFloat(zoneLat[i][j]));
            }
            PolygonOptions rectOptions = new PolygonOptions()
                    .add(latLngs[i])
                    .fillColor(Color.argb(20, 90, 0, 155))
                    .strokeWidth(2.0f);
            Polygon polygon = mMap.addPolygon(rectOptions);
            polygon.setVisible(false);
            zonePolygons.add(polygon);
        }

        LatLng latlng = new LatLng(49.208021750106795, -122.91226351780601);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13.0f));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LatLng centering;

        for(int j = 0; j < zonePolygons.size(); j++) {
            if(PolyUtil.containsLocation(
                    latLng, zonePolygons.get(j).getPoints(), false)) {
                Polygon currPoly = zonePolygons.get(j);
                currPoly.setVisible(true);
                if(prevPolygon != null && prevPolygon.isVisible()) {
                    prevPolygon.setVisible(false);
                    hideBusStopsNB();
                    hideParksNB();
                    hideRecNB();
                    hideSchoolsNB();
                    hideShopsNB();
                }
                prevPolygon = currPoly;
                centering = new LatLng(centerLat[j][0], centerLong[j][0]);
                getAllPoints(currPoly);
                setTitle(zoneNames.get(j));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centering, 14.0f));
            }
        }
        if (tourMode) {
            tourExploreMapDemoResult();
        }

    }

    private void getAllPoints(Polygon polygon) {

        latLngsShopsInPolygon = new LatLng[shopsNames.size()];
        latLngsParksInPolygon = new LatLng[parksLat.length];
        latLngsBusStopsInPolygon = new LatLng[busStopX.size()];
        latLngsRecreationInPolygon = new LatLng[recX.size()];
        latLngsSchoolsInPolygon = new LatLng[schoolsX.size()];

        for(int j = 0; j < shopsNames.size(); j++) {
            if(PolyUtil.containsLocation(
                    Float.parseFloat(shopsX.get(j)),
                    Float.parseFloat(shopsY.get(j)), polygon.getPoints(), false)) {
                latLngsShopsInPolygon[j] = new LatLng(Float.parseFloat(shopsX.get(j)), Float.parseFloat(shopsY.get(j)));

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position((LatLng)latLngsShopsInPolygon[j])
                        .title("Shop: \r" + shopsNames.get(j))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                );
                shopsMarkersNB.add(marker);

            }
        }

        for(int j = 0; j < parksLat.length; j++) {
            if(PolyUtil.containsLocation(
                    Float.parseFloat(parksLat[j]),
                    Float.parseFloat(parksLong[j]), polygon.getPoints(), false)) {
                latLngsParksInPolygon[j] = new LatLng(Float.parseFloat(parksLat[j]), Float.parseFloat(parksLong[j]));

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position((LatLng)latLngsParksInPolygon[j])
                        .title("Park: \r" + parksNames.get(j))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );
                parksMarkersNB.add(marker);
            }
        }

        for(int j = 0; j < busStopX.size(); j++) {
            if(PolyUtil.containsLocation(
                    Float.parseFloat(busStopX.get(j)),
                    Float.parseFloat(busStopY.get(j)), polygon.getPoints(), false)) {
                latLngsBusStopsInPolygon[j] = new LatLng(Float.parseFloat(busStopX.get(j)), Float.parseFloat(busStopY.get(j)));

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position((LatLng)latLngsBusStopsInPolygon[j])
                        .title("Bus Stop: \r" + busStopNames.get(j))
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.reticle))
                );
                busStopMarkersNB.add(marker);
            }
        }

        for(int j = 0; j < recX.size(); j++) {
            if(PolyUtil.containsLocation(
                    Float.parseFloat(recX.get(j)),
                    Float.parseFloat(recY.get(j)), polygon.getPoints(), false)) {
                latLngsRecreationInPolygon[j] = new LatLng(Float.parseFloat(recX.get(j)), Float.parseFloat(recY.get(j)));

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position((LatLng)latLngsRecreationInPolygon[j])
                        .title("Recreation Center: \r" + recNames.get(j))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );
                recMarkersNB.add(marker);
            }
        }

        for(int j = 0; j < schoolsX.size(); j++) {
            if(PolyUtil.containsLocation(
                    Float.parseFloat(schoolsX.get(j)),
                    Float.parseFloat(schoolsY.get(j)), polygon.getPoints(), false)) {
                latLngsSchoolsInPolygon[j] = new LatLng(Float.parseFloat(schoolsX.get(j)), Float.parseFloat(schoolsY.get(j)));

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position((LatLng)latLngsSchoolsInPolygon[j])
                        .title("School: \r" + schoolsNames.get(j))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                );
                shopsMarkersNB.add(marker);
            }
        }
    }

    public void hideRecNB() {
        for(int j = 0; j < recMarkersNB.size(); j++) {
            recMarkersNB.get(j).remove();
        }
    }

    public void hideShopsNB() {
        for(int j = 0; j < shopsMarkersNB.size(); j++) {
            shopsMarkersNB.get(j).remove();
        }
    }

    public void hideSchoolsNB() {
        for(int j = 0; j < schoolsMarkersNB.size(); j++) {
            schoolsMarkersNB.get(j).remove();
        }
    }

    public void hideParksNB() {
        for(int j = 0; j < parksMarkersNB.size(); j++) {
            parksMarkersNB.get(j).remove();
        }
    }

    public void hideBusStopsNB() {
        for(int j = 0; j < busStopMarkersNB.size(); j++) {
            busStopMarkersNB.get(j).remove();
        }
    }

    private void getAllZones() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getAllZones(db);
        int count = 0;

        while(cursor.moveToNext()) {

            zoneNames.add(cursor.getString(1));
            zoneCoords = cursor.getString(2);
            centerLong[count][0] = Float.parseFloat(cursor.getString(4));
            centerLat[count][0] = Float.parseFloat(cursor.getString(5));

            try {
                JSONArray array = new JSONArray(zoneCoords);
                JSONArray array2 = new JSONArray(array.getJSONArray(0).toString());
                zoneLat[count] = new String[array2.length()];
                zoneLong[count] = new String[array2.length()];
                int length = array2.length();
                for (int i = 0; i < length; i++) {
                    JSONArray array3 = new JSONArray(array2.getJSONArray(i).toString());
                    zoneLong[count][i] = array3.get(1).toString();
                    zoneLat[count][i] = array3.get(0).toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            count++;
        }
        cursor.close();
    }

    private void getSchools() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getAllSchools(db);
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
        db = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getAllRec(db);
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
        db = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getAllBusStops(db);
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
        db = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getAllParks(db);
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
        db = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getAllShops(db);
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


    public void tourExploreActivityOverview() {
        toolbar.setOnClickListener(null);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourExploreActivityOverviewHeader)).setDescription(getString(R.string.tourExploreActivityOverviewText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.BOTTOM))
                .setOverlay(new Overlay())
                .playOn(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourExploreMap();
            }
        });
    }

    public void tourExploreMap() {
        mTourGuideHandler.cleanUp();
        toolbar.setOnClickListener(null);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourExploreMapHeader)).setDescription(getString(R.string.tourExploreMapText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.BOTTOM))
                .setOverlay(new Overlay())
                .playOn(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourExploreMapDemo();
            }
        });
    }

    public void tourExploreMapDemo() {
        mTourGuideHandler.cleanUp();
        toolbar.setOnClickListener(null);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourExploreMapDemoHeader)).setDescription(getString(R.string.tourExploreMapDemoText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.TOP))
                .setOverlay(new Overlay())
                .playOn(findViewById(R.id.map));
        findViewById(R.id.map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourExploreMapDemoResult();
            }
        });
    }

    public void tourExploreMapDemoResult() {
        mTourGuideHandler.cleanUp();
        findViewById(R.id.map).setOnClickListener(null);
        toolbar.setOnClickListener(null);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourExploreMapDemoResultHeader)).setDescription(getString(R.string.tourExploreMapDemoResultText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.BOTTOM))
                .setOverlay(new Overlay())
                .playOn(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourConclusion();
            }
        });
    }

    public void tourConclusion() {
        mTourGuideHandler.cleanUp();
        toolbar.setOnClickListener(null);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourConclusionHeader)).setDescription(getString(R.string.tourConclusionText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.BOTTOM))
                .setOverlay(new Overlay())
                .playOn(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExploreMapsActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
            }
        });
    }

}
