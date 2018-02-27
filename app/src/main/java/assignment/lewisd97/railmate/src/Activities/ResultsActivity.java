package assignment.lewisd97.railmate.src.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import assignment.lewisd97.railmate.src.Helpers.FontsOverride;
import assignment.lewisd97.railmate.src.Helpers.MapBuilder;
import assignment.lewisd97.railmate.src.Helpers.StationAdapter;
import assignment.lewisd97.railmate.src.Models.Station;
import assignment.lewisd97.railmate.R;


/**
 * Created by lewisd97 on 07/02/2018.
 */

public class ResultsActivity extends Activity {
    double currentLat, currentLong;
    ArrayList<Station> stationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        FontsOverride.setFont(this);

        currentLat = getIntent().getDoubleExtra("Latitude", 0.000);
        currentLong = getIntent().getDoubleExtra("Longitude", 0.000);

        stationArrayList = (ArrayList<Station>) getIntent().getSerializableExtra("Stations");

        setUpMap();
        createListOfStations();
    }

    private void setUpMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        MapBuilder mapBuilder = new MapBuilder(stationArrayList, new LatLng(currentLat, currentLong), this);
        mapFragment.getMapAsync(mapBuilder);
    }

    private void createListOfStations() {
        ListView lv = findViewById(R.id.results_listview);
        lv.setAdapter(new StationAdapter(this, stationArrayList, currentLat, currentLong));
    }
}
