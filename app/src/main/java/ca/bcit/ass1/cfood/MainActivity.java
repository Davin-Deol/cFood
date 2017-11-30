package ca.bcit.ass1.cfood;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import java.io.File;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class MainActivity extends AppCompatActivity {

    private DBHelper openHelper;
    SQLiteDatabase db;
    private String TAG = PopulateDB.class.getSimpleName();
    boolean tourMode = false;
    FloatingActionButton exploreFloatingActionButton;
    FloatingActionButton floatingActionButton;
    Button mainGetStartedButton;
    TourGuide mTourGuideHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openHelper = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            tourMode= false;
        } else {
            tourMode= extras.getBoolean("TOUR_MODE");
        }
        exploreFloatingActionButton = (FloatingActionButton) findViewById(R.id.mainExploreFloatingActionButton);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.mainFloatingActionButton);
        mainGetStartedButton = (Button) findViewById(R.id.mainGetStartedButton);
        checkDBExist();

        if (!tourMode) {
            exploreFloatingActionButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ExploreMapsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            floatingActionButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SavedZonesActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            mainGetStartedButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    v.setSelected(true);
                    Intent intent = new Intent(MainActivity.this, SavedZonesActivity.class);
                    intent.putExtra("TOUR_MODE", true);
                    startActivity(intent);
                }
            });
        } else {
            tourMainExploreButton();
        }
    }

    private void checkDBExist() {
        File file = this.getDatabasePath("cfood.db");
        if(!file.exists()) {
            PopulateDB populate = new PopulateDB(getApplicationContext(), this);
            populate.init();
        } else if(file.exists()) {
            db = openHelper.getReadableDatabase();
            Cursor cursor = openHelper.getAllZones(db);
            try {
                if(!cursor.moveToFirst()) {
                    PopulateDB populate = new PopulateDB(getApplicationContext(), this);
                    populate.init();
                }
            } catch (Exception e) {
                PopulateDB populate = new PopulateDB(getApplicationContext(), this);
                populate.init();
            }
            cursor.close();
        }
    }

    public void tourMainExploreButton() {
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(getString(R.string.tourMainExploreButtonHeader)).setDescription(getString(R.string.tourMainExploreButtonText)).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)).setGravity(Gravity.TOP))
                .setOverlay(new Overlay())
                .playOn(exploreFloatingActionButton);
        exploreFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExploreMapsActivity.class);
                intent.putExtra("TOUR_MODE", true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}