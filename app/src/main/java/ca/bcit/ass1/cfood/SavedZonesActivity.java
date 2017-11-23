package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class SavedZonesActivity extends AppCompatActivity {
    SavedZonesActivity.CustomAdapter customAdapter;
    public ArrayList<Neighbourhood> neighbourhoods = new ArrayList<>();
    TourGuide mTourGuideHandler;
    Toolbar toolbar;
    ListView listView;
    private int tourPhase = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_zones);
        toolbar = findViewById(R.id.savedZonesToolbar);
        setSupportActionBar(toolbar);
        boolean introMode = false;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            introMode= false;
        } else {
            introMode= extras.getBoolean("INTROMODE");
        }

        try {
            JSONArray parentArray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                if (finalObject != null) {
                    neighbourhoods.add(new Neighbourhood(finalObject.getString("NEIGH_NAME"), "flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk flflksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk flflksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk flflksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl"));
                }
            }
        } catch (JSONException e) {
            System.err.print(e.toString());
        }
        if (introMode) {
            mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                    .setPointer(new Pointer())
                    .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_1)).setDescription(getString(R.string.tourDescription_1)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
                    .setOverlay(new Overlay())
                    .playOn(toolbar);
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickMe(view);
                }
            });
        } else {
            listView = findViewById(R.id.savedZonesListView);
            customAdapter = new SavedZonesActivity.CustomAdapter(false, 0);
            listView.setAdapter(customAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private class CustomAdapter extends BaseAdapter {
        boolean tourMode = false;
        int tourPhase = 0;
        public CustomAdapter(boolean tourMode, int tourPhase) {
            this.tourMode = tourMode;
            this.tourPhase = tourPhase;
        }

        @Override
        public int getCount() {
            return neighbourhoods.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.layout_saved_zone, null);
            TextView zoneID = view.findViewById(R.id.savedZonesZoneID);
            zoneID.setText(neighbourhoods.get(i).neighbourhood);
            TextView savedZonesZoneDescription = view.findViewById(R.id.savedZonesZoneDescription);
            savedZonesZoneDescription.setText(neighbourhoods.get(i).description);
            if (!tourMode) {
                final View v = view;
                final int index = i;
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        v.setSelected(true);
                        Intent i = new Intent(SavedZonesActivity.this, ZoneDetailsActivity.class);
                        i.putExtra("NEIGHBOURHOOD_SELECTED", neighbourhoods.get(index).neighbourhood);
                        i.putExtra("NEIGHBOURHOOD_DESCRIPTION", neighbourhoods.get(index).description);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                });
                return view;
            } else {

                // Only if we're at the part where we tap the item and nothing happens
                if (tourPhase == 4) {
                    // Only make the first list item clickable
                    if (i == 0) {
                        mTourGuideHandler = TourGuide.init(SavedZonesActivity.this).with(TourGuide.Technique.Click)
                                .setPointer(new Pointer())
                                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_4)).setDescription(getString(R.string.tourDescription_4)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
                                .setOverlay(new Overlay())
                                .playOn(view);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clickMe4(view);
                            }
                        });
                    }
                } else if (tourPhase == 5) {
                    if (i == 0) {
                        final View v = view;
                        final int index = i;
                        mTourGuideHandler = TourGuide.init(SavedZonesActivity.this).with(TourGuide.Technique.Click)
                                .setPointer(new Pointer())
                                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_5)).setDescription(getString(R.string.tourDescription_5)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
                                .setOverlay(new Overlay())
                                .playOn(view);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v.setSelected(true);
                                Intent i = new Intent(SavedZonesActivity.this, ZoneDetailsActivity.class);
                                i.putExtra("NEIGHBOURHOOD_SELECTED", neighbourhoods.get(index).neighbourhood);
                                i.putExtra("NEIGHBOURHOOD_DESCRIPTION", neighbourhoods.get(index).description);
                                i.putExtra("TOUR_MODE", false);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        });
                    }
                }
                return view;
            }
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
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("nb.json");
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

    /**
     * This is the part where we talk about what the activity consists of
     * @param view
     */
    public void clickMe(View view) {
        mTourGuideHandler.cleanUp();
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_2)).setDescription(getString(R.string.tourDescription_2)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
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
     * This is the part where we just show the listview
     * @param view
     */
    public void clickMe2(View view) {
        mTourGuideHandler.cleanUp();
        toolbar.setOnClickListener(null);
        listView = findViewById(R.id.savedZonesListView);
        customAdapter = new SavedZonesActivity.CustomAdapter(true, 3);
        listView.setAdapter(customAdapter);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_3)).setDescription(getString(R.string.tourDescription_3)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.TOP))
                .setOverlay(new Overlay())
                .playOn(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickMe3(view);
            }
        });
    }

    /**
     * This is the part where we just show a list item
     * @param view
     */
    public void clickMe3(View view) {
        mTourGuideHandler.cleanUp();
        listView = findViewById(R.id.savedZonesListView);
        customAdapter = new SavedZonesActivity.CustomAdapter(true, 4);
        listView.setAdapter(customAdapter);
    }

    /**
     * This is the part where tapping the list item takes them to the next activity
     * @param view
     */
    public void clickMe4(View view) {
        mTourGuideHandler.cleanUp();
        listView = findViewById(R.id.savedZonesListView);
        customAdapter = new SavedZonesActivity.CustomAdapter(true, 5);
        listView.setAdapter(customAdapter);
    }
}
