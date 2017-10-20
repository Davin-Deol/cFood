package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
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

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
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
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.search:
                generate(null);
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
            labelTextView.setText(categories[i]);

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

            TextView valueTextView = view.findViewById(R.id.inputValue);
            valueTextView.setId(i);

            Button incrementButton = view.findViewById(R.id.incrementButton);
            incrementButton.setTag(i);

            Button decrementButton = view.findViewById(R.id.decrementButton);
            decrementButton.setTag(i);

            return view;
        }
    }

    public void increment(View v) {
        String textValueID = v.getTag().toString();
        TextView text = findViewById(Integer.parseInt(textValueID));
        int valueIndex = Integer.parseInt(textValueID);
        if (value[valueIndex] >= (distances.length - 1)) {
            text.setText(distances[value[valueIndex]]);
        } else {
            value[Integer.parseInt(textValueID)]++;
            text.setText(distances[value[valueIndex]]);
        }
    }

    public void decrement(View v) {
        String textValueID = v.getTag().toString();
        int resID = getResources().getIdentifier(textValueID, "id", getPackageName());
        TextView text = findViewById(resID);
        int valueIndex = Integer.parseInt(textValueID);
        if (value[valueIndex] <= 0) {
            text.setText(distances[value[valueIndex]]);
        } else {
            value[Integer.parseInt(textValueID)]--;
            text.setText(distances[value[valueIndex]]);
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