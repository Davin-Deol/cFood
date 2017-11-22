package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SavedZonesActivity extends AppCompatActivity {
    SavedZonesActivity.CustomAdapter customAdapter;
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
        customAdapter = new SavedZonesActivity.CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private class CustomAdapter extends BaseAdapter {
        public CustomAdapter() {
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
            final View v = view;
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        v.setSelected(true);
                        Intent i = new Intent(SavedZonesActivity.this, ZoneDetailsActivity.class);
                        startActivity(i);
                    }

                });
            TextView zoneID = view.findViewById(R.id.savedZonesZoneID);
            zoneID.setText(neighbourhoods.get(i).neighbourhood);
            TextView savedZonesZoneDescription = view.findViewById(R.id.savedZonesZoneDescription);
            savedZonesZoneDescription.setText(neighbourhoods.get(i).description);
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
    }
}
