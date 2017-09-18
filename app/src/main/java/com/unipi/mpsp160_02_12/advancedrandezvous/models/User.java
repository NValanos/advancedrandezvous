package com.unipi.mpsp160_02_12.advancedrandezvous.models;

/**
 * Created by Dimitris on 17/9/2017.
 */

public class User {

    private String username;
    private String email;

    public User(String aUsername, String anEmail){
        username = aUsername;
        email = anEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
