package assignment.lewisd97.railmate.src.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lewisd97 on 07/02/2018.
 */

public class Station implements Parcelable {
    private String stationName;
    private double stationLatitude, stationLongitude;

    public Station(String stationName, double stationLatitude, double stationLongitude) {
        this.stationName = stationName;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
    }

    public String getStationName() {
        return stationName;
    }

    public double getStationLatitude() {
        return stationLatitude;
    }

    public double getStationLongitude() {
        return stationLongitude;
    }

    /** Make this parcelable so we can pass it in Intents */

    protected Station(Parcel in) {
        stationName = in.readString();
        stationLatitude = in.readDouble();
        stationLongitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stationName);
        dest.writeDouble(stationLatitude);
        dest.writeDouble(stationLongitude);
    }

    public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}
