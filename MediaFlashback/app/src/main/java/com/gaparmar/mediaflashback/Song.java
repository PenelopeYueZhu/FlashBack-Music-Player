package com.gaparmar.mediaflashback;

import android.content.Context;

/**
 * Created by gauravparmar on 2/2/18.
 */

// -1, NA, represents uninitialized value
public class Song {

    enum state {
        LIKED, DISLIKED, NEITHER
    }

    // Declaring the member variables of the com.gaparmar.mediaflashback.Song object

    //TODO:
    // mp3 file
    // cover art
    private final int LATITUDE = 0;  // constants representing location[] index
    private final int LONGITUDE = 1;
    private String title;
    private String parentAlbum;
    private state currentState;
    private double[] location;    // [Latitude, Longitude] stored as double[]
    private String artistName;
    private int resID;
    private int lengthInSeconds;
    private int yearOfRelease;
    private int timeLastPlayed; // TODO:: temporary way of storing the time stamp
    private int probability; // TODO:: not yet implemented?


    /**
     *  the default constructor
     *  Initializes all members to default values
     */
    public Song(){
        super();
        title = "";
        parentAlbum = "NA";
        currentState = state.NEITHER;
        location = new double[2];
        location[LATITUDE] = 0.0;
        location[LONGITUDE] = 0.0;
        artistName = "";
        resID = 0;
        lengthInSeconds = -1;
        yearOfRelease = -1;
        timeLastPlayed = -1;
        probability = 1;
    }

    /**
     * The constructor that initializes the Song object based on the parameters
     * @param title The title of the Song
     * @param parentAlbum The title of the Song Album
     * @param artistName The name of the Artist
     * @param lengthInSeconds The length of the Song is seconds
     * @param yearOfRelease The release year of the Song
     * @param resID The Resource ID of the song
     * @param location The last played Location coords of the Song
     */
    public Song(String title, String parentAlbum,
                String artistName, int lengthInSeconds,
                int yearOfRelease, int resID, double[] location){
        this();
        this.title = title;
        this.parentAlbum = parentAlbum;
        this.lengthInSeconds = lengthInSeconds;
        this.artistName = artistName;
        this.yearOfRelease = yearOfRelease;
        this.resID = resID;
        this.probability = 1;
        this.location = location;
    }

    /**
     * @return Retrieves the Title of the Song object
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * Sets the Title of the Song
     * @param title the new title
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Retrieves the name of the parent Album
     * @return parent Album's name
     */
    public String getParentAlbum(){
        return this.parentAlbum;
    }

    /**
     * Updates the name of the Parent album
     * @param parentAlbum new parent album name
     */
    public void setParentAlbum(String parentAlbum){
        this.parentAlbum = parentAlbum;
    }

    /**
     * Retrives the current state of the Song
     * @param context the context referring to the calling activity
     * @return whether the song is LIKED/DISLIKED/NEUTRAl
     */
    public state getCurrentState(Context context){
        return StorageHandler.getSongState(context, this.getResID());
    }

    /**
     * Updates the current state of the Song
     * @param currentState the new state of the Song to be updated to
     */
    public void setCurrentState(state currentState){
        this.currentState = currentState;
    }

    /**
     * Retrieves the Currently Stored Location in the Song
     * @param context the context referring to the calling activity
     * @return the location coords
     */
    public double[] getLocation(Context context){
        return StorageHandler.getSongLocation(context, this.getResID());
    }

    /**
     * Sets the Location in the Song
     * @param location
     */
    public void setLocation(double[] location){
        this.location = location;
    }

    /**
     * Retrieves the Name of the Artist
     * @return Song's Artist name
     */
    public String getArtistName(){
        return this.artistName;
    }

    /**
     * Updates the Song's Artist name
     * @param artistName The new Artist name
     */
    public void setArtistName(String artistName){
        this.artistName = artistName;
    }

    /**
     * Retrieves the length of the Song in Seconds
     * @return the length of the song
     */
    public int getLengthInSeconds(){
        return this.lengthInSeconds;
    }

    /**
     * Updates the length of the Song in the seconds
     * @param lengthInSeconds the new length of the Song
     */
    public void setLengthInSeconds(int lengthInSeconds){
        this.lengthInSeconds = lengthInSeconds;
    }

