package assignment.lewisd97.railmate.src.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ArrayList;

import assignment.lewisd97.railmate.src.Helpers.FontsOverride;
import assignment.lewisd97.railmate.src.Helpers.HttpGetRequest;
import assignment.lewisd97.railmate.src.Helpers.MyFocusChangeListener;
import assignment.lewisd97.railmate.src.Models.Station;
import assignment.lewisd97.railmate.R;

public class SearchActivity extends Activity {

    EditText postcodeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        FontsOverride.setFont(this);

        postcodeET = findViewById(R.id.postcodeEntry);

        //hide keyboard when losing focus, better UX
        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener(this);
        postcodeET.setOnFocusChangeListener(ofcListener);
    }

    public void findPostcode(View v) {
        String postcode = postcodeET.getText().toString().replaceAll("\\s+","");

        if (isValidPostcode(postcode)) {
            try {
                Geocoder geocoder = new Geocoder(this);

                Address address = geocoder.getFromLocationName(postcode, 1).get(0);

                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                getStationsFromLatLng(new LatLng(latitude, longitude));
            } catch (Exception e) {
                Toast.makeText(this, "Please ensure postcode is valid.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Check postcode / internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public void findLocation(View v) {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

            getStationsFromLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

        } else {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    findLocation(findViewById(R.id.detectButton));
                } else {
                    Toast.makeText(this, "Location permission is needed for this feature.", Toast.LENGTH_SHORT).show();
                }

                return;
            }
        }
    }

    private void getStationsFromLatLng(final LatLng position) {
        if (hasInternetConnection()) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        ArrayList<Station> stationsArrayList;
                        URL url;

                        try {
                            url = new URL(createStationUrl(position));

                            HttpGetRequest getReq = new HttpGetRequest(url);
                            stationsArrayList = getReq.execute().get();

                            if (!stationsArrayList.isEmpty()) {
                                goToResults(stationsArrayList, position);
                            } else {
                                Toast.makeText(SearchActivity.this, "No stations found. Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();
        } else {
            Toast.makeText(this, "Check your internet connection / permission!", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToResults(ArrayList<Station> stations, LatLng position) {
        if (!stations.isEmpty()) {
            Intent resultsIntent = new Intent(this, ResultsActivity.class);
            resultsIntent.putParcelableArrayListExtra("Stations", stations);
            resultsIntent.putExtra("Latitude", position.latitude);
            resultsIntent.putExtra("Longitude", position.longitude);
            startActivity(resultsIntent);
        } else {
            Toast.makeText(this, "Sorry, something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private String createStationUrl (LatLng position) {
        String stationApiBaseUrl = "http://zebedee.kriswelsh.com:8080/stations";
        String baseLat = "?lat=";
        String baseLong = "&lng=";

        return stationApiBaseUrl + baseLat + position.latitude + baseLong + position.longitude;
    }

    private boolean hasInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean isValidPostcode(String postcode) {
        return (postcode.length() >= 5 && postcode.length() <= 7);
    }
}

interface Constants {
    int LOCATION_PERMISSION_REQUEST_CODE = 123;
}
