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
    private UserLocation userLocation;


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
        final UserLocation userLocation = new UserLocation(current);

    }

    /**
     * This constructor initializes the flashback player with a list of songs
     * @param list The list of songs to play
     * @param current the context of the calling Activity
     */
    public FlashbackPlayer(ArrayList<String> list, final Context current, MusicQueuer musicQueuer) {
        this(current, musicQueuer);
        allSongs = list;
        makeFlashbackPlaylist();
    }

    public List<String> getSongsToPlay()
    {
        return songsToPlay;
    }

    /**
     * Compile a list of songs to play for the user based on probability calculated
     */
    public void makeFlashbackPlaylist()
    {
        for(String fileName : allSongs){
            Song song = musicQueuer.getSong(fileName);
            song.updateProbability(userLocation.getLoc(), context);
            Log.d("FBP:makeFlashbackPlaylist", "Adding songs to the list");
            //if(StorageHandler.getSongDay())
            sortedList.add(song);
        }
        Collections.sort(sortedList, new SongCompare());

        for(String fileName : allSongs)
        {
            Song song = musicQueuer.getSong(fileName);
            Log.d("FBP:makeFlashbackPlaylist","songName "+ song.getTitle());

        }

        for (Song x : sortedList) {
            Log.d("FBP:makeFlashbackPlaylist", "sortedList "+ x.getTitle());
        }
    }

    /**
     * Add songs in album to the list of songs this flashback player plays through
     */
    public void loadPlaylist() {
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
}
