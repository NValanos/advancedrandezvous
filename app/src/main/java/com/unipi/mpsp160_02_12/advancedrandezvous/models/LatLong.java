package com.unipi.mpsp160_02_12.advancedrandezvous.models;

/**
 * Created by Dimitris on 14/9/2017.
 */

public class LatLong {

    private double latitude;
    private double longitude;

    public LatLong(){}

    public LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
