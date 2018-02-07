package assignment.lewisd97.railmate;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ArrayList;

import assignment.lewisd97.railmate.Helpers.HttpGetRequest;
import assignment.lewisd97.railmate.Models.Station;

public class SearchActivity extends Activity {

    String postcodeApiBaseUrl = "https://api.postcodes.io/postcodes/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findPostcode();
    }

    private void findPostcode() {
        String postcode = "m32 0hr".replaceAll("\\s+",""); //get text from edittext
        if (isValidPostcode(postcode) && hasInternetConnection()) {
            try {
                URL url = new URL(postcodeApiBaseUrl + postcode);
                HttpGetRequest getReq = new HttpGetRequest(url);
                BufferedReader bufferedReader = getReq.execute().get();

                JSONObject postcodeJson = new JSONObject(bufferedReader.readLine());

                double latitude = (double) postcodeJson.getJSONObject("result").get("latitude");
                double longitude = (double) postcodeJson.getJSONObject("result").get("longitude");

                getStationsFromLatLong(latitude, longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Check postcode / internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void findLocation(View v) {
        //todo

        //get Location object from LocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        //get lat/long from Location object (getLatitude / getLongitude from Location object)
        //getstationsfromlatlong
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
                                            stationJsonObject.getInt("Latitude"),
                                            stationJsonObject.getInt("Longitude")
                                    );

                                    stationsArrayList.add(station);
                                }
                            }

                            if (!stationsArrayList.isEmpty()) {
                                goToResults(stationsArrayList);
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

    private void goToResults(ArrayList<Station> stations) {
        Log.d("blep", stations.toString());
        //pass stations to ResultActivity when made
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
