package com.unipi.mpsp160_02_12.advancedrandezvous.models;


import java.util.List;

/**
 * Created by Dimitris on 11/9/2017.
 */

public class Event {
    String id;
    String ownerId;
    List<Participant> participantsIdList;
    boolean active;
    String title;
    LatLong location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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

    public void setParticipantsIdList(List<Participant> participantsIdList) {
        this.participantsIdList = participantsIdList;
    }

    public List<Participant> getParticipantsIdList() {
        return participantsIdList;
    }
}
