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
    private ArrayList<Song> songs;


    /* The default constructor */
    public Album(){
        super();
        songs = new ArrayList<>();
        numSongs = 0;
        artistName = "";
        releaseYear = -1;
        lengthInSeconds = -1;
    }

    /* The custom constructor that takes an array of song objects*/
    public Album(Song[] songs){
        this();
        this.songs = new ArrayList<>(Arrays.asList(songs));
        this.numSongs = songs.length;

        // If input array is not empty
        if (songs.length > 0){
            this.artistName = songs[0].getArtistName();
            this.releaseYear = songs[0].getYearOfRelease();
        }

        // Calculate the sum of the length of each song
        int totalLength = 0;
        for (int i = 0; i<songs.length; i++)
            totalLength += songs[i].getLengthInSeconds();
        this.lengthInSeconds = totalLength;
    }

    public void addSong(Song s) throws InvalidParameterException{
        if (s == null){
            System.err.println("The Song object is uninitialized");
            throw new InvalidParameterException();
        }
        boolean invalidInput = false;
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
            this.songs.add(s);
            this.numSongs++;
            lengthInSeconds += s.getLengthInSeconds();
        }
    }


    public boolean hasSong(Song s){
        return this.songs.contains(s);
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
        return songs.get(i);
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




}
