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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    View wholePage;
    private int tourPhase = 0;
    boolean tourMode = false;
    boolean endOfTour = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_zones);
        toolbar = findViewById(R.id.savedZonesToolbar);
        wholePage = findViewById(R.id.savedZonesActivity);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            tourMode= false;
        } else {
            tourMode= extras.getBoolean("TOUR_MODE");
            endOfTour = extras.getBoolean("END_OF_TOUR");
        }
        QueryDB queryDB;

        try {
            JSONArray parentArray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                queryDB = new QueryDB(getApplicationContext(), SavedZonesActivity.this, finalObject.getString("NEIGH_NAME"));
                if (finalObject != null) {
                    neighbourhoods.add(new Neighbourhood(finalObject.getString("NEIGH_NAME"),
                            queryDB.getDesc(finalObject.getString("NEIGH_NAME"))));
                }
            }
        } catch (JSONException e) {
            System.err.print(e.toString());
        }
        if (tourMode) {
            beginTour();
        } else {
            listView = findViewById(R.id.savedZonesListView);
            customAdapter = new SavedZonesActivity.CustomAdapter(tourMode);
            listView.setAdapter(customAdapter);
        }
        if (endOfTour) {
            finalClickMe();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private class CustomAdapter extends BaseAdapter {
        boolean tourMode = false;
        boolean[] animatedAlready = new boolean[neighbourhoods.size()];
        public CustomAdapter(boolean tourMode) {
            this.tourMode = tourMode;
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
            final View v = view;
            final int index = i;
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    v.setSelected(true);
                    Intent i = new Intent(SavedZonesActivity.this, ZoneDetailsActivity.class);
                    i.putExtra("NEIGHBOURHOOD_SELECTED", neighbourhoods.get(index).neighbourhood);
                    i.putExtra("NEIGHBOURHOOD_DESCRIPTION", neighbourhoods.get(index).description);
                    i.putExtra("TOUR_MODE", tourMode);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            });
            if (!animatedAlready[i]) {
                animatedAlready[i] = true;
                Animation animation = AnimationUtils.loadAnimation(SavedZonesActivity.this, R.anim.slide_in_right);
                animation.setStartOffset(200);
                animation.setDuration(500);
                view.startAnimation(animation);
            }
            return view;
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
     * This is the part where we talk about the activity's layout
     */
    public void beginTour() {
        wholePage.bringToFront();
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourIntroductionHeader)).setDescription(getString(R.string.tourIntroductionText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
                .setOverlay(new Overlay())
                .playOn(toolbar);
        wholePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourAppLayout();
            }
        });
    }

    public void tourAppLayout() {
        mTourGuideHandler.cleanUp();
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourAppLayoutHeader)).setDescription(getString(R.string.tourAppLayoutText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
                .setOverlay(new Overlay())
                .playOn(toolbar);
        wholePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourNeighbourhoodActivityOverview();
            }
        });
    }

    /**
     * This is the part where we talk about what the activity consists of
     */
    public void tourNeighbourhoodActivityOverview() {
        mTourGuideHandler.cleanUp();
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourNeighbourhoodActivityOverviewHeader)).setDescription(getString(R.string.tourNeighbourhoodActivityOverviewText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
                .setOverlay(new Overlay())
                .playOn(toolbar);
        wholePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourDatabase();
            }
        });
    }

    /**
     * This is the part where we talk about what the activity consists of
     */
    public void tourDatabase() {
        mTourGuideHandler.cleanUp();
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourDatabaseHeader)).setDescription(getString(R.string.tourDatabaseText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)))
                .setOverlay(new Overlay())
                .playOn(toolbar);
        wholePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourNeighbourhoodContent();
            }
        });
    }

    /**
     * This is the part where we just show the listview
     */
    public void tourNeighbourhoodContent() {
        mTourGuideHandler.cleanUp();
        toolbar.setOnClickListener(null);
        listView = findViewById(R.id.savedZonesListView);
        customAdapter = new SavedZonesActivity.CustomAdapter(tourMode);
        listView.setAdapter(customAdapter);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourNeighbourhoodContentHeader)).setDescription(getString(R.string.tourNeighbourhoodContentText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.TOP))
                .setOverlay(new Overlay())
                .playOn(listView);
        wholePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMe4(view);
            }
        });
    }

    /**
     * This is the part where we just show a list item
     * @param view
     */
    public void clickMe4(View view) {
        mTourGuideHandler.cleanUp();
        wholePage.setClickable(false);
        listView = findViewById(R.id.savedZonesListView);
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_4)).setDescription(getString(R.string.tourDescription_4)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.BOTTOM))
                .setOverlay(new Overlay())
                .playOn(listView.getChildAt(0));
    }

    public void finalClickMe() {
        wholePage.bringToFront();
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourTitle_11)).setDescription(getString(R.string.tourDescription_11)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.BOTTOM))
                .setOverlay(new Overlay())
                .playOn(toolbar);
        wholePage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                endTour();
            }
        });
    }

    public void endTour() {
        wholePage.setClickable(false);
        mTourGuideHandler.cleanUp();
    }
}
