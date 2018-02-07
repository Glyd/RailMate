package assignment.lewisd97.railmate.Models;

/**
 * Created by lewisd97 on 07/02/2018.
 */

public class Station {
    private String stationName;
    private float stationLatitude, stationLongitude;

    public Station(String stationName, float stationLatitude, float stationLongitude) {
        this.stationName = stationName;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public float getStationLatitude() {
        return stationLatitude;
    }

    public void setStationLatitude(float stationLatitude) {
        this.stationLatitude = stationLatitude;
    }

    public float getStationLongitude() {
        return stationLongitude;
    }

    public void setStationLongitude(float stationLongitude) {
        this.stationLongitude = stationLongitude;
    }
}
