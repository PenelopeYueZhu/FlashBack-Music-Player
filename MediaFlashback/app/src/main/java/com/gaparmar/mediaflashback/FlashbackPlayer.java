package com.gaparmar.mediaflashback;

/**
 * Created by Aaron on 2/14/2018.
 */

import android.Manifest;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

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


    ArrayList<Song> songsList = new ArrayList<Song>();
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
       // mediaPlayer = new MediaPlayer();
       // mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            /**
             * Automatically play next song after each song completion
             * @param mp
             */

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
           /* @Override
            public void onCompletion(MediaPlayer mp) {
                firstTime = false;
                isFinished = (currInd == songsToPlay.size()-1);
                // Updates the Song information in the Shared Preference resource
                System.out.println("on completion listener from the flashback player called");
                StorageHandler.storeSongLocation(current,getCurrentSongId(),userLocation.getLoc());
                StorageHandler.storeSongDay(current, getCurrentSongId(), "Friday");
                StorageHandler.storeSongTime(current, getCurrentSongId(), 0);
                StorageHandler.storeSongState(current, getCurrentSongId(), Song.state.DISLIKED);
                // if not finished, automatically play next song
                if (!isFinished() && songsToPlay.size() > 0) {
                    nextSong();
                }
            }
        });
        songsToPlay = new ArrayList<>();*/
    }

    /**
     * This constructor initializes the flashback player with a list of songs
     * @param list The list of songs to play
     * @param current the context of the calling Activity
     */
    public FlashbackPlayer(List<Integer> list, final Context current, MusicQueuer musicQueuer) {
        this(current, musicQueuer);
        songsToPlay = list;
        makeFlashbackPlaylist();
    }

    public List<Integer> getSongsToPlay()
    {
        return songsToPlay;
    }

    /**
     * TODO
     */
    public void makeFlashbackPlaylist()
    {

        for(Integer songId : songsToPlay){
            Song song = musicQueuer.getSong(songId);
            song.updateProbability(userLocation.getLoc(), context);
            songsList.add(song);
        }
        Collections.sort(songsList, new SongCompare());

        for(Integer songId : songsToPlay)
        {
            Song song = musicQueuer.getSong(songId);
            System.out.println(song.getTitle());
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

}
