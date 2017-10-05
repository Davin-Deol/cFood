package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void increment(View v) {
        String textValueID = v.getTag().toString();
        String buttonID = "inputValue" + textValueID;
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        TextView text = (TextView) findViewById(resID);

        int value = Integer.parseInt(text.getText().toString());

        if (value >= 9) {
            text.setText("" + value);
        } else {
            value++;
            text.setText("" + value);
        }
    }

    public void decrement(View v) {
        String textValueID = v.getTag().toString();
        String buttonID = "inputValue" + textValueID;
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        TextView text = (TextView) findViewById(resID);

        int value = Integer.parseInt(text.getText().toString());

        if (value <= 0) {
            text.setText("" + value);
        } else {
            value--;
            text.setText("" + value);
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