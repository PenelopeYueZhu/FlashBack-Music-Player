package com.gaparmar.mediaflashback;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by veronica.lin1218 on 2/4/2018.
 */

public class MusicPlayer extends AppCompatActivity {
    protected MusicQueuer musicQueuer;
    protected MediaPlayer mediaPlayer;
    protected UINormal tracker;
    protected List<Integer> songsToPlay;
    protected int currInd = 0;
    protected boolean isFinished = false;
    protected boolean firstTime = true; /* flag representing if this is first song played */
    protected int timeStamp = 0;
    protected static Song lastPlayed;
    protected boolean playingSong = false;
    protected Context context;

    protected Calendar currDate;
    protected SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
    protected SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.US);

    /**
     * default contructor. Doesn't do anything
     */
    public MusicPlayer(){}
    /**
     * Default MusicPlayer constructor
     * @param current The reference activity context
     * @param musicQueuer the MusicQueuer object that stores all the songs
     */
    public MusicPlayer(final Context current, MusicQueuer musicQueuer ) {
        this.musicQueuer = musicQueuer;
        this.tracker = MainActivity.getUITracker();
        this.context = current;
        currDate = Calendar.getInstance();
        mediaPlayer = new MediaPlayer();
        final UserLocation userLocation = new UserLocation(current);

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            /**
             * Automatically play next song after each song completes
             * @param mp
             */
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Update the date, time, and location

                double[] location = userLocation.getLoc();
                // If -1, location is turned off and should not be used
                if(location[0] != -1) {
                    StorageHandler.storeSongLocation(current, getCurrentSongId(), userLocation.getLoc());
                }
                String weekdayStr = dayFormat.format(currDate.getTime());
                getCurrSong().setDayOfWeek(weekdayStr);
                StorageHandler.storeSongDay(current, getCurrentSongId(), weekdayStr);
                int timeOfDay = Integer.parseInt(hourFormat.format(currDate.getTime()));
                getCurrSong().setTimeLastPlayed(timeOfDay);
                StorageHandler.storeSongTime(current, getCurrentSongId(), timeOfDay);
                StorageHandler.storeSongState(current, getCurrentSongId(), Song.state.DISLIKED);

                System.out.println("Song finished playing");
                firstTime = false;
                isFinished = (currInd == songsToPlay.size()-1);
                // if not finished, automatically play next song
                if (!isFinished() && songsToPlay.size() > 1) {
                    StorageHandler.storeSongLocation(current,getCurrentSongId(),new double[]{0.0,0.0});//userLocation.getLoc());
                    weekdayStr = dayFormat.format(currDate.getTime());
                    getCurrSong().setDayOfWeek(weekdayStr);
                    StorageHandler.storeSongDay(current, getCurrentSongId(), weekdayStr);
                    timeOfDay = Integer.parseInt(hourFormat.format(currDate.getTime()));
                    getCurrSong().setTimeLastPlayed(timeOfDay);
                    StorageHandler.storeSongTime(current, getCurrentSongId(), timeOfDay);
                    StorageHandler.storeSongState(current, getCurrentSongId(), Song.state.DISLIKED);
                    nextSong();
                }
                else {
                    mediaPlayer.reset();
                    tracker.setButtonsPausing();
                }
            }
        });
        songsToPlay = new ArrayList<>();
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
                                System.out.println("Song started");
                                //firstTime = true;
                                mediaPlayer.start();
                                playingSong = true;
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
            playingSong = true;
            mediaPlayer.start();
        }
    }


    /**
     * Pauses the currently playing song
     */
    public void pauseSong() {
        playingSong = false;
        mediaPlayer.pause();
    }

    /**
     * Resets the currently playing song
     */
    public void resetSong() {
        playingSong = true;
        mediaPlayer.reset();
    }

    /**
     * Starts playing the next song in the queue and return the that song. If there is no song to
     * play, return null
     * @return the new song that started playing
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Song nextSong() {
        Song song = null;
        firstTime = false;
        if (currInd != songsToPlay.size()-1 && songsToPlay.size() > 0) {
            resetSong();
            musicQueuer.getSong((songsToPlay.get(currInd))).getResID();
            currInd++;
            song = musicQueuer.getSong(songsToPlay.get(currInd));
            loadMedia( song.getResID());
            //if( firstTime ) playSong();
            // DONT UNCOMMENT
        }
        //else {
            // wrap around the list
            //currInd = 0;
            //loadMedia(songsToPlay.get(0).getRawID());
        //}
        return song;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void previousSong() {
        firstTime = false;
        if (currInd > 0) {
            resetSong();
            playingSong = true;
            currInd--;
            loadMedia( musicQueuer.getSong(songsToPlay.get(currInd)).getResID());

        } /*else {
            // wrap around to the last song.
            currInd = songsToPlay.size() - 1;
            System.out.println( "Line 137 This index should be 1 " + currInd);
            loadMedia(songsToPlay.get(songsToPlay.size()-1).getRawID());
        }*/
        //if( firstTime ) playSong();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadNewSong(Integer ID) {
        resetSong();
        playingSong = true;
        songsToPlay.clear(); // clear our album
        songsToPlay.add(ID);
        currInd = 0;
        if( firstTime ) firstTime = false;
        loadMedia(ID);
    }

    /**
     * Add songs in album to songsToPlay List
     * @param a
     */
    public void loadAlbum(Album a) {
        resetSong();
        songsToPlay.clear();
        for (int i = 0; i < a.getNumSongs(); i++) {
            songsToPlay.add(a.getSongAtIndex(i).getResID());
        }
        if( firstTime ) firstTime = false;
        currInd = 0;
        loadMedia(songsToPlay.get(0));
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

    public int getTimeStamp(){
        return mediaPlayer.getCurrentPosition();
    }


    /**
     * Stops playing the current song being played in normal mode
     */
    public int[] stopPlaying() {
      // If there is a song currently playing, record the song's info
        timeStamp = getTimeStamp();
      if( playingSong ) {
        lastPlayed = this.getCurrSong();
       // mediaPlayer.pause();
      }
        return new int[]{timeStamp, getCurrSong().getResID()};
    }

    /**
     * To resume a song when user coming back from flashback mode 
     */
    public void resumePlaying(int timeStamp, int songId) {
  //    if( lastPlayed != null ) {
        this.loadNewSong( songId );
           playingSong = true;
        this.playSong();
        mediaPlayer.seekTo( timeStamp ); // Get to where user left off

   //   }
    }
}
