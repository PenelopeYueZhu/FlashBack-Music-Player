package com.gaparmar.mediaflashback;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Math.toRadians;

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
   /* private final int LATITUDE = 0;  // constants representing location[] index
    private final int LONGITUDE = 1;
    private final int THRESHOLD = 1000;
    private final String UNKNOW = "Unknown";

    private final int MORNING = 0;
    private final int AFTERNOON = 1;
    private final int EVENING = 2;*/

    private int resID;

    // MetaData field
    private String title;
    private String parentAlbum;
    private String artistName;
    private int yearOfRelease;

    // Play data
    private int rate;
   // private double[] location;    // [Latitude, Longitude] stored as double[]
   // private int lengthInSeconds;
    private String dayOfWeek;
    private String address;
    private String userName;
 //   private int timeLastPlayed; // TODO:: temporary way of storing the time stamp
    private int probability;
   // private String currDay;
 //   private double[] currLocation;
    private int time;
    private long timeStamp;
 //   private String fullTimeStampString;
    private double lat, lon;

    /**
     *  the default constructor
     *  Initializes all members to default values
     */
    public Song(){
        super();
        title = "";
        parentAlbum = "NA";
        rate = 0;
      //  location = new double[2];
       // lat = location[LATITUDE] = 0.0;
       // lon = location[LONGITUDE] = 0.0;
        lat = 0.0;
        lon = 0.0;
        artistName = "";
        resID = 0;
        //lengthInSeconds = -1;
        yearOfRelease = -1;
        //timeLastPlayed = -1;
        probability = 1;
        dayOfWeek = Constant.UNKNOWN;
        time = -1;
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
       // this.lengthInSeconds = lengthInSeconds;
        this.artistName = artistName;
        this.yearOfRelease = yearOfRelease;
        this.resID = resID;
        this.probability = 1;
        if(location != null) {
            //this.location = location;
            this.lat = location[0];
            this.lon = location[1];
        }else{
            //location = new double[]{0.0, 0.0};
            this.lat = 0.0;
            this.lon = 0.0;
        }

    }

   public Song( int id ){
       this.resID = id;
       dayOfWeek = Constant.UNKNOWN;
       time = -1;
       probability = 1;
       lat = 0.0;
       lon = 0.0;
       rate = 0;
       timeStamp = 0;
       address = Constant.UNKNOWN;
       userName = Constant.UNKNOWN;
   }

   /********************************* Setters ***********************************************/
    /**
     * Set the metadata of a song
     * @param title title of the song
     * @param album album of the song
     * @param artist artist of the song
     * @param year year of released
     */
    protected void setMetadata( String title, String album, String artist, String year ){
        this.title = title;
        this.parentAlbum = album;
        this.artistName = artist;
        this.yearOfRelease = Integer.parseInt(year);
    }

    /**
     * Set the coordinates where a song is lastly played
     * @param location the coordinates array
     */
    public void setLocation(double[] location)
    {
        this.lat = location[0];
        this.lon = location[1];
    }

    /**
     * Set the rate of the song
     * @param rate - 1 like
     *             - 0 neural
     *             - -1 dislike
     */
    public void setState(int rate)
    {
        this.rate = rate;
    }

    /**
     * Set the address string where the song is lastly played
     * @param address the string
     */
    protected void setAddress( String address ){
        this.address = address;
    }

    /**
     * Set the day of the week this song is lastly played
     * @param dayOfWeek the day of the week represented in string, "Monday", "Tuesday" etc
     */
    protected void setDayOfWeek( String dayOfWeek ){
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Set the full timestamp of the song
     * @param timeStamp the int timeStamp
     */
    protected void setTimeStamp( long timeStamp ){
        this.timeStamp = timeStamp;
    }

    /**
     * Set the username who most recently played the song
     * @param userName the username
     */
    protected  void setUserName( String userName ){
        this.userName = userName;
    }

    /**
     * Set the time of the day when a song was played
     * @param time the time of the day when the song is played
     */
    protected void setTime( int time ){
        this.time = time;
    }

    /**
     * Set the probability of a song being played
     * @param prob  the probability of the song
     */
    protected void setProbability( int prob ) {this.probability = prob;}



    /************************************** Getters for UI display *******************************/
    public int getResID() { return this.resID; }

    // Getters for UI display

    /**
     * @return Retrieves the Title of the Song object
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * Retrieves the Name of the Artist
     * @return Song's Artist name
     */
    public String getArtistName(){
        return this.artistName;
    }

    /**
     * Retrieves the name of the parent Album
     * @return parent Album's name
     */
    public String getParentAlbum(){
        return this.parentAlbum;
    }


    /************************************** Getters for Probability Checking *******************************/

    /**
     * Get the current rating of the song
     * @return rate - 0 neutral
     *              - 1 liked
     *              - -1 disliked
     */
    public int getRate(){
        return rate;
    }

    /**
     * Gete the time stamp
     * @return the full time stamp in seconds
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Get the time of the day
     * @return the time of the day when the song was lastly played
     */
    public int getTime() { return time;}

    /**
     * Return the day of week the song is lately played
     * @return day string
     */
    public String getDayOfWeek()
    {
        return dayOfWeek;
    }

    /**
     * Returns the current probability of the song
     * @return probablity
     */
    public int getProbability() {return probability;}


    /************************ checkers *****************************************/
    /**
     * Check if the song's location is within the range of given location
     * @param location the given location / center
     * @return true if song's location is within that location
     *          false otherwise
     */
    public boolean isInRange( double[] location) {
        double userLat = location[0];
        double userLon = location[1];

        double userLatR = this.toRadians(userLat);
        double storedLatR = this.toRadians(this.lat);

        double deltaLat = this.toRadians(userLat - this.lat);
        double deltaLon = this.toRadians(userLon - this.lon);

        // Raidus of earth in feet
        double radius = 3959 * 5280;

        // a = sin^2(deltaLat/2) + cos(lat1R) * cos(lat2R) * sin^2(deltaLon/2)
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(userLatR) * Math.cos(storedLatR) * Math.sin(deltaLon / 2)
                * Math.sin(deltaLon / 2);


        // c = 2 * atan2(sqrt(a) * sqrt(1-a))
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt((1-a)));

        // d = radius * c

        double distance = radius * c;
        if( distance <= Constant.LOC_THRESHOLD) return true;
        else return false;
    }

    /**
     * helper function that converts degree to radiant
     * @param degrees the degree we are trying to convert
     * @return the radiant converted from parameter degree
     */
    private static double toRadians(double degrees){
        return degrees * Math.PI / 180;
    }





    /**
     * Get the day of the week
     * @param value the number of the day of the week, 1-monday so on
     * @return the string value of that day
     */
   /* private String getDayOfWeek(int value)
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
    }*/

    /**
     * Update probabilily of a song being played based on the location and time it was last played
     * @param currLocation the current location of the user
     * @param context the current activity the song lives on
     */
    /*public void updateProbability(double[] currLocation, Context context)
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

    public void updateProbability(double[] currLocation)
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
        if(getCurrentState() == 1)
        {
            prob++;
        }
        if(getCurrentState() == -1)
        {
            prob = 0;
            this.probability = 0;
        }
        this.probability += prob;

    }

    public int getCurrentState()
    {
        return currentState;
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
    }*/

    /**
     * Calculate if the song is with in a certain threshold of where it was last played
     * @param currLocation the current location of the user
     * @param threshold the range we want to set - within that range, we say the song is close enough
     * @return true if the song is close enough, false otherwise
     */
 /*   public boolean isWithinRange(double[] currLocation, int threshold)
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
    }*/

    /**
     * Return if in the morning, afternoon, or evening
     * @param time in hours
     * @return 0 if morning, 1 if afternoon, 2 if evening
     */
   /* public int getTimeZone(int time) {
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
    }*/

    /**
     * Uses the haversine formula to calculate distance between GPS coordinates
     * @param loc1 the current location user is at
     * @param loc2 the location where the song is last played
     * @return the distance between two location
     */
  /*  public static double calculateDist(double[] loc1, double[] loc2)
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

    }*/

}
