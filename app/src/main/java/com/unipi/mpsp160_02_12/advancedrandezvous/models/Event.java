package com.unipi.mpsp160_02_12.advancedrandezvous.models;


/**
 * Created by Dimitris on 11/9/2017.
 */

public class Event {
    String title;
    LatLong location;
    long date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLong getLocation() {
        return location;
    }

    public void setLocation(LatLong location) {
        this.location = location;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
