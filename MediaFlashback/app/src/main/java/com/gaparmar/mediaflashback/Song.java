package com.gaparmar.mediaflashback;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by gauravparmar on 2/2/18.
 */

// -1, NA, represents uninitialized value
public class Song {

    public enum state {
        LIKED, DISLIKED, NEITHER
    }
    // Declaring the member variables of the com.gaparmar.mediaflashback.Song object

    //TODO:
    // mp3 file
    // cover art
    private final int LATITUDE = 0;  // constants representing location[] index
    private final int LONGITUDE = 1;
    private final int THRESHOLD = 1000;
    private final String UNKNOW = "Unknown";

    private final int MORNING = 0;
    private final int AFTERNOON = 1;
    private final int EVENING = 2;

    private String title;
    private String parentAlbum;
    private int currentState;
    private double[] location;    // [Latitude, Longitude] stored as double[]
    private String artistName;
    private int resID;
    private int lengthInSeconds;
    private int yearOfRelease;
    private String dayOfWeek;
    private int timeLastPlayed; // TODO:: temporary way of storing the time stamp
    private int probability;
    private String currDay;
    private double[] currLocation;
    private int currTime;
    private long fullTimeStamp;
    private String fullTimeStampString;

    /**
     *  the default constructor
     *  Initializes all members to default values
     */
    public Song(){
        super();
        title = "";
        parentAlbum = "NA";
        currentState = 0;
        location = new double[2];
        location[LATITUDE] = 0.0;
        location[LONGITUDE] = 0.0;
        artistName = "";
        resID = 0;
        lengthInSeconds = -1;
        yearOfRelease = -1;
        timeLastPlayed = -1;
        probability = 1;
        dayOfWeek = UNKNOW;
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
        if(location != null) {
            this.location = location;
        }else{
            location = new double[]{0.0, 0.0};
        }

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
     * @param context the current activity that the song lives on
     * @return whether the song is LIKED/DISLIKED/NEUTRAl
     */
    public int getCurrentState(Context context){
        return StorageHandler.getSongState(context, this.getResID());
    }

    /**
     * Updates the current state of the Song
     * @param currentState the new state of the Song to be updated to
     */
    public void setCurrentState(int currentState){
        this.currentState = currentState;
    }

    /**
     * Retrieves the Currently Stored Location in the Song
     * @return the location coords
     */
    public double[] getLocation(Context context){
        return StorageHandler.getSongLocation(context, this.resID);
    }

    /**
     * Get the location whre the song is lastly played in readable String
     * @param context the current context the song lives on
     * @return the string that presents the location
     */
    public String getLocationString(Context context){
        double songLat = getLocation(context)[0];
        double songLong = getLocation(context)[1];
        if(songLat == -1 || songLong == -1){
            return "None";
        }
        return UserLocation.getCity(songLat, songLong) + ", " +
                UserLocation.getState(songLat, songLong);
    }

    /**
     * Sets the Location in the Song
     * @param location
     */
    public void setLocation(double[] location, Context context){
        StorageHandler.storeSongLocation(context, this.resID, location);
        this.location = StorageHandler.getSongLocation(context, this.resID);
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
     * @return the time the song
     */
    public int getTimeLastPlayed(){
        return this.timeLastPlayed;
    }

    /**
     *
     * @param timeLastPlayed
     */
    public void setTimeLastPlayed(int timeLastPlayed){
        this.timeLastPlayed = timeLastPlayed;
    }

    public void setResID(int rawID) { this.resID = rawID; }

    public int getResID() { return this.resID; }

    public void setDayOfWeek(String dayOfWeek)
    {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfWeek()
    {
        return dayOfWeek;
    }

    public long getFullTimeStamp() {
        return fullTimeStamp;
    }

    public String getFullTimeStampString() {
        return fullTimeStampString;
    }


    /**
     * Get the day of the week
     * @param value the number of the day of the week, 1-monday so on
     * @return the string value of that day
     */
    private String getDayOfWeek(int value)
    {
        String day = "";
        switch(value){
            case 1:
                day="Sunday";
                break;
            case 2:
                day="Monday";
                break;
            case 3:
                day="Tuesday";
                break;
            case 4:
                day="Wednesday";
                break;
            case 5:
                day="Thursday";
                break;
            case 6:
                day="Friday";
                break;
            case 7:
                day="Saturday";
                break;
        }
        return day;
    }

    /**
     * Update probabilily of a song being played based on the location and time it was last played
     * @param currLocation the current location of the user
     * @param context the current activity the song lives on
     */
    public void updateProbability(double[] currLocation, Context context)
    {
        int prob = 0;
        this.probability = 1;
        currDay= getDayOfWeek(Calendar.DAY_OF_WEEK);
        if(isWithinRange(currLocation, 1000)) // TODO : pass in users current location
        {
            prob++;
        }
        if(isSameDay(currDay, dayOfWeek))
        {
            prob++;
        }
        if(isSameTime(currTime, timeLastPlayed))
        {
            prob++;
        }
        if(getCurrentState(context) == 1)
        {
            prob++;
        }
        if(getCurrentState(context) == -1)
        {
            prob = 0;
            this.probability = 0;
        }
        this.probability += prob;

        System.out.println("Song title: "+ getTitle() + " probability: " + this.probability);
    }

    public int getProbability(){
        return this.probability;
    }

    public void setProbability(int x)
    {
        probability = x;
    }

    public void setCurrDay(String currDay)
    {
        this.currDay = currDay;
    }

    public void setCurrLocation(double[] currLocation)
    {
        this.currLocation = currLocation;
    }

    public void setCurrTime(int currTime)
    {
        this.currTime = currTime;
    }

    public void setFullTimeStamp( long timeStamp ) {
        this.fullTimeStamp = timeStamp;
    }

    public void setFullTimeStampString( String timeStampString ) {
        this.fullTimeStampString = timeStampString;
    }

    /**
     * Calculate if the song is with in a certain threshold of where it was last played
     * @param currLocation the current location of the user
     * @param threshold the range we want to set - within that range, we say the song is close enough
     * @return true if the song is close enough, false otherwise
     */
    public boolean isWithinRange(double[] currLocation, int threshold)
    {
        //TODO:: create method to determine if the current location is in the same range as the last played location
        // 1000 ft
        return calculateDist(currLocation, this.location) <= THRESHOLD;
    }

    public boolean isSameDay(String currDay, String day)
    {
        //TODO:: create method to determine if the current day is the same as last played day

        if(day == null || currDay == null || day.isEmpty()){
            return false;
        }
        return currDay.equals(day);
    }

    public boolean isSameTime(int currTime, int time)
    {
        //TODO:: create method to determine if the current time interval is the same as last played time interval
        return getTimeZone(currTime) == getTimeZone(time);
    }

    /**
     * Return if in the morning, afternoon, or evening
     * @param time in hours
     * @return 0 if morning, 1 if afternoon, 2 if evening
     */
    public int getTimeZone(int time) {
        if (time >= 5 && time < 11) {
            // 5 AM - 11 AM
            return MORNING;
        } else if (time >= 11 && time < 17) {
            // 11 AM - 5 PM
            return AFTERNOON;
        } else {
            // 5 PM - 5 AM
            return EVENING;
        }
    }

    /**
     * Uses the haversine formula to calculate distance between GPS coordinates
     * @param loc1 the current location user is at
     * @param loc2 the location where the song is last played
     * @return the distance between two location
     */
    public static double calculateDist(double[] loc1, double[] loc2)
    {

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
     * helper function that converts degree to radiant
     * @param degrees the degree we are trying to convert
     * @return the radiant converted from parameter degree
     */
    private static double toRadians(double degrees){
        return degrees * Math.PI / 180;
    }

}
