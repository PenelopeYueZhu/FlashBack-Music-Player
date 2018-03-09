package com.gaparmar.mediaflashback;

/**
 * Created by gauravparmar on 2/2/18.
 */
public class Song {

    private String songURL;
    private String title;
    private String parentAlbum;
    private String artistName;
    private String fileName;
    private int yearOfRelease;
    private Constant.State state;

    /**
     *  the default constructor
     *  Initializes all members to default values
     */
    public Song(){
        title = "";
        parentAlbum = "NA";
        artistName = "";
        songURL = "";
        yearOfRelease = -1;
        state = Constant.State.NEUTRAL;
    }

    /**
     * @param state the state of the song
     */
    public void setState(Constant.State state) {
        this.state = state;
    }
    /**
     * @return state the state of the song
     */
    public Constant.State getSate(){
        return state;
    }


    /**
     * @param URL the url string where we can download the song
     */
    public void setSongURL( String URL ){
        this.songURL = URL;
    }
    /**
     * @return the URL where the song was downloaded from
     */
    public String getSongURL(){
        return this.songURL;
    }


    /**
     * @param title the title of this song object
     */
    public void setTitle( String title ){
        this.title = title;
    }
    /**
     * @return Retrieves the Title of the Song object
     */
    public String getTitle(){
        return this.title;
    }


    /**
     * @param name The name of the song artist
     */
    public void setArtistName(String name){
        this.artistName = name;
    }
    /**
     * @return Song's Artist name
     */
    public String getArtistName(){
        return this.artistName;
    }


    /**
     * @param album The name of the parent album
     */
    public void setParentAlbum(String album){
        this.parentAlbum = album;
    }
    /**
     * Retrieves the name of the parent Album
     * @return parent Album's name
     */
    public String getParentAlbum(){
        return this.parentAlbum;
    }

    /**
     * @param fileName The name of the file stored locally
     */
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    /**
     * @return the file name of the local file
     */
    public String getFileName(){
        return this.fileName;
    }


    /**
     * @param year The release year of the song
     */
    public void setYearOfRelease(int year){
        this.yearOfRelease = year;
    }
    /**
     * Retrieves the name of the parent Album
     * @return parent Album's name
     */
    public int getYearOfRelease(){
        return this.yearOfRelease;
    }

}
