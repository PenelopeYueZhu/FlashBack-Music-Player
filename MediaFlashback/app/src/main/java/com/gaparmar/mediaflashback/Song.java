package com.gaparmar.mediaflashback;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import static android.content.Context.MODE_PRIVATE;

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
    private final int LATITUDE = 0;  // constants represting location[] index
    private final int LONGITUDE = 1;
    private String title;
    private String parentAlbum;
    private state currentState;
    private double[] location;    // [Latitude, Longitude] stored as double[]
    private String artistName;
    private int rawID;
    private int lengthInSeconds;
    private int yearOfRelease;
    private int timeLastPlayed; // TODO:: temporary way of storing the time stamp
    private int probability; // TODO:: not yet implemented?

    private SharedPreferences sharedPreferences;


    /* the default constructor */
    public Song(){
        super();
        title = "";
        parentAlbum = "NA";
        currentState = state.NEITHER;
        location = new double[2];
        location[LATITUDE] = 0.0;
        location[LONGITUDE] = 0.0;
        artistName = "";
        rawID = 0;
        lengthInSeconds = -1;
        yearOfRelease = -1;
        timeLastPlayed = -1;
        probability = 1;
    }

    public Song(String title, String parentAlbum,
                String artistName, int lengthInSeconds,
                int yearOfRelease, int rawID, double[] location,
                Context context){
        this();
        this.title = title;
        this.parentAlbum = parentAlbum;
        this.lengthInSeconds = lengthInSeconds;
        this.artistName = artistName;
        this.yearOfRelease = yearOfRelease;
        this.rawID = rawID;
        this.probability = 1;
        this.location = location;
        this.sharedPreferences = getPrefs(context);
    }

    private static SharedPreferences getPrefs(Context context){
        return context.getSharedPreferences("Location", MODE_PRIVATE);
    }
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getParentAlbum(){
        return this.parentAlbum;
    }

    public void setParentAlbul(String parentAlbum){
        this.parentAlbum = parentAlbum;
    }

    public state getCurrentState(){
        return this.currentState;
    }

    public void setCurrentState(state currentState){
        this.currentState = currentState;
    }

    public double[] getLocation(Context context){
        sharedPreferences = getPrefs(context);
        String loc = sharedPreferences.getString(getTitle(), "");
        if(loc.length() == 0){
            return new double[]{0.0, 0.0};
        }else{
            return new double[]{Double.parseDouble(loc.substring(0, loc.indexOf(","))),
            Double.parseDouble(loc.substring(loc.indexOf(",")+1, loc.length()))};
        }
    }

    public void setLocation(double[] location, Context context){
        this.location = location;
        sharedPreferences = getPrefs(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getTitle(), "" + location[0] + "," + location[1]);
        editor.apply();

    }

    public String getArtistName(){
        return this.artistName;
    }

    public void setArtistName(String artistName){
        this.artistName = artistName;
    }

    public int getLengthInSeconds(){
        return this.lengthInSeconds;
    }

    public void setLengthInSeconds(int lengthInSeconds){
        this.lengthInSeconds = lengthInSeconds;
    }

    public int getYearOfRelease(){
        return this.yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease){
        this.yearOfRelease = yearOfRelease;
    }

    public int getTimeLastPlayed(){
        return this.timeLastPlayed;
    }

    public void setTimeLastPlayed(int timeLastPlayed){
        this.timeLastPlayed = timeLastPlayed;
    }

    public void setRawID(int rawID) { this.rawID = rawID; }

    public int getRawID() { return this.rawID; };

    public void updateProbability(Context context)
    {
        int prob = 0;
        if(isWithinRange(new double[2], context)) // TODO : pass in users current location
        {
            prob++;
        }
        if(isSameDay())
        {
            prob++;
        }
        if(isSameTime())
        {
            prob++;
        }
        if(getCurrentState() == state.LIKED)
        {
            prob++;
        }
        if(getCurrentState() == state.DISLIKED)
        {
            prob = 0;
            this.probability = 0;
        }
        this.probability += prob;
    }

    public int getProbability(){
        return this.probability;
    }

    public void setProbability(int x)
    {
        probability = x;
    }

    public boolean isWithinRange(double[] currLocation, Context context)
    {
        //TODO:: create method to determine if the current location is in the same range as the last played location
        // 1000 ft
        return calculateDist(currLocation, getLocation(context)) <= 1000;
    }

    public boolean isSameDay()
    {
        //TODO:: create method to determine if the current day is the same as last played day
        return false;
    }

    public boolean isSameTime()
    {
        //TODO:: create method to determine if the current time interval is the same as last played time interval
        return false;
    }
    /**
     * This method uses the haversine formula to calculate distance between GPS coordinates
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

    public static double toRadians(double degrees){
        return degrees * Math.PI / 180;
    }

}
