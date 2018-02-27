package assignment.lewisd97.railmate.src.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import assignment.lewisd97.railmate.R;
import assignment.lewisd97.railmate.src.Activities.ResultsActivity;
import assignment.lewisd97.railmate.src.Models.Station;

/**
 * Created by lewisd97 on 09/02/2018.
 */

public class MapBuilder extends Activity implements OnMapReadyCallback {
    // Include the OnCreate() method here too, as described above.
    ArrayList<Station> stationArrayList;
    LatLng currentLocation;
    Activity callingActivity;

    public MapBuilder(ArrayList<Station> stationArrayList, LatLng currentLocation, Activity callingActivity) {
        this.stationArrayList = stationArrayList;
        this.currentLocation = currentLocation;
        this.callingActivity = callingActivity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        /**Add markers to map */

        ArrayList<LatLng> latLngArrayList = new ArrayList<>();
        ArrayList<String> stationNameArrayList = new ArrayList<>();

         for (Station station : stationArrayList) {
            LatLng latLng = new LatLng(station.getStationLatitude(), station.getStationLongitude());
            String stationName = station.getStationName();

            latLngArrayList.add(latLng);
            stationNameArrayList.add(stationName);
        }

        for (int i = 0; i < latLngArrayList.size(); i++) {
            googleMap.addMarker(new MarkerOptions().position(latLngArrayList.get(i))
                    .title(stationNameArrayList.get(i)));
        }

        /**Custom theme for map */

        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(callingActivity, R.raw.custom_map_style));
        } catch (Resources.NotFoundException e) {
            Log.d("error", "Couldn't find map styling");
        }

        /** Set up camera position and zoom */
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);

        googleMap.animateCamera(zoom);

        /**Enable showing of current location */

        if (callingActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            return;
        } else {
            Toast.makeText(this, "Unable to show current location.", Toast.LENGTH_SHORT).show();
        }
    }
}
