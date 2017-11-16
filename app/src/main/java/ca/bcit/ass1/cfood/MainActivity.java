package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String[] distances;
    String[] categories;
    int[] value;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = getResources().getStringArray(R.array.categories);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        distances = getResources().getStringArray(R.array.distances);
        value = new int[categories.length];
        for (int i = 0; i < value.length; i++) {
            value[i] = 0;
        }
        ListView listView = findViewById(R.id.listView);

        customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                switch(item.getItemId()) {
                    case R.id.favourites:
                        intent = new Intent(MainActivity.this, SavedZonesActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.mainFloatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                generate(null);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.clear:
                value = new int[categories.length];
                customAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            view = getLayoutInflater().inflate(R.layout.input_layout, null);
            TextView labelTextView = view.findViewById(R.id.inputLabel);
            final TextView valueTextView = view.findViewById(R.id.inputValue);
            Button incrementButton = view.findViewById(R.id.incrementButton);
            Button decrementButton = view.findViewById(R.id.decrementButton);

            labelTextView.setText(categories[i]);
            valueTextView.setText(distances[value[i]]);

            if (labelTextView.getText().length() < 10) {
                labelTextView.setTextSize(20);
            } else if (labelTextView.getText().length() < 20) {
                labelTextView.setTextSize(18);
            } else if (labelTextView.getText().length() < 30) {
                labelTextView.setTextSize(16);
            } else if (labelTextView.getText().length() < 40) {
                labelTextView.setTextSize(14);
            } else if (labelTextView.getText().length() < 50) {
                labelTextView.setTextSize(12);
            }

            final int index = i;
            incrementButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (value[index] < (distances.length - 1)) {
                        value[index]++;
                    }
                    notifyDataSetChanged();
                }
            });

            decrementButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (value[index] > 0) {
                        value[index]--;
                    }
                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }

    /**
     * This function is only used by the button that starts the search
     * @param v - holds the button view
     */
    public void generate(View v) {
        Intent intent = new Intent(MainActivity.this, Results.class);
        intent.putExtra("INPUTS", value);
        startActivity(intent);
    }
}