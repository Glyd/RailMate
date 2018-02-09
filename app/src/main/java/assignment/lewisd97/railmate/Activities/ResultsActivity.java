package assignment.lewisd97.railmate.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import assignment.lewisd97.railmate.Helpers.FontsOverride;
import assignment.lewisd97.railmate.Helpers.StationAdapter;
import assignment.lewisd97.railmate.Models.Station;
import assignment.lewisd97.railmate.R;


/**
 * Created by lewisd97 on 07/02/2018.
 */

public class ResultsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        FontsOverride.setFonts(this);

        createListOfStations();
    }

    private void createListOfStations() {
        ArrayList<Station> stations = (ArrayList<Station>) getIntent().getSerializableExtra("Stations");
        double currentLat, currentLong;

        currentLat = getIntent().getDoubleExtra("Latitude", 0.000);
        currentLong = getIntent().getDoubleExtra("Longitude", 0.000);

        ListView lv = (ListView) findViewById(R.id.results_listview);
        lv.setAdapter(new StationAdapter(this, stations, currentLat, currentLong));
    }
}
