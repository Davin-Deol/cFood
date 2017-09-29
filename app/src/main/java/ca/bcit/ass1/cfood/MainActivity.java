package ca.bcit.ass1.cfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Results[] resultsArray = {
                new Results("Neighbourhood 1", "Description - noisy area", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 2", "Description - bad place to live", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 3", "Description - high density", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 4", "Description - far away from everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 5", "Description - close to everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 1", "Description - noisy area", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 2", "Description - bad place to live", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 3", "Description - high density", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 4", "Description - far away from everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 5", "Description - close to everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 1", "Description - noisy area", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 2", "Description - bad place to live", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 3", "Description - high density", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 4", "Description - far away from everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2"),
                new Results("Neighbourhood 5", "Description - close to everything", "Noise: 3, Parks: 3, Schools: 3, Shopping: 2")
        };

        ListView resultsList = new ListView(this);
        ArrayAdapter<Results> resultsAdapter = new ArrayAdapter<Results>(this,
                0, resultsArray) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    Results currentResult = resultsArray[position];
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
        setContentView(resultsList);
        resultsList.setAdapter(resultsAdapter);

    }
}
