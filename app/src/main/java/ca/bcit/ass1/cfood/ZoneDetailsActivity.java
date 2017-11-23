package ca.bcit.ass1.cfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ZoneDetailsActivity extends AppCompatActivity {
    String[] categories;
    Neighbourhood sampleNeighbourhood = new Neighbourhood("Willington, Deer Lake", "flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl");
    private boolean[] checkboxes;
    Menu menu;

    private ProgressDialog pDialog;

    String [] coordsLong;
    String [] coordsLat;

    String [] shopsNames;
    String [] shopsX;
    String [] shopsY;

    String [] parksLat;
    String [] parksLong;
    String [] parksNames;

    String [] busStopX;
    String [] busStopY;
    String [] busStopNames;

    String [] recX;
    String [] recY;
    String [] recNames;

    String [] schoolsX;
    String [] schoolsY;
    String [] schoolsNames;
    Bundle bundle;

    MapsActivity fragment;
    ListView zoneDetailsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new getQuery().execute();

        categories = getResources().getStringArray(R.array.categories);
        checkboxes = new boolean[categories.length];
        for (boolean checkbox : checkboxes) {
            checkbox = true;
        }
        setContentView(R.layout.activity_zone_details);
        Toolbar toolbar = findViewById(R.id.zoneDetailsToolbar);
        setSupportActionBar(toolbar);

        TextView zoneDetailsDescription = findViewById(R.id.zoneDetailsDescriptionTextView);
        zoneDetailsDescription.setText(sampleNeighbourhood.description);

        zoneDetailsListView = findViewById(R.id.zoneDetailsListView);
//        CustomAdapter customAdapter = new CustomAdapter();
//        zoneDetailsListView.setAdapter(customAdapter);
        setTitle("Uptown");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        Bundle bundle = new Bundle();
//        bundle.putString("test", recNames[2]);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        MapsActivity fragment = new MapsActivity();
//        fragmentTransaction.replace(R.id.mapView, fragment);
//        fragmentTransaction.commit();
//        fragment.setArguments(bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_neighbourhood_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, SavedZonesActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return categories.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.layout_zones_details_specs, null);
            TextView savedZonesSpecLabel = view.findViewById(R.id.savedZonesSpecLabel);
            savedZonesSpecLabel.setText(categories[i]);
            CheckBox checkbox = (CheckBox) view.findViewById(R.id.savedZonesCheckbox);
            final int index = i;
            if (checkboxes[i]) {
                checkbox.setChecked(false);
            } else {
                checkbox.setChecked(true);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkboxes[index]) {
                        checkboxes[index] = false;
                        switch(index) {
                            case 0:
                                putBusStopMarkers();
                                break;
                            case 1:
                                putShopsMarkers();
                                break;
                            case 2:
                                putRecMarkers();
                                break;
                            case 3:
                                putParksMarkers();
                                break;
                            case 4:
                                putSchoolsMarkers();
                                break;
                            default: break;
                        }
                    } else {
                        checkboxes[index] = true;
                        switch(index) {
                            case 0:
                                hideBusStopMarkers();
                                break;
                            case 1:
                                hideShopsMarkers();
                                break;
                            case 2:
                                hideRecMarkers();
                                break;
                            case 3:
                                hideParksMarkers();
                                break;
                            case 4:
                                hideSchoolMarkers();
                                break;
                            default: break;
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    private class getQuery extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new ProgressDialog(ZoneDetailsActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            QueryDB queryDB = new QueryDB(getApplicationContext(), ZoneDetailsActivity.this, "Uptown");
            queryDB.retrieveAllData();

            coordsLong = queryDB.zoneLong;
            coordsLat = queryDB.zoneLat;

            shopsNames = queryDB.shopsNames.toArray(new String[queryDB.shopsNames.size()]);
            shopsX = queryDB.shopsX.toArray(new String[queryDB.shopsX.size()]);
            shopsY = queryDB.shopsY.toArray(new String[queryDB.shopsY.size()]);

            parksNames = queryDB.parksNames.toArray(new String[queryDB.parksNames.size()]);
            parksLat = queryDB.parksLat;
            parksLong = queryDB.parksLong;

            busStopX = queryDB.busStopX.toArray(new String[queryDB.busStopX.size()]);
            busStopY = queryDB.busStopY.toArray(new String[queryDB.busStopY.size()]);
            busStopNames = queryDB.busStopNames.toArray(new String[queryDB.busStopNames.size()]);

            recNames = queryDB.recNames.toArray(new String[queryDB.recNames.size()]);
            recX = queryDB.recX.toArray(new String[queryDB.recX.size()]);
            recY = queryDB.recY.toArray(new String[queryDB.recY.size()]);

            schoolsNames = queryDB.schoolsNames.toArray(new String[queryDB.schoolsNames.size()]);
            schoolsX = queryDB.schoolsX.toArray(new String[queryDB.schoolsX.size()]);
            schoolsY = queryDB.schoolsY.toArray(new String[queryDB.schoolsY.size()]);

            bundle = new Bundle();
            bundle.putStringArray("coordsLat", coordsLat);
            bundle.putStringArray("coordsLong", coordsLong);

            bundle.putStringArray("shopsNames", shopsNames);
            bundle.putStringArray("shopsX", shopsX);
            bundle.putStringArray("shopsY", shopsY);

            bundle.putStringArray("parksNames", parksNames);
            bundle.putStringArray("parksLat", parksLat);
            bundle.putStringArray("parksLong", parksLong);

            bundle.putStringArray("recNames", recNames);
            bundle.putStringArray("recX", recX);
            bundle.putStringArray("recY", recY);

            bundle.putStringArray("busStopNames", busStopNames);
            bundle.putStringArray("busStopX", busStopX);
            bundle.putStringArray("busStopY", busStopY);

            bundle.putStringArray("schoolsNames", schoolsNames);
            bundle.putStringArray("schoolsX", schoolsX);
            bundle.putStringArray("schoolsY", schoolsY);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new MapsActivity();
            fragmentTransaction.replace(R.id.mapView, fragment);
            fragmentTransaction.commit();
            fragment.setArguments(bundle);

            return null;
        }

        protected void onPostExecute(Void result) {

            CustomAdapter customAdapter = new CustomAdapter();
            zoneDetailsListView.setAdapter(customAdapter);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SavedZonesActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }

    private void putShopsMarkers() {
        fragment.showShops();
    }

    private void hideShopsMarkers() {
        fragment.hideShops();
    }

    private void putSchoolsMarkers() {
        fragment.showSchools();
    }

    private void hideSchoolMarkers() {
        fragment.hideSchools();
    }

    private void putParksMarkers() {
        fragment.showParks();
    }

    private void hideParksMarkers() {
        fragment.hideParks();
    }

    private void putRecMarkers() {
        fragment.showRec();
    }

    private void hideRecMarkers() {
        fragment.hideRec();
    }

    private void putBusStopMarkers() {
        fragment.showBusStops();
    }

    private void hideBusStopMarkers() {
        fragment.hideBusStops();
    }
}
