package assignment.lewisd97.railmate.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ArrayList;

import assignment.lewisd97.railmate.Helpers.FontsOverride;
import assignment.lewisd97.railmate.Helpers.HttpGetRequest;
import assignment.lewisd97.railmate.Models.Station;
import assignment.lewisd97.railmate.R;

public class SearchActivity extends Activity {

    String postcodeApiBaseUrl = "https://api.postcodes.io/postcodes/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        FontsOverride.setFonts(this);
    }

    public void findPostcode(View v) {
        EditText postcodeET = (EditText) findViewById(R.id.postcodeEntry);
        String postcode = postcodeET.getText().toString().replaceAll("\\s+","");

        if (isValidPostcode(postcode) && hasInternetConnection()) {
            try {
                URL url = new URL(postcodeApiBaseUrl + postcode);
                HttpGetRequest getReq = new HttpGetRequest(url);
                BufferedReader bufferedReader = getReq.execute().get();

                JSONObject postcodeJson = new JSONObject(bufferedReader.readLine());

                double latitude = postcodeJson.getJSONObject("result").getDouble("latitude");
                double longitude = postcodeJson.getJSONObject("result").getDouble("longitude");

                getStationsFromLatLong(latitude, longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Check postcode / internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public void findLocation(View v) {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

            getStationsFromLatLong(location.getLatitude(), location.getLongitude());

        } else {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    findLocation(findViewById(R.id.button2));
                } else {
                    Toast.makeText(this, "Location permission is needed for this feature.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void getStationsFromLatLong(final double latitude, final double longitude) {
        if (hasInternetConnection()) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        ArrayList<Station> stationsArrayList = new ArrayList<>();
                        URL url;
                        try {
                            url = new URL(createStationUrl(latitude, longitude));

                            HttpGetRequest getReq = new HttpGetRequest(url);
                            BufferedReader bufferedReader = getReq.execute().get();

                            String line;

                            while ((line = bufferedReader.readLine()) != null) {
                                JSONArray stationsJsonArray = new JSONArray(line);
                                for (int i = 0; i < stationsJsonArray.length(); i++) {
                                    JSONObject stationJsonObject = (JSONObject) stationsJsonArray.get(i);

                                    Station station = new Station(
                                            stationJsonObject.getString("StationName"),
                                            stationJsonObject.getDouble("Latitude"),
                                            stationJsonObject.getDouble("Longitude")
                                    );

                                    stationsArrayList.add(station);
                                }
                            }

                            if (!stationsArrayList.isEmpty()) {
                                goToResults(stationsArrayList, latitude, longitude);
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

    private void goToResults(ArrayList<Station> stations, double latitude, double longitude) {
        if (!stations.isEmpty()) {
            Intent resultsIntent = new Intent(this, ResultsActivity.class);
            resultsIntent.putParcelableArrayListExtra("Stations", stations);
            resultsIntent.putExtra("Latitude", latitude);
            resultsIntent.putExtra("Longitude", longitude);
            startActivity(resultsIntent);
        } else {
            Toast.makeText(this, "Sorry, something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private String createStationUrl (double latitude, double longitude) {
        String stationApiBaseUrl = "http://zebedee.kriswelsh.com:8080/stations";
        String baseLat = "?lat=";
        String baseLong = "&lng=";

        return stationApiBaseUrl + baseLat + latitude + baseLong + longitude;
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
