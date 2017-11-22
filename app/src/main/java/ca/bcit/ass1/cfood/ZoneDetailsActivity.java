package ca.bcit.ass1.cfood;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setTitle(sampleNeighbourhood.neighbourhood.toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapsActivity fragment = new MapsActivity();
        fragmentTransaction.replace(R.id.mapView, fragment);
        fragmentTransaction.commit();
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
}
