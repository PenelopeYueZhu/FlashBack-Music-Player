package com.gaparmar.mediaflashback;

/**
 * Created by lxyzh on 3/4/2018.
 */

public class SongString {
    String id;
    String lat;
    String lon;

    public SongString(){}

    public SongString(String lat, String lon, String id){
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }

    public String getLat(){
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getId() {
        return id;
    }
}
