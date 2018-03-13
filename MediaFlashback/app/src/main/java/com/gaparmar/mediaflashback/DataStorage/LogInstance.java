package com.gaparmar.mediaflashback.DataStorage;

/**
 * Created by gauravparmar on 3/10/18.
 */

/**
 * A subclass to encapsulate the data to be logged
 */
public class LogInstance{
    public String locationPlayed;
    public String userName;
    public String dayOfWeek;
    public long timestamp;
    public int timeOfDay;
    public double latitude;
    public double longitude;
    LogInstance(String locationPlayed, String userName, String dayOfWeek, long timestamp,
                int timeOfDay, double latitude, double longitude){
        this.locationPlayed = locationPlayed;
        this.userName = userName;
        this.dayOfWeek = dayOfWeek;
        this.timestamp = timestamp;
        this.timeOfDay = timeOfDay;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}