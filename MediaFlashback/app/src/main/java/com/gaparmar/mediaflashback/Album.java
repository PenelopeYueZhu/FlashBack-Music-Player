package com.gaparmar.mediaflashback;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by gauravparmar on 2/3/18.
 */

public class Album {

    private int numSongs;
    private String artistName;
    private int releaseYear;
    private int lengthInSeconds;
    private ArrayList<Integer> songs;
    private String albumTitle;

    // The music queuer for this album
    MusicQueuer mq;


    /* The default constructor */
    public Album(){
        super();
        songs = new ArrayList<>();
        numSongs = 0;
        artistName = "";
        albumTitle="";
        releaseYear = -1;
        lengthInSeconds = -1;
    }

    public Album( String name ){
        super();
        songs = new ArrayList<>();
        numSongs = 0;
        artistName = "";
        albumTitle=name;
        releaseYear = -1;
        lengthInSeconds = -1;
        albumTitle = "";
    }

    /* The custom constructor that takes an array of song objects*/
    public Album(ArrayList<Integer> songLists, MusicQueuer mq, String name ){
        this();
        this.songs = songLists;
        this.numSongs = songs.size();
        this.mq = mq;
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

    public void addSong(Song s) throws InvalidParameterException{
        if (s == null){
            System.err.println("The Song object is uninitialized");
            throw new InvalidParameterException();
        }
        boolean invalidInput = false;
        // If there is no song in the album yet
        if( numSongs == 0 ) {
            this.songs.add( s.getRawID() );
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
            this.songs.add(s.getRawID());
            this.numSongs++;
            lengthInSeconds += s.getLengthInSeconds();
        }
    }


    public boolean hasSong(Song s){
        return this.songs.contains(s.getRawID());
    }

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

    public int getNum_songs(){
        return this.numSongs;
    }

    public Song getSongAtIndex(int i){
        return mq.getSong( songs.get( i ));
    }

    public int getNumSongs(){
        return this.numSongs;
    }

    public String getArtistName(){
        return this.artistName;
    }

    public int getReleaseYear(){
        return this.releaseYear;
    }

    public int getLengthInSeconds(){
        return this.lengthInSeconds;
    }

    public String getAlbumTitle(){return this.albumTitle;}




}
