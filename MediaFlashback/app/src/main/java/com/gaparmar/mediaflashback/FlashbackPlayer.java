package com.gaparmar.mediaflashback;

/**
 * Created by Aaron on 2/14/2018.
 */

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Handles the music events of Flashback Mode
 */
public class FlashbackPlayer extends MusicPlayer {
    private Context context;

    ArrayList<Song> sortedList = new ArrayList<Song>();
    ArrayList<String> allSongs = new ArrayList<String>();
    private static class SongCompare implements Comparator<Song>{
        public int compare(Song s1, Song s2) {
            return s2.getProbability() - s1.getProbability();
        }
    }

    /**
     * The constructor initializes the flashback player without a list of songs
     * @param current The context of the calling activity
     */

    public FlashbackPlayer( final Context current, MusicQueuer musicQueuer) {
        super(current, musicQueuer);
        this.context = current;
    }

    /**
     * This constructor initializes the flashback player with a list of songs
     * @param list The list of songs to play
     * @param current the context of the calling Activity
     */
    public FlashbackPlayer(ArrayList<String> list, final Context current, MusicQueuer musicQueuer) {
        this(current, musicQueuer);
        allSongs = list;
       // makeFlashbackPlaylist();
    }

    public List<String> getSongsToPlay()
    {
        return songsToPlay;
    }

    /**
     * Set the playlist
     */
    public void setPlayList(ArrayList<Song> list) {
        for (int i = 0; i < list.size(); i++) {
            sortedList.add(list.get(i));
        }
    }

    /**
     * Add songs in album to the list of songs this flashback player plays through
     */
    public void loadList() {
        resetSong();
        songsToPlay.clear();
        for (int i = 0; i < sortedList.size(); i++) {
            Log.d("FBP:loadPlaylist", "sortedList " + sortedList.get(i).getTitle());
            songsToPlay.add(sortedList.get(i).getFileName());
        }
        if( firstTime ) firstTime = false;
        currInd = 0;
        loadMedia(songsToPlay.get(0));
    }

    public void addToList( String filename ){
        songsToPlay.add(filename);
        if( firstTime )firstTime = false;
    }
}
