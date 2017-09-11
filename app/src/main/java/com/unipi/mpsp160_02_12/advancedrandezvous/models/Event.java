package com.unipi.mpsp160_02_12.advancedrandezvous.models;

import android.location.Location;

import java.util.Date;

/**
 * Created by Dimitris on 11/9/2017.
 */

public class Event {
    String title;
    Location location;
    String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
