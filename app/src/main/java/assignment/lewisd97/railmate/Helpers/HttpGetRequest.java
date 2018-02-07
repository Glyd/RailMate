package assignment.lewisd97.railmate.Helpers;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lewisd97 on 07/02/2018.
 */

public class HttpGetRequest extends AsyncTask<String, Void, BufferedReader> {
    private String requestMethod = "GET";
    private int readTimeout = 15000;
    private int connectionTimeout = 15000;
    private URL url;

    public HttpGetRequest(URL url) {
        this.url = url;
    }

    @Override
    protected BufferedReader doInBackground(String... params){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(requestMethod);
            connection.setReadTimeout(readTimeout);
            connection.setConnectTimeout(connectionTimeout);

            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);

            return reader;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
    protected void onPostExecute(BufferedReader reader){
        super.onPostExecute(reader);
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}