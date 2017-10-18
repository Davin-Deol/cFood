package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String[] distances;
    int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        distances = getResources().getStringArray(R.array.distances);
    }

    public void increment(View v) {
        String textValueID = v.getTag().toString();
        String buttonID = "inputValue" + textValueID;
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        TextView text = (TextView) findViewById(resID);

        if (value >= (distances.length - 1)) {
            text.setText("" + distances[value]);
        } else {
            value++;
            text.setText("" + distances[value]);
        }
    }

    public void decrement(View v) {
        String textValueID = v.getTag().toString();
        String buttonID = "inputValue" + textValueID;
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
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