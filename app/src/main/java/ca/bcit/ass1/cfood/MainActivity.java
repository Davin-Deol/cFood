package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private DBHelper openHelper;
    SQLiteDatabase db;
    private String TAG = PopulateDB.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openHelper = new DBHelper(this);

        checkDBExist();

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.mainFloatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedZonesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        Button mainGetStartedButton = (Button) findViewById(R.id.mainGetStartedButton);
        mainGetStartedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.setSelected(true);
                Intent intent = new Intent(MainActivity.this, SavedZonesActivity.class);
                intent.putExtra("TOUR_MODE", true);
                startActivity(intent);
            }
        });

    }

    private void checkDBExist() {
        File file = this.getDatabasePath("cfood.db");
        if(!file.exists()) {
            PopulateDB populate = new PopulateDB(getApplicationContext(), this);
            populate.init();
        } else if(file.exists()) {
            db = openHelper.getReadableDatabase();
            Cursor cursor = openHelper.getAllZones(db);
            String name = null;
            try {
                if(cursor.moveToFirst()) {
                    name = cursor.getString(1);
                }
            } catch (Exception e) {
                PopulateDB populate = new PopulateDB(getApplicationContext(), this);
                populate.init();
            }
            cursor.close();
        }
    }
}