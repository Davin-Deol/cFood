package ca.bcit.ass1.cfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

import java.util.ArrayList;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;


public class ZoneDetailsActivity extends AppCompatActivity{
    String neighbourhoodSelected;
    String title;
    String description;
    String[] categories;
    private boolean[] checkboxes;
    Menu menu;
    CheckBox checkbox;
    TourGuide mTourGuideHandler;
    Toolbar toolbar;
    MapView mMap;
    View wholePage;
    private int x = 0;

    private ShareActionProvider share = null;
    private ProgressDialog pDialog;

    boolean[] values;

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
    TextView zoneDetailsDescription;
    CustomAdapter customAdapter;
    private boolean tourMode = false;
    int tourPhase = 0;
    boolean citySelected = true;
    boolean cityRadioSelected = true;
    boolean nbRadioSelected = false;

    ArrayList<CheckBox> allCheckBoxes = new ArrayList<CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        neighbourhoodSelected = getIntent().getExtras().getString("NEIGHBOURHOOD_SELECTED");
        new getQuery().execute();

        categories = getResources().getStringArray(R.array.categories);
        checkboxes = new boolean[categories.length];
        for (int i = 0; i < checkboxes.length; i++) {
            checkboxes[i] = true;
        }
        setContentView(R.layout.activity_zone_details);
        toolbar = findViewById(R.id.zoneDetailsToolbar);
        wholePage = findViewById(R.id.zoneDetailsActivity);
        setSupportActionBar(toolbar);
        setTitle(neighbourhoodSelected);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mMap = findViewById(R.id.mapView);
        description = getIntent().getExtras().getString("NEIGHBOURHOOD_DESCRIPTION");
        zoneDetailsDescription = findViewById(R.id.zoneDetailsDescriptionTextView);

        zoneDetailsListView = findViewById(R.id.zoneDetailsListView);
        tourMode = getIntent().getExtras().getBoolean("TOUR_MODE");
        customAdapter = new CustomAdapter(checkboxes);
        zoneDetailsListView.setAdapter(customAdapter);
        if (tourMode) {
            clickMe1();
        } else {
            zoneDetailsDescription.setText(description);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_neighbourhood_details, menu);

        MenuItem item = menu.findItem(R.id.createEventItem);

