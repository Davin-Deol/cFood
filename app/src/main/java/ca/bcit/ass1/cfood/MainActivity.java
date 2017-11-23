package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDBExist();

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.mainFloatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedZonesActivity.class);
                startActivity(intent);
            }
        });

        Button mainGetStartedButton = (Button) findViewById(R.id.mainGetStartedButton);
        mainGetStartedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.setSelected(true);
                Intent intent = new Intent(MainActivity.this, SavedZonesActivity.class);
                intent.putExtra("INTROMODE", true);
                startActivity(intent);
            }
        });

    }

    private void checkDBExist() {
        File file = this.getDatabasePath("cfood");
        if(!file.exists()) {
            PopulateDB populate = new PopulateDB(getApplicationContext(), this);
            populate.init();
        }
    }
}