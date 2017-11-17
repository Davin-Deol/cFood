package ca.bcit.ass1.cfood;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
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

import java.util.ArrayList;

public class SavedZonesActivity extends AppCompatActivity {
    SavedZonesActivity.CustomAdapter customAdapter;
    private static int MAX_DESCRIPTION_LENGTH = 100;
    boolean[] values = new boolean[2];
    public ArrayList<Neighbourhood> neighbourhoods = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_zones);
        Toolbar toolbar = findViewById(R.id.savedZonesToolbar);
        setSupportActionBar(toolbar);
        boolean introMode = false;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            introMode= false;
        } else {
            introMode= extras.getBoolean("INTROMODE");
        }

        if (introMode) {
            this.setTitle("Intro Mode");
        }
        Neighbourhood neighbourhood1 = new Neighbourhood("Willington, Deer Lake", "flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk flflksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk flflksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk flflksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl", "5/5");
        Neighbourhood neighbourhood2 = new Neighbourhood("Bernard, CampBell", "dfk dlfalsdfd fd flkahld fdsj fsadl", "4/5");

        neighbourhoods.add(neighbourhood1);
        neighbourhoods.add(neighbourhood2);

        ListView listView = findViewById(R.id.savedZonesListView);
        customAdapter = new SavedZonesActivity.CustomAdapter(this, values);
        listView.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private class CustomAdapter extends BaseAdapter {
        private Context context; //context
        private boolean[] items; //data source of the list adapter
        public CustomAdapter(Context context, boolean[] items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return values.length;
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
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SavedZonesActivity.this, ZoneDetailsActivity.class);
                        startActivity(i);
                    }

                });
            TextView zoneID = view.findViewById(R.id.savedZonesZoneID);
            zoneID.setText(neighbourhoods.get(i).neighbourhood);
            TextView savedZonesZoneDescription = view.findViewById(R.id.savedZonesZoneDescription);
            if (neighbourhoods.get(i).description.length() > MAX_DESCRIPTION_LENGTH) {
                savedZonesZoneDescription.setText(neighbourhoods.get(i).description.substring(0, MAX_DESCRIPTION_LENGTH) + "...");
            } else {
                savedZonesZoneDescription.setText(neighbourhoods.get(i).description);
            }
            return view;
        }
    }
}
