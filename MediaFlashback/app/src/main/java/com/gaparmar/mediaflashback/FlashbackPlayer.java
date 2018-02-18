package com.gaparmar.mediaflashback;

/**
 * Created by Aaron on 2/14/2018.
 */

import android.content.Context;
import android.content.MutableContextWrapper;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FlashbackPlayer extends MusicPlayer {

   /* private MediaPlayer mediaPlayer;
    private MusicQueuer musicQueuer;
    private List<Song> songsToPlay;
    int currInd = 0;
    private boolean isFinished = false;
    private boolean firstTime = true;

    private int timeStamp;
    private Song lastPlayed;
    private boolean playingSong = false;*/
    private Context context;
    private UserLocation userLocation;


    ArrayList<Song> sortedList = new ArrayList<Song>();
    ArrayList<Integer> allSongs = new ArrayList<Integer>();
    // TODO: SEPARATE CLASS
    private static class SongCompare implements Comparator<Song>{
        public int compare(Song s1, Song s2)
        {
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
    public FlashbackPlayer(ArrayList<Integer> list, final Context current, MusicQueuer musicQueuer) {
        this(current, musicQueuer);
        allSongs = list;
        makeFlashbackPlaylist();
    }

    public List<Integer> getSongsToPlay()
    {
        return songsToPlay;
    }

    public void makeFlashbackPlaylist()
    {
        for(Integer songId : allSongs){
            Song song = musicQueuer.getSong(songId);
            song.updateProbability(userLocation.getLoc(), context);
            sortedList.add(song);
        }
        Collections.sort(sortedList, new SongCompare());

        for(Integer songId : allSongs)
        {
            Song song = musicQueuer.getSong(songId);
            Log.d("songName", song.getTitle());

        }

        for (Song x : sortedList) {
            Log.d("sortedList", x.getTitle());
        }

        /*maxWeight = 0;
        intervalArray = new int[songsToPlay.size()][2];
        for(Song s : songsToPlay)
        {
            s.updateProbability();
        }
        for(int i = 0; i < songsToPlay.size(); i++)
        {
            intervalArray[i][0] = i;
            intervalArray[i][1] = songsToPlay.get(i).getProbability();
            maxWeight += intervalArray[i][1];
        }

        for(int i = 0; i < intervalArray.length; i++)
        {
            for(int j = 0; j < intervalArray[i].length; j++)
            {
                System.out.println(intervalArray[i][j]);
            }
        }
        System.out.println(maxWeight);*/
    }

    /**
     * Add songs in album to songsToPlay List
     */
    public void loadPlaylist() {
        resetSong();
        songsToPlay.clear();
        for (int i = 0; i < sortedList.size(); i++) {
            songsToPlay.add(sortedList.get(i).getResID());
        }
        if( firstTime ) firstTime = false;
        currInd = 0;
        loadMedia(songsToPlay.get(0));
    }
}
