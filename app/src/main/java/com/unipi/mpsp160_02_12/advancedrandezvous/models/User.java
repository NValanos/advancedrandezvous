package com.unipi.mpsp160_02_12.advancedrandezvous.models;

/**
 * Created by Dimitris on 17/9/2017.
 */

public class User {

    private String username;
    private String email;
    private String id;
    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public User(){

    }

    public User(String aUsername, String anEmail, String anId){
        username = aUsername;
        email = anEmail;
        id = anId;
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

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
