package com.gaparmar.mediaflashback.DataStorage;

/**
 * Created by gauravparmar on 3/10/18.
 */

/**
 * A subclass to encapsulate the data to be logged
 */
public class LogInstance{
    public String title;
    public String album;
    public String artist;
    public String locationPlayed;
    public String userName;
    public String proxy;
    public String Id;
    public String dayOfWeek;
    public long timestamp;
    public int timeOfDay;
    public double latitude;
    public double longitude;
    public String url;
    LogInstance(String title, String album, String artist, String locationPlayed, String userName,
                String proxy, String Id, String dayOfWeek, long timestamp,
                int timeOfDay, double latitude, double longitude, String url){
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.locationPlayed = locationPlayed;
        this.userName = userName;
        this.proxy = proxy;
        this.Id = Id;
        this.dayOfWeek = dayOfWeek;
        this.timestamp = timestamp;
        this.timeOfDay = timeOfDay;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
    }
}