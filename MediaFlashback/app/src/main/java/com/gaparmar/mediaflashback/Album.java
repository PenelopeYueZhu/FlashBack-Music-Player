package com.gaparmar.mediaflashback;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by gauravparmar on 2/3/18.
 */

/**
 * This class represents an album, and allows for the creation and
 * manipulation of one.
 */
public class Album {

    private int numSongs;
    private String artistName;
    private int releaseYear;
    private int lengthInSeconds;
    private ArrayList<String> songs;
    private String albumTitle;

    // The music queuer for this album
    MusicQueuer mq = MainActivity.getMusicQueuer();


    /**
     * The default constructor.
     */
    public Album(){
        super();
        songs = new ArrayList<>();
        numSongs = 0;
        artistName = "";
        albumTitle="";
        releaseYear = -1;
        lengthInSeconds = -1;
    }

    /**
     * Constructor which sets the  album name
     * @param name the album title
     */
    public Album( String name ){
        this();
        songs = new ArrayList<>();
        numSongs = 0;
        artistName = "";
        albumTitle=name;
        releaseYear = -1;
        lengthInSeconds = -1;
    }

    /**
     * Constructor that takes list of songs, music queuer and album title
     * @param songLists List of ID of songs
     * @param name The title of the Album
     */
    public Album(ArrayList<String> songLists, String name ){
        this();
        this.songs = songLists;
        this.numSongs = songs.size();
        this.albumTitle = name;
        // If input array is not empty
        if (numSongs> 0){
            this.artistName = mq.getSong(songs.get(0)).getArtistName();
            this.releaseYear = mq.getSong(songs.get(0)).getYearOfRelease();
        }

        // Calculate the sum of the length of each song
        int totalLength = 0;
        for (int i = 0; i< numSongs; i++)
            totalLength += mq.getSong( songs.get( i )).getLengthInSeconds();
        this.lengthInSeconds = totalLength;
    }

    /**
     * Adds a song to the Album
     * @param s The song to add
     * @throws InvalidParameterException
     */
    public void addSong(Song s) throws InvalidParameterException{
        if (s == null){
            System.err.println("The Song object is uninitialized");
            throw new InvalidParameterException();
        }
        boolean invalidInput = false;
        // If there is no song in the album yet
        if( numSongs == 0 ) {
            this.songs.add( s.getFileName() );
            this.numSongs++;
            this.releaseYear = s.getYearOfRelease();
            this.artistName = s.getArtistName();
            this.lengthInSeconds = s.getLengthInSeconds();
        }
        if (hasSong(s)){
            System.out.println("The song already exists in album");
            invalidInput = true;
        }

        if (s.getYearOfRelease() != this.releaseYear){
            System.out.println("The song was released in a different year");
            invalidInput = true;
        }

        // Only add if the input song is valid
        if (!invalidInput){
            this.songs.add(s.getFileName());
            this.numSongs++;
            lengthInSeconds += s.getLengthInSeconds();
        }
    }

    /**
     * Sets the MusicQueuer to the given one
     * @param mq The new MusicQueuer
     */
    public void setMusicQueuer(MusicQueuer mq) {
        this.mq= mq;
    }

    /**
     * Check if a song is contained in the album
     * @param s the song we want to check
     */
    public boolean hasSong(Song s){
        return this.songs.contains(s.getResID());
    }

    /**
     * Remove a song from the album
     * @param s the song that we want to remove
     * @throws InvalidParameterException
     */
    public void removeSong(Song s) throws InvalidParameterException{
        if (s == null){
            System.err.println("The Song object is uninitialized");
        }
        boolean invalidInput = false;
        if (!hasSong(s)){
            System.out.println("The song does not exist in the album");
            invalidInput = true;
        }

        if(!invalidInput){
            this.songs.indexOf(s);
            this.numSongs--;
            this.lengthInSeconds -= s.getLengthInSeconds();
        }
    }

    /**
     * Gets the song at a current index
     * @param i The index to get a song at
     * @return The song at that index
     */
    public Song getSongAtIndex(int i){
        return mq.getSong( songs.get( i ));
    }

    /**
     * Gets the number of songs in the album
     * @return The number of songs
     */
    public int getNumSongs(){
        return this.numSongs;
    }

    /**
     * Gets the artists name
     * @return The artist's name
     */
    public String getArtistName(){
        return this.artistName;
    }

    /**
     * Gets the release year of the album
     * @return The release year
     */
    public int getReleaseYear(){
        return this.releaseYear;
    }

    /**
     * Gets the length of the song
     * @return How long the song is
     */
    public int getLengthInSeconds(){
        return this.lengthInSeconds;
    }

    /**
     * Gets the title of the album
     * @return The album's title
     */
    public String getAlbumTitle(){return this.albumTitle;}

}
