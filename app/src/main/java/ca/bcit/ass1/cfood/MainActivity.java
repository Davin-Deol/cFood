package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.mainFloatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                generate(null);
            }
        });

    }

    /**
     * This function is only used by the button that starts the search
     * @param v - holds the button view
     */
    public void generate(View v) {
        Intent intent = new Intent(MainActivity.this, SavedZonesActivity.class);
        startActivity(intent);
    }
}