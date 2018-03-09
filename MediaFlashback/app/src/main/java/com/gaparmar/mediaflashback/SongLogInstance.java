package com.gaparmar.mediaflashback;

/**
 * Created by gauravparmar on 3/8/18.
 *
 * Encapsulates the data needed to log information for 1 song completion event
 */
public class SongLogInstance {
    private String locationPlayed;
    private String userName;
    private int timestamp;
    private float latitude;
    private float longitude;

    /**
     * @return the place this log was generated
     */
    public String getLocationPlayed() {
        return locationPlayed;
    }

    /**
     * @param locationPlayed the place this log was generated
     */
    public void setLocationPlayed(String locationPlayed) {
        this.locationPlayed = locationPlayed;
    }

    /**
     * @return the name of the user that generated this log
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the name of the user that generated this log
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the UNIX timestamp of when this log was generated
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the UNIX timestamp of when this log was generated
     */
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return The latitude of location where this log was generated
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude of the location where this log was generated
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude of where this log was generated
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude of where this log was generated
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public SongLogInstance(String locationPlayed, String userName, int timestamp, float latitude, float longitude) {
        this.locationPlayed = locationPlayed;
        this.userName = userName;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