    /**
     * Retrieves the Song's year of release
     * @return year of release
     */
    public int getYearOfRelease(){
        return this.yearOfRelease;
    }

    /**
     * Updates the Song object's release year
     * @param yearOfRelease the new year of release
     */
    public void setYearOfRelease(int yearOfRelease){
        this.yearOfRelease = yearOfRelease;
    }

    /**
     * Retrieves the last played time of the Song
     * @param context the context referring to the calling activity
     * @return the time the song
     */
    public int getTimeLastPlayed(Context context){
        return StorageHandler.getSongTime(context, this.getResID());
    }

    /**
     * Updates the resource id of the Song
     * @param rawID the new resource ID
     */
    public void setResID(int rawID) { this.resID = rawID; }

    /**
     * Retrieves the resource ID of the Song
     * @return the res ID of the Song
     */
    public int getResID() { return this.resID; }

    /**
     * Calculates and updates the probability of song being played
     * @param context the context of the calling activity
     */
    public void updateProbability(Context context){
        int prob = 0;
        // TODO : pass in users current location
        if(isWithinRange(new double[2], context, 1000)){
            prob++;
        }
        if(isSameDay()){
            prob++;
        }
        if(isSameTime()){
            prob++;
        }
        if(getCurrentState(context) == state.LIKED){
            prob++;
        }
        if(getCurrentState(context) == state.DISLIKED){
            prob = 0;
            this.probability = 0;
        }
        this.probability += prob;
    }

    /**
     * Retrieves the currently stored probability of the Song
     * @return the probaility of the song being played
     */
    public int getProbability(){
        return this.probability;
    }

    /**
     * Updates the Probability of the Song being played in the flashback mode
     * @param x the number representing the probability
     */
    public void setProbability(int x)
    {
        probability = x;
    }

    /**
     * Checks if the current location is within range
     * @param currLocation The coords of the current location
     * @param context The context of the calling Activity
     * @param threshold The numeric threshold for the range calculation
     * @return True if the current location is in range
     */
    public boolean isWithinRange(double[] currLocation, Context context, int threshold) {
        //TODO:: create method to determine if the current location is in the same range as the last played location
        return calculateDist(currLocation, getLocation(context)) <= threshold;
    }

    /**
     * Checks if the current day is the same day
     * @return if the current day is the same day as the last played song
     */
    public boolean isSameDay() {
        //TODO:: create method to determine if the current day is the same as last played day
        return false;
    }

    /**
     * Checks if the current time is same as the last played time
     * @return True if it is the same time
     */
    public boolean isSameTime() {
        //TODO:: create method to determine if the current time interval is the same as last played time interval
        return false;
    }

    /**
     * Calculates the distance between two locations using Haversine formula
     * @param loc1 The first gps location coordinate
     * @param loc2 The second gps location coordinate
     * @return The distance between the give coordinates
     */
    public static double calculateDist(double[] loc1, double[] loc2){
        int LATITUDE = 0;
        int LONGITUDE = 1;

        // Setting up the variables
        double lat1 = loc1[LATITUDE];
        double lon1 = loc1[LONGITUDE];
        double lat2 = loc2[LATITUDE];
        double lon2 = loc2[LONGITUDE];

        double lat1R = toRadians(lat1);
        double lat2R = toRadians(lat2);

        double deltaLat = toRadians(lat2 - lat1);
        double deltaLon = toRadians(lon2 - lon1);

        // Raidus of earth in feet
        double radius = 3959 * 5280;

        // a = sin^2(deltaLat/2) + cos(lat1R) * cos(lat2R) * sin^2(deltaLon/2)
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1R) * Math.cos(lat2R) * Math.sin(deltaLon / 2)
                * Math.sin(deltaLon / 2);

        // c = 2 * atan2(sqrt(a) * sqrt(1-a))
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt((1-a)));

        // d = radius * c
        double distance = radius * c;
        return distance;

    }

    /**
     * Utility method that converts angle in degrees to radians
     * @param degrees Angle in degrees
     * @return Corresponding angle in radians
     */
    public static double toRadians(double degrees){
        return degrees * Math.PI / 180;
    }

}
