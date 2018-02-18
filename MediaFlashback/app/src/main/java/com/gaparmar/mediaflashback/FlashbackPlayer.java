package com.gaparmar.mediaflashback;

/**
 * Created by Aaron on 2/14/2018.
 */

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FlashbackPlayer extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private List<Song> songsToPlay;
    int currInd = 0;
    private boolean isFinished = false;
    private boolean firstTime = true;

    private int timeStamp;
    private Song lastPlayed;
    private boolean playingSong = false;
    private Context context;

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
    public FlashbackPlayer(final Context current) {
        this.context = current;
        final UserLocation userLocation = new UserLocation(current);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            /**
             * Automatically play next song after each song completion
             * @param mp
             */
            @Override
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
        songsToPlay = new ArrayList<>();
    }

    /**
     * This constructor initializes the flashback player with a list of songs
     * @param list The list of songs to play
     * @param current the context of the calling Activity
     */
    public FlashbackPlayer(List<Song> list, final Context current) {
        this(current);
        songsToPlay = list;
        makeFlashbackPlaylist();
    }

    public List<Song> getSongsToPlay()
    {
        return songsToPlay;
    }

    /**
     * TODO
     */
    public void makeFlashbackPlaylist()
    {
        for(Song song : songsToPlay){
            song.updateProbability(context);
        }
        Collections.sort(songsToPlay, new SongCompare());

        for(Song song : songsToPlay)
        {
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

    public void loadMedia(int resourceID) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        AssetFileDescriptor assetFileDescriptor =
                context.getResources().openRawResourceFd(resourceID);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(
                    new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // automatically plays the next song not first
                            if (!firstTime) {
                                //firstTime = true;
                                mediaPlayer.start();
                            }
                        }
                    });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void playSong() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            playingSong = true;
        }
    }

    public void pauseSong() {
        mediaPlayer.pause();
    }

    public void resetSong() {
        mediaPlayer.reset();
    }

    public void nextSong() {
        firstTime = false;
        if (currInd != songsToPlay.size()-1 && songsToPlay.size() > 0) {
            resetSong();
            currInd++;

            loadMedia(songsToPlay.get(currInd).getResID());
            //if( firstTime ) playSong();
            // DONT UNCOMMENT
        }
        //else {
        // wrap around the list
        //currInd = 0;
        //loadMedia(songsToPlay.get(0).getResID());
        //}
    }

    public void previousSong() {
        if (currInd > 0) {
            resetSong();
            currInd--;
            loadMedia(songsToPlay.get(currInd).getResID());
            System.out.println( "Line 133 This index should be 0 " + currInd);
        } /*else {
            // wrap around to the last song.
            currInd = songsToPlay.size() - 1;
            System.out.println( "Line 137 This index should be 1 " + currInd);
            loadMedia(songsToPlay.get(songsToPlay.size()-1).getResID());
        }*/
        //if( firstTime ) playSong();
    }

    public void loadNewSong(Song s) {
        resetSong();
        songsToPlay.clear(); // clear our album
        songsToPlay.add(s);
        loadMedia(s.getResID());
        playSong();
    }

    /**
     * Add songs in album to songsToPlay List
     * @param a
     */
    public void loadAlbum(Album a) {
        resetSong();
        songsToPlay.clear();
        for (int i = 0; i < a.getNumSongs(); i++) {
            songsToPlay.add(a.getSongAtIndex(i));
        }
    }

    public Song getCurrSong() {
        System.out.println("currIndex\t"+currInd);
        return songsToPlay.get(currInd);
    }

    public int getCurrentSongId(){
        return getCurrSong().getResID();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isFinished() {
        return currInd == songsToPlay.size();
    }

    public boolean wasPlayingSong() { return playingSong; }

    /*
     * To stop a song from playing in normal mode
     */
    public /*static*/ void stopPlaying() {
        // If there is a song currently playing, record the song's info
        if( playingSong ) {
            playingSong = false;
            lastPlayed = this.getCurrSong();
            timeStamp = mediaPlayer.getCurrentPosition();
        }
    }

    /*
     * To resume a song when user coming back from flashback mode
     */
    public /*static*/ void resumePlaying() {
        if( lastPlayed != null ) {
            this.loadNewSong( lastPlayed );
            mediaPlayer.seekTo( timeStamp ); // Get to where user left off
            this.playSong();
            playingSong = true;
        }
    }

}
