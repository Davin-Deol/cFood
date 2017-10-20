package ca.bcit.ass1.cfood;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int[] arrayB = getIntent().getExtras().getIntArray("INPUTS");
        String test = "" + arrayB[0];

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final Neighbourhood[] resultsArray = {
                new Neighbourhood("Neighbourhood 1", getResources().getStringArray(R.array.distances)[arrayB[0]], "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 2", "Description - bad place to live", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 3", "Description - high density", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 4", "Description - far away from everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 5", "Description - close to everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 1", "Description - noisy area", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 2", "Description - bad place to live", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 3", "Description - high density", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 4", "Description - far away from everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 5", "Description - close to everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 1", "Description - noisy area", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 2", "Description - bad place to live", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 3", "Description - high density", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 4", "Description - far away from everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Neighbourhood("Neighbourhood 5", "Description - close to everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2")
        };

        ListView resultsList = new ListView(this);
        ArrayAdapter<Neighbourhood> resultsAdapter = new ArrayAdapter<Neighbourhood>(this,
                0, resultsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Neighbourhood currentResult = resultsArray[position];
                if(convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.activity_list, null, false);
                }
                TextView resultsNHood = (TextView)convertView.findViewById(R.id.results);
                TextView resultsDesc = (TextView)convertView.findViewById(R.id.resultsDescription);
                TextView resultsRate = (TextView)convertView.findViewById(R.id.resultsRating);

                resultsNHood.setText(currentResult.neighbourhood);
                resultsDesc.setText(currentResult.description);
                resultsRate.setText(currentResult.rating);

                return convertView;
            }
        };

        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3 ) {
                TextView expandable = (TextView)view.findViewById(R.id.expandable);
                TextView resultsNHood = (TextView)view.findViewById(R.id.results);
                TextView resultsDesc = (TextView)view.findViewById(R.id.resultsDescription);
                TextView resultsRate = (TextView)view.findViewById(R.id.resultsRating);

                if ( expandable.getVisibility() == View.GONE)
                {
                    //expandedChildList.set(arg2, true);
//                    resultsNHood.setVisibility(View.GONE);
//                    resultsDesc.setVisibility(View.GONE);
//                    resultsRate.setVisibility(View.GONE);
                    expandable.setVisibility(View.VISIBLE);
                }
                else
                {
                    //expandedChildList.set(arg2, false);
//                    resultsNHood.setVisibility(View.VISIBLE);
//                    resultsDesc.setVisibility(View.VISIBLE);
//                    resultsRate.setVisibility(View.VISIBLE);
                    expandable.setVisibility(View.GONE);
                }
            }
        });
        setContentView(resultsList);
        resultsList.setAdapter(resultsAdapter);


    }

}
