package assignment.lewisd97.railmate.Helpers;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import assignment.lewisd97.railmate.Models.Station;
import assignment.lewisd97.railmate.R;

/**
 * Created by lewisd97 on 08/02/2018.
 */

public class StationAdapter extends BaseAdapter {

    ArrayList<Station> stations;
    double currentLat, currentLong;

    Context context;

    //constructor
    public StationAdapter(Context context, ArrayList<Station> stations, double currentLat, double currentLong) {
        this.context = context;
        this.stations = stations;
        this.currentLat = currentLat;
        this.currentLong = currentLong;
    }

    public int getCount() {
        return stations.size();
    }

    public Object getItem(int arg0) {
        //implement something if ever needed, for now there's no need
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View arg1, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.results_listview, viewGroup, false);
        Station station = stations.get(position);

        TextView stationDistanceText = (TextView) row.findViewById(R.id.stationDistance);
        TextView stationNameText = (TextView) row.findViewById(R.id.stationName);

        float results[] = new float[3];

        Location.distanceBetween(
                currentLat,
                currentLong,
                station.getStationLatitude(),
                station.getStationLongitude(),
                results);


        // Distance is given in meters. There are 1609.34 meters in a mile, so divide by that to show the result in miles.
        // BigDecimal and setScale are used to display 2 decimals places instead of the full double value.
        String stationDistance = String.valueOf(new BigDecimal((results[0]/1609.34)).setScale(2, RoundingMode.HALF_UP).doubleValue()) + "mi";

        stationDistanceText.setText(stationDistance);
        stationNameText.setText(station.getStationName());

        return row;
    }
}