package com.gaparmar.mediaflashback;

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

    private String title;
    private String parentAlbum;
    private state currentState;
    private String location;    // TODO:: temporary way of storing location
    private String artistName;
    private int rawID;
    private int lengthInSeconds;
    private int yearOfRelease;
    private int timeLastPlayed; // TODO:: temporary way of storing the time stamp
    private int probability; // TODO:: not yet implemented?


    /* the default constructor */
    public Song(){
        super();
        title = "";
        parentAlbum = "NA";
        currentState = state.NEITHER;
        location = "NA";
        artistName = "";
        rawID = 0;
        lengthInSeconds = -1;
        yearOfRelease = -1;
        timeLastPlayed = -1;
        probability = 1;
    }

    public Song(String title, String parentAlbum,
                String artistName, int lengthInSeconds,
                int yearOfRelease, int rawID){
        this();
        this.title = title;
        this.parentAlbum = parentAlbum;
        this.lengthInSeconds = lengthInSeconds;
        this.artistName = artistName;
        this.yearOfRelease = yearOfRelease;
        this.rawID = rawID;
        this.probability = 1;
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

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location = location;
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

    public void updateProbability()
    {
        int prob = 0;
        if(isWithinRange())
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

    public boolean isWithinRange()
    {
        //TODO:: create method to determine if the current location is in the same range as the last played location
        return false;
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

}
