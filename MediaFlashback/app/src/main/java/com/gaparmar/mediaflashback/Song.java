package com.gaparmar.mediaflashback;

/**
 * Created by gauravparmar on 2/2/18.
 */

// -1, NA, represents uninitialized value
public class Song {

    // Declaring the member variables of the Song object

    private String songURL;
    private String fileName;
    private String firebaseID;

    // MetaData field
    private String title;
    private String parentAlbum;
    private String artistName;
    private int yearOfRelease;

    // Play data
    private int rate;
    private String dayOfWeek;
    private String address;
    private String userName;
    private int probability;
    private int time;
    private long timeStamp;
    private double lat, lon;

    /**
     *  the default constructor
     *  Initializes all members to default values
     */
    public Song(){
        super();
        this.fileName = Constant.UNKNOWN;
        title = "";
        parentAlbum = "NA";
        rate = 0;
        lat = 0.0;
        lon = 0.0;
        artistName = "";
        songURL = "";
        fileName = "";
        yearOfRelease = -1;
        probability = 1;
        dayOfWeek = Constant.UNKNOWN;
        time = -1;
    }

    /**
     * The constructor that initializes the Song object based on the parameters
     * @param title The title of the Song
     * @param parentAlbum The title of the Song Album
     * @param artistName The name of the Artist
     * @param yearOfRelease The release year of the Song
     * @param fileName The file name of the song
     * @param location The last played Location coords of the Song
     */
    public Song(String title, String parentAlbum,
                String artistName,
                int yearOfRelease, String fileName, String songURL, double[] location){
        this();
        this.title = title;
        this.parentAlbum = parentAlbum;
        this.artistName = artistName;
        this.yearOfRelease = yearOfRelease;
        this.fileName = fileName;
        this.songURL = songURL;
        this.probability = 1;
        if(location != null) {
            this.lat = location[0];
            this.lon = location[1];
        }else{
            this.lat = 0.0;
            this.lon = 0.0;
        }

    }

   public Song( String fileName ){
       this.fileName = fileName;
       firebaseID = Song.reformatFileName(fileName);
       dayOfWeek = Constant.UNKNOWN;
       time = -1;
       probability = 1;
       lat = 0.0;
       lon = 0.0;
       rate = 0;
       timeStamp = 0;
       address = Constant.UNKNOWN;
       userName = Constant.UNKNOWN;
       this.songURL = Constant.UNKNOWN;
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
    protected void setAddress( String address ) {
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
    protected  void setUserName( String userName ) {
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

    /**
     * Set the URL for the song
     * @param URL the url string where we can download the song
     */
    protected void setSongURL( String URL ){
        this.songURL = URL;
    }

    /**
     * Set the title for the song
     * @param title the title of this song object
     */
    protected void setSongTitle( String title ){
        this.title = title;
    }



    /************************************** Getters for UI display *******************************/
    public String getFileName() {
        return fileName;
    }

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
     * Helper function that strips away the filename's special chars
     */
    public static String reformatFileName( String filename ) {
        return filename.replaceAll("\\.", " " );
    }

    public String getFirebaseID(){
        return this.firebaseID;
    }

}
