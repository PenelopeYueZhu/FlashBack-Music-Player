package com.gaparmar.mediaflashback;

/**
 * Created by lxyzh on 3/4/2018.
 */

public class SongString {
    int id;

    // metadata fields
    String title;
    String album;
    String artist;
    int year;

    // Play data field
    double lat = 0.0;
    double lon = 0.0;
    int timeStamp = 0;
    int time = 0;
    String dayOfWeek = Constant.UNKNOWN;
    String address = Constant.UNKNOWN;
    String userName = Constant.UNKNOWN;

    // Rate of the song, default neutral
    int rate = 0;


    public SongString(){}

    public SongString(int id){
        this.id = id;
    }

    /************************ setters ***************************************/
    /**
     * Set the coordinates where a song is lastly played
     * @param lat the latitiude
     * @param lon the longitutde
     */
    protected void setCoords( double lat, double lon ) {
        this.lat = lat;
        this.lon = lon;
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
    protected void setTimeStamp( int timeStamp ){
        this.timeStamp = timeStamp;
    }

    /**
     * Set the rate of the song
     * @param rate - 1 like
     *             - 0 neural
     *             - -1 dislike
     */
    protected void setRate( int rate ){
        this.rate = rate;
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
     * Set the metadata of a song
     * @param title title of the song
     * @param album album of the song
     * @param artist artist of the song
     * @param year year of released
     */
    protected void setMetadata( String title, String album, String artist, int year ){
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.year = year;
    }

    /*******************************getters************************************/
    // For ID checking
    public int getId() {
        return id;
    }

    // For UI display
    public String getAlbum() {
        return album;
    }
    public String getArtist() {
        return artist;
    }
    public String getTitle(){
        return title;
    }
    
    public double getLat(){
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public double[] getCoord() {return new double[]{lat, lon};}
    public int getTime() {return time;}
    public int getRate() {return rate;}
}
