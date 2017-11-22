package ca.bcit.ass1.cfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
    Neighbourhood sampleNeighbourhood = new Neighbourhood("Willington, Deer Lake", "flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl", "5/5");
    private boolean[] checkboxes;

    private ProgressDialog pDialog;

    String [] coordsLong;
    String [] coordsLat;

    String [] allShopsNames;
    Parcelable[] allShopsLatLng;

    String [] allShopsX;
    String [] allShopsY;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new getQuery().execute();
        String[] test = coordsLong;
        String [] blah =  allShopsX;
        String [] blah2 = allShopsY;


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

        ListView zoneDetailsListView = findViewById(R.id.zoneDetailsListView);
        CustomAdapter customAdapter = new CustomAdapter();
        zoneDetailsListView.setAdapter(customAdapter);
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
        getMenuInflater().inflate(R.menu.menu_neighbourhood_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, SavedZonesActivity.class));
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
                    } else {
                        checkboxes[index] = true;
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

            allShopsNames = queryDB.shopsNames.toArray(new String[queryDB.shopsNames.size()]);
            allShopsX = queryDB.shopsX.toArray(new String[queryDB.shopsX.size()]);
            allShopsY = queryDB.shopsY.toArray(new String[queryDB.shopsY.size()]);

            parksLat = queryDB.parksLat;
            parksLong = queryDB.parksLong;
            parksNames = queryDB.parksNames.toArray(new String[queryDB.parksNames.size()]);

            busStopX = queryDB.busStopX.toArray(new String[queryDB.busStopX.size()]);
            busStopY = queryDB.busStopY.toArray(new String[queryDB.busStopY.size()]);
            busStopNames = queryDB.busStopNames.toArray(new String[queryDB.busStopNames.size()]);

            recX = queryDB.recX.toArray(new String[queryDB.recX.size()]);
            recY = queryDB.recY.toArray(new String[queryDB.recY.size()]);
            recNames = queryDB.recNames.toArray(new String[queryDB.recNames.size()]);

            schoolsX = queryDB.schoolsX.toArray(new String[queryDB.schoolsX.size()]);
            schoolsY = queryDB.schoolsY.toArray(new String[queryDB.schoolsY.size()]);
            schoolsNames = queryDB.schoolsNames.toArray(new String[queryDB.schoolsNames.size()]);

            bundle = new Bundle();
            bundle.putStringArray("coordsLat", coordsLat);
            bundle.putStringArray("coordsLong", coordsLong);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MapsActivity fragment = new MapsActivity();
            fragmentTransaction.replace(R.id.mapView, fragment);
            fragmentTransaction.commit();
            fragment.setArguments(bundle);

            return null;
        }

        protected void onPostExecute(Void result) {

//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            MapsActivity fragment = new MapsActivity();
//            fragment.setArguments(bundle);
//            fragmentTransaction.replace(R.id.mapView, fragment);
//            fragmentTransaction.commit();
        }
    }
}
