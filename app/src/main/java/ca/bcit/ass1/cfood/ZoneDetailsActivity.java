package ca.bcit.ass1.cfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
    String neighbourhoodSelected;
    String description;
    String[] categories;
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

        neighbourhoodSelected = getIntent().getExtras().getString("NEIGHBOURHOOD_SELECTED");
        new getQuery().execute();

        categories = getResources().getStringArray(R.array.categories);
        checkboxes = new boolean[categories.length + 1];
        for (int i = 0; i < checkboxes.length; i++) {
            checkboxes[i] = true;
        }
        setContentView(R.layout.activity_zone_details);
        Toolbar toolbar = findViewById(R.id.zoneDetailsToolbar);
        setSupportActionBar(toolbar);

        description = getIntent().getExtras().getString("NEIGHBOURHOOD_DESCRIPTION");
        TextView zoneDetailsDescription = findViewById(R.id.zoneDetailsDescriptionTextView);
        zoneDetailsDescription.setText(description);

        zoneDetailsListView = findViewById(R.id.zoneDetailsListView);
        CustomAdapter customAdapter = new CustomAdapter(checkboxes);
        zoneDetailsListView.setAdapter(customAdapter);
        setTitle(neighbourhoodSelected);
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

        boolean[] values;
        public CustomAdapter(boolean[] values) {
            this.values = values;
        }

        @Override
        public int getCount() {
            return categories.length + 1;
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
            if (i == 0) {
                savedZonesSpecLabel.setText("ALL");
            } else {
                savedZonesSpecLabel.setText(categories[i - 1]);
            }
            CheckBox checkbox = (CheckBox) view.findViewById(R.id.savedZonesCheckbox);
            final int index = i;
            checkbox.setChecked(values[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (values[index]) {
                        values[index] = false;
                        checkBoxChanges(index, false);
                    } else {
                        values[index] = true;
                        checkBoxChanges(index, true);
                    }
                    notifyDataSetChanged();
                }
            });

            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (values[index]) {
                        values[index] = false;
                        checkBoxChanges(index, false);
                    } else {
                        values[index] = true;
                        checkBoxChanges(index, true);
                    }
                    notifyDataSetChanged();
                }
            });
            return view;
        }

        private void checkBoxChanges(int index, boolean show) {
            if (show) {
                if (index == 0) {
                    for (int i = 0; i < values.length; i++) {
                        values[i] = true;
                    }
                    putBusStopMarkers();
                    putShopsMarkers();
                    putRecMarkers();
                    putParksMarkers();
                    putSchoolsMarkers();
                } else {
                    switch (index) {
                        case 1:
                            putBusStopMarkers();
                            break;
                        case 2:
                            putShopsMarkers();
                            break;
                        case 3:
                            putRecMarkers();
                            break;
                        case 4:
                            putParksMarkers();
                            break;
                        case 5:
                            putSchoolsMarkers();
                            break;
                        default:
                            break;
                    }
                }
            } else {
                if (index == 0) {
                    for (int i = 0; i < values.length; i++) {
                        values[i] = false;
                    }
                    hideBusStopMarkers();
                    hideShopsMarkers();
                    hideRecMarkers();
                    hideParksMarkers();
                    hideSchoolsMarkers();
                } else {
                    switch (index) {
                        case 1:
                            hideBusStopMarkers();
                            break;
                        case 2:
                            hideShopsMarkers();
                            break;
                        case 3:
                            hideRecMarkers();
                            break;
                        case 4:
                            hideParksMarkers();
                            break;
                        case 5:
                            hideSchoolsMarkers();
                            break;
                        default:
                            break;
                    }
                }
            }
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
            QueryDB queryDB = new QueryDB(getApplicationContext(), ZoneDetailsActivity.this, neighbourhoodSelected);
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

        @Override
        protected void onPostExecute(Void result) {
            CustomAdapter customAdapter = new CustomAdapter(checkboxes);
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

    private void hideSchoolsMarkers() {
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

    public void refreshMyData(){
        Intent i = new Intent(ZoneDetailsActivity.this, ZoneDetailsActivity.class);
        i.putExtra("NEIGHBOURHOOD_SELECTED", neighbourhoodSelected);
        i.putExtra("NEIGHBOURHOOD_DESCRIPTION", description);
        startActivity(i);
        this.overridePendingTransition(0, 0);
    }
}
