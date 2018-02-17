package com.gaparmar.mediaflashback;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veronica.lin1218 on 2/4/2018.
 */

public class MusicPlayer extends AppCompatActivity {
    private MusicQueuer musicQueuer;
    private MediaPlayer mediaPlayer;
    private static List<Integer> songsToPlay;
    private int currInd = 0;
    private boolean isFinished = false;
    private boolean firstTime = true; /* flag representing if this is first song played */

    private int timeStamp;
    private Song lastPlayed;
    private boolean playingSong = false;
    private Context context;

    /**
     * Default MusicPlayer constructor
     * @param current The reference activity context
     * @param musicQueuer the MusicQueuer object that stores all the songs
     */
    public MusicPlayer(Context current, MusicQueuer musicQueuer ) {
        this.musicQueuer = musicQueuer;
        this.context = current;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            /**
             * Automatically play next song after each song completion
             * @param mp
             */
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCompletion(MediaPlayer mp) {
                firstTime = false;
                isFinished = (currInd == songsToPlay.size()-1);
                // if not finished, automatically play next song
                if (!isFinished() && songsToPlay.size() > 0) {
                    nextSong();
                }
            }
        });
        songsToPlay = new ArrayList<>();
        System.out.println("Music Player object created");
    }


    // TODO:: Maybe rename this function?
    /**
     * Loads the resources need to play a song
     * and Starts playing the given song
     * @param resourceID The ID of the song to be played
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
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


    /**
     * Resumes playing the currently loaded song
     */
    public void playSong() {
        if (mediaPlayer != null /*&& !playingSong*/) {
            mediaPlayer.start();
            playingSong = true;
        }
    }


    /**
     * Pauses the currently playing song
     */
    public void pauseSong() {
        mediaPlayer.pause();
    }

    /**
     * Resets the currently playing song
     */
    public void resetSong() {
        mediaPlayer.reset();
    }

    /**
     * Starts playing the next song in the queue
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void nextSong() {
        firstTime = false;
        if (currInd != songsToPlay.size()-1 && songsToPlay.size() > 0) {
            resetSong();
            currInd++;

            System.out.println( "Line 122 this index should be 1 " + currInd );
            loadMedia( musicQueuer.getSong(songsToPlay.get(currInd)).getRawID());
            //if( firstTime ) playSong();
            // DONT UNCOMMENT
        }
        //else {
            // wrap around the list
            //currInd = 0;
            //loadMedia(songsToPlay.get(0).getRawID());
        //}
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void previousSong() {
        if (currInd > 0) {
            resetSong();
            currInd--;
            loadMedia( musicQueuer.getSong(songsToPlay.get(currInd)).getRawID());
            System.out.println( "Line 133 This index should be 0 " + currInd);
        } /*else {
            // wrap around to the last song.
            currInd = songsToPlay.size() - 1;
            System.out.println( "Line 137 This index should be 1 " + currInd);
            loadMedia(songsToPlay.get(songsToPlay.size()-1).getRawID());
        }*/
        //if( firstTime ) playSong();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadNewSong(Song s) {
        resetSong();
        songsToPlay.clear(); // clear our album
        songsToPlay.add(s.getRawID());
        loadMedia(s.getRawID());
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
            songsToPlay.add(a.getSongAtIndex(i).getRawID());
        }
    }

    /**
     * @return The current Song to be Played
     *          NULL if no song is currently selected
     */
    public Song getCurrSong() {
        if (songsToPlay.size()==0)
            return null;
        return musicQueuer.getSong(songsToPlay.get(currInd));
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isFinished() {
        return currInd == songsToPlay.size();
    }

    public boolean wasPlayingSong() { return playingSong; }


    /**
     * Stops playing the current song being played in normal mode
     */
    public void stopPlaying() {
      // If there is a song currently playing, record the song's info
      if( playingSong ) {
        playingSong = false;
        lastPlayed = this.getCurrSong();
        timeStamp = mediaPlayer.getCurrentPosition();
      }
    }

    /**
     * To resume a song when user coming back from flashback mode 
     */
    public void resumePlaying() {
      if( lastPlayed != null ) {
        this.loadNewSong( lastPlayed );
        mediaPlayer.seekTo( timeStamp ); // Get to where user left off
        this.playSong();
        playingSong = true;
      }
    }
}
