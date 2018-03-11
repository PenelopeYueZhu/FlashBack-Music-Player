package com.gaparmar.mediaflashback.DataStorage;

/**
 * Created by gauravparmar on 3/10/18.
 */

/**
 * A subclass to encapsulate the data to be logged
 */
public class LogInstance{
    String locationPlayed;
    String userName;
    int timestamp;
    double latitude;
    double longitude;
    LogInstance(String locationPlayed, String userName, int timestamp, double latitude, double longitude){
        this.locationPlayed = locationPlayed;
        this.userName = userName;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}