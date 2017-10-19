package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String[] distances;
    String[] categories;
    int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categories = getResources().getStringArray(R.array.categories);
        distances = getResources().getStringArray(R.array.distances);
        ListView listView = (ListView) findViewById(R.id.listView);

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter {

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
            view = getLayoutInflater().inflate(R.layout.inputlayout, null);
            TextView labelTextView = (TextView) view.findViewById(R.id.inputLabel);
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

            TextView valueTextView = (TextView) view.findViewById(R.id.inputValue);
            String valueID = "inputValue" + i;
            int valueResID = getResources().getIdentifier(valueID, "id", getPackageName());
            valueTextView.setId(i);

            Button incrementButton = (Button) view.findViewById(R.id.incrementButton);
            incrementButton.setTag(i);

            Button decrementButton = (Button) view.findViewById(R.id.decrementButton);
            decrementButton.setTag(i);

            return view;
        }
    }

    public void increment(View v) {
        String textValueID = v.getTag().toString();
        String valueID = "inputValue" + textValueID;
        TextView text = (TextView) findViewById(Integer.parseInt(textValueID));

        if (value >= (distances.length - 1)) {
            text.setText("" + distances[value]);
        } else {
            value++;
            text.setText("" + distances[value]);
        }
    }

    public void decrement(View v) {
        String textValueID = v.getTag().toString();
        int resID = getResources().getIdentifier(textValueID, "id", getPackageName());
        TextView text = (TextView) findViewById(resID);

        if (value <= 0) {
            text.setText("" + distances[value]);
        } else {
            value--;
            text.setText("" + distances[value]);
        }
    }

    /**
     * This function is only used by the button that starts the search
     * @param v
     */
    public void generate(View v) {
        Intent intent = new Intent(MainActivity.this, Results.class);

        startActivity(intent);
    }
}