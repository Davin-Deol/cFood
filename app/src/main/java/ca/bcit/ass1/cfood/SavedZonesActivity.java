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

public class SavedZonesActivity extends AppCompatActivity {
    SavedZonesActivity.CustomAdapter customAdapter;
    boolean[] values = new boolean[10];
    boolean deleteMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_zones);
        Toolbar toolbar = findViewById(R.id.savedZonesToolbar);
        setSupportActionBar(toolbar);
        ListView listView = findViewById(R.id.savedZonesListView);
        customAdapter = new SavedZonesActivity.CustomAdapter(this, values);
        listView.setAdapter(customAdapter);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.savedZonesNavigation);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_saved_zones, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.remove:
                customAdapter.deleteModeChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            return 2;
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
            if (deleteMode) {
                view = getLayoutInflater().inflate(R.layout.layout_delete_saved_zone, null);
                final int index = i;
                final View finalView = view;
                CheckBox checkBox = finalView.findViewById(R.id.checkBox);
                checkBox.setChecked(items[i]);
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if (items[index]) {
                            items[index] = false;
                        } else {
                            items[index] = true;
                        }
                        notifyDataSetChanged();
                    }

                });
            } else {
                view = getLayoutInflater().inflate(R.layout.layout_saved_zone, null);
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SavedZonesActivity.this, ZoneDetailsActivity.class);
                        startActivity(i);
                    }

                });
            }
            TextView zoneID = view.findViewById(R.id.savedZonesZoneID);
            zoneID.setText("Zone " + i);
            TextView savedZonesZoneDescription = view.findViewById(R.id.savedZonesZoneDescription);
            savedZonesZoneDescription.setText("flksajhflk asdflk aslfka lskdfh kasdhf lkashf lkas dfkjhaflah l adslkf alsdk fl");
            return view;
        }

        public void deleteModeChanged() {
            if (deleteMode) {
                deleteMode = false;
            } else {
                deleteMode = true;
            }
            notifyDataSetChanged();
        }
    }
}