        share = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        MenuItemCompat.setActionProvider(item, share);

        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT, "Interested in moving to: " +
                neighbourhoodSelected + "\n\n" + "Brief Overview: " + description);
        share.setShareIntent(myShareIntent);

        return(super.onCreateOptionsMenu(menu));
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
            savedZonesSpecLabel.setText(categories[i]);
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
            if (citySelected) {
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
            } else {
                if (show) {
                    if (index == 0) {
                        for (int i = 0; i < values.length; i++) {
                            values[i] = true;
                        }
                        putBusStopMarkersNB();
                        putShopsMarkersNB();
                        putRecMarkersNB();
                        putParksMarkersNB();
                        putSchoolsMarkersNB();
                    } else {
                        switch (index) {
                            case 1:
                                putBusStopMarkersNB();
                                break;
                            case 2:
                                putShopsMarkersNB();
                                break;
                            case 3:
                                putRecMarkersNB();
                                break;
                            case 4:
                                putParksMarkersNB();
                                break;
                            case 5:
                                putSchoolsMarkersNB();
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
                        hideBusStopMarkersNB();
                        hideShopsMarkersNB();
                        hideRecMarkersNB();
                        hideParksMarkersNB();
                        hideSchoolsMarkersNB();
                    } else {
                        switch (index) {
                            case 1:
                                hideBusStopMarkersNB();
                                break;
                            case 2:
                                hideShopsMarkersNB();
                                break;
                            case 3:
                                hideRecMarkersNB();
                                break;
                            case 4:
                                hideParksMarkersNB();
                                break;
                            case 5:
                                hideSchoolsMarkersNB();
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private class getQuery extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ZoneDetailsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
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
            pDialog.dismiss();
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

    private void putShopsMarkersNB() {
        fragment.showShopsNB();
    }

    private void hideShopsMarkersNB() {
        fragment.hideShopsNB();
    }

    private void putSchoolsMarkersNB() {
        fragment.showSchoolsNB();
    }

    private void hideSchoolsMarkersNB() {
        fragment.hideSchoolsNB();
    }

    private void putParksMarkersNB() {
        fragment.showParksNB();
    }

    private void hideParksMarkersNB() {
        fragment.hideParksNB();
    }

    private void putRecMarkersNB() {
        fragment.showRecNB();
    }

    private void hideRecMarkersNB() {
        fragment.hideRecNB();
    }

    private void putBusStopMarkersNB() {
        fragment.showBusStopsNB();
    }

    private void hideBusStopMarkersNB() {
        fragment.hideBusStopsNB();
    }

    public void refreshMyData(){
        Intent i = new Intent(ZoneDetailsActivity.this, ZoneDetailsActivity.class);
        i.putExtra("NEIGHBOURHOOD_SELECTED", neighbourhoodSelected);
        i.putExtra("NEIGHBOURHOOD_DESCRIPTION", description);
        i.putExtra("TOUR_MODE", tourMode);
        startActivity(i);
        this.overridePendingTransition(0, 0);
    }

    public void onRadioButtonClicked(View view) {
        RadioButton cityRadio = (RadioButton) view.findViewById(R.id.cityRadio);
        RadioButton neighbourhoodRadio = (RadioButton) view.findViewById(R.id.neighbourhoodRadio);


        switch(view.getId()) {
            case R.id.cityRadio:
                if(!cityRadioSelected && nbRadioSelected) {
                    citySelected = true;
                    cityRadio.setChecked(true);
                    hideBusStopMarkersNB();
                    hideShopsMarkersNB();
                    hideRecMarkersNB();
                    hideParksMarkersNB();
                    hideSchoolsMarkersNB();

                    putBusStopMarkers();
                    putParksMarkers();
                    putRecMarkers();
                    putShopsMarkers();
                    putSchoolsMarkers();

                    for(int i = 0; i < allCheckBoxes.size(); i++) {
                        allCheckBoxes.get(i).setChecked(true);
                        if(i < checkboxes.length)
                            checkboxes[i] = true;
                    }
                }
                cityRadioSelected = true;
                nbRadioSelected = false;


                break;
            case R.id.neighbourhoodRadio:
                if(!nbRadioSelected && cityRadioSelected) {
                    citySelected = false;
                    neighbourhoodRadio.setChecked(true);
                    hideBusStopMarkers();
                    hideShopsMarkers();
                    hideRecMarkers();
                    hideParksMarkers();
                    hideSchoolsMarkers();

                    putBusStopMarkersNB();
                    putParksMarkersNB();
                    putRecMarkersNB();
                    putShopsMarkersNB();
                    putSchoolsMarkersNB();

                    for(int i = 0; i < allCheckBoxes.size(); i++) {
                        allCheckBoxes.get(i).setChecked(true);
                        if(i < checkboxes.length)
                            checkboxes[i] = true;
                    }
                }

                cityRadioSelected = false;
                nbRadioSelected = true;

                break;
        }
    }

    /**
     * This is the part where we talk about what the activity consists of
     */
    public void clickMe1() {
        ++tourPhase;
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_5)).setDescription(getString(R.string.tourDescription_5)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
                .setOverlay(new Overlay())
                .playOn(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMe2(view);
            }
        });
    }

    /**
     * This is the part where we talk about the map fragment
     */
    public void clickMe2(View view) {
        ++tourPhase;
        mTourGuideHandler.cleanUp();
        toolbar.setOnClickListener(null);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_6)).setDescription(getString(R.string.tourDescription_6)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
                .setOverlay(new Overlay())
                .playOn(mMap);
        findViewById(R.id.zoneDetailsActivity).bringToFront();
        findViewById(R.id.zoneDetailsActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMe3(view);
            }
        });
    }

    /**
     * This is the part where we talk about the description view
     */
    public void clickMe3(View view) {
        ++tourPhase;
        mTourGuideHandler.cleanUp();
        wholePage.setOnClickListener(null);
        zoneDetailsDescription.setText(description);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_7)).setDescription(getString(R.string.tourDescription_7)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
                .setOverlay(new Overlay())
                .playOn(zoneDetailsDescription);
        wholePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMe4(view);
            }
        });
    }

    /**
     * This is the part where we talk about the list view
     */
    public void clickMe4(View view) {
        ++tourPhase;
        mTourGuideHandler.cleanUp();
        wholePage.setOnClickListener(null);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_8)).setDescription(getString(R.string.tourDescription_8)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.TOP))
                .setOverlay(new Overlay())
                .playOn(zoneDetailsListView);
        wholePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMe5(v);
            }
        });
    }

    /**
     * This is the part where we talk about the list item
     */
    public void clickMe5(View view) {
        mTourGuideHandler.cleanUp();
        wholePage.setClickable(false);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_9)).setDescription(getString(R.string.tourDescription_9)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.TOP))
                .setOverlay(new Overlay())
                .playOn(zoneDetailsListView.getChildAt(0));
        zoneDetailsListView.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxes[0] = !checkboxes[0];
                customAdapter.checkBoxChanges(0, checkboxes[0]);
                clickMe6(v);
            }
        });
    }

    /**
     * This is the part where we talk about the list item
     */
    public void clickMe6(View view) {
        wholePage.setClickable(true);
        mTourGuideHandler.cleanUp();
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_10)).setDescription(getString(R.string.tourDescription_10)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.BOTTOM))
                .setOverlay(new Overlay())
                .playOn(mMap);
        wholePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ZoneDetailsActivity.this, SavedZonesActivity.class);
                i.putExtra("END_OF_TOUR", true);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
            }
        });
    }
}
