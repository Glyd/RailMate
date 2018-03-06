package assignment.lewisd97.railmate.src.Helpers;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import assignment.lewisd97.railmate.src.Models.Station;

/**
 * Created by lewisd97 on 07/02/2018.
 */

public class HttpGetRequest extends AsyncTask<String, Void, ArrayList<Station>> {
    private String requestMethod = "GET";
    private int readTimeout = 15000;
    private int connectionTimeout = 15000;
    private URL url;

    public HttpGetRequest(URL url) {
        this.url = url;
    }

    @Override
    protected ArrayList<Station> doInBackground(String... params){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(requestMethod);
            connection.setReadTimeout(readTimeout);
            connection.setConnectTimeout(connectionTimeout);

            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            String line;

            ArrayList<Station> stationsArrayList = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                JSONArray stationsJsonArray = null;

                try {
                    stationsJsonArray = new JSONArray(line);

                    for (int i = 0; i < stationsJsonArray.length(); i++) {
                        JSONObject stationJsonObject = (JSONObject) stationsJsonArray.get(i);

                        Station station = new Station(
                                stationJsonObject.getString("StationName"),
                                stationJsonObject.getDouble("Latitude"),
                                stationJsonObject.getDouble("Longitude")
                        );

                        stationsArrayList.add(station);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return stationsArrayList;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}