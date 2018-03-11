package com.gaparmar.mediaflashback;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by veronica.lin1218 on 3/9/2018.
 */

public class Artist {
    private ArrayList<String> allSongs;
    private String artistName;

    public Artist(String artistName) {
        this.artistName = artistName;
        allSongs = new ArrayList();
    }
    /**
     * Add song to artist's songs
     * @param song
     */
    public void addSong(String song) {
        if (!hasSong(song)) {
            allSongs.add(song);
        }
    }

    public String getArtistName() {
        return artistName;
    }

    /**
     * Add all songs by albums
     * @param album
     */
    public void addAlbum(Album album) {
        for (int i = 0; i < album.getNumSongs(); i++) {
            String currSong = album.getSongAtIndex(i).getFileName();
            Log.d("Artist", "Adding song for artist: " + currSong);
            addSong(currSong);
        }
        Log.d("Artist song list", "song list for " + artistName + allSongs.size());
    }

    public ArrayList<String> getAllSongs() {
        return allSongs;
    }

    public boolean hasSong(String song){
        return this.allSongs.contains(song);
    }
}
