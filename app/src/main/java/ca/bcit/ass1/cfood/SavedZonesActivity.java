package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
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

import org.w3c.dom.Text;

public class SavedZonesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setContentView(R.layout.activity_saved_zones);
        ListView listView = findViewById(R.id.savedZonesListView);

        SavedZonesActivity.CustomAdapter customAdapter = new SavedZonesActivity.CustomAdapter();
        listView.setAdapter(customAdapter);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.favourites);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                switch(item.getItemId()) {
                    case R.id.search:
                        intent = new Intent(SavedZonesActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
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
            zoneID.setText("Zone " + i);
            TextView savedZonesZoneDescription = view.findViewById(R.id.savedZonesZoneDescription);
            savedZonesZoneDescription.setText("flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas ");
            return view;
        }
    }
}
