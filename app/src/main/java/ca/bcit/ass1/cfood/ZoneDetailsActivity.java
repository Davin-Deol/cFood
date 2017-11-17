package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ZoneDetailsActivity extends AppCompatActivity {
    String[] categories;
    Neighbourhood sampleNeighbourhood = new Neighbourhood("Willington, Deer Lake", "flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl", "5/5");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = getResources().getStringArray(R.array.categories);
        setContentView(R.layout.activity_zone_details);
        Toolbar toolbar = findViewById(R.id.zoneDetailsToolbar);
        setSupportActionBar(toolbar);
        TextView zoneDetailsID = findViewById(R.id.zoneDetailsID);
        zoneDetailsID.setText(sampleNeighbourhood.neighbourhood);
        TextView zoneDetailsDescription = findViewById(R.id.zoneDetailsDescription);
        zoneDetailsDescription.setText(sampleNeighbourhood.description);
        ListView zoneDetailsListView = findViewById(R.id.zoneDetailsListView);
        CustomAdapter customAdapter = new CustomAdapter();
        zoneDetailsListView.setAdapter(customAdapter);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapsActivity fragment = new MapsActivity();
        fragmentTransaction.replace(R.id.mapView, fragment);
        fragmentTransaction.commit();
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
            TextView savedZonesSpecValue = view.findViewById(R.id.savedZonesSpecValue);
            savedZonesSpecValue.setText("Spec value: " + i);
            return view;
        }
    }
}
