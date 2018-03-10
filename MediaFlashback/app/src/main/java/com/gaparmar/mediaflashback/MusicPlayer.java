package com.gaparmar.mediaflashback;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private final String MEDIA_PATH =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    +"/myDownloads/";
    protected MusicQueuer musicQueuer;
    protected MediaPlayer mediaPlayer;
    protected UINormal tracker;
    protected List<String> songsToPlay;
    protected int currInd = 0;
    protected boolean isFinished = false;
    protected boolean firstTime = true; /* flag representing if this is first song played */
    protected int timeStamp = 0;
    protected static Song lastPlayed;
    protected boolean playingSong = false;
    protected Context context;

    /**
     * default contructor. Doesn't do anything
     */
    public MusicPlayer(){}
    /**
     * Default MusicPlayer constructor
     * @param current The reference activity context
     * @param musicQueuer the MusicQueuer object that stores all the songs
     */
    public MusicPlayer(final Context current, final MusicQueuer musicQueuer ) {
        this.musicQueuer = musicQueuer;
        this.tracker = MainActivity.getUITracker();
        this.context = current;
        if( mediaPlayer == null ) {
            mediaPlayer = new MediaPlayer();
        }

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
                musicQueuer.updateTrackInfo(getCurrentSongFileName());

                Log.d("MP:OnCompleteListener","Song finished playing");
                firstTime = false;
                isFinished = (currInd == songsToPlay.size()-1);

                // if not finished, automatically play next song
                if (!isFinished() && songsToPlay.size() > 1) {
                    nextSong();
                }
                else {
                    firstTime = true;
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    if( songsToPlay.size() > 0) {
                        loadMedia(songsToPlay.get(0));
                        Log.d("MP:OnCompleteListener", "Song reloaded after finishing");
                    }
                }
            }
        });
        songsToPlay = new ArrayList<>();
    }

    public void loadMedia(String fileName) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        try {
            mediaPlayer.setDataSource(MEDIA_PATH+fileName);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(
                    new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // automatically plays the next song not first
                            if (!firstTime) {
                                Log.d("MP:LoadMedia", "Song Started");
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
            Log.d("MP:playSong", "song playing");
            playingSong = true;
            MainActivity.isPlaying = true;
            mediaPlayer.start();
        }
    }


    /**
     * Pauses the currently playing song
     */
    public void pauseSong() {
        Log.d("MP:pauseSong", "song pausing");
        playingSong = false;
        mediaPlayer.pause();
    }

    /**
     * Resets the currently playing song
     */
    public void resetSong() {
        Log.d("MP:reestSong", "song reset");
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
            musicQueuer.getSong((songsToPlay.get(currInd))).getFileName();
            currInd++;
            song = musicQueuer.getSong(songsToPlay.get(currInd));

            Log.d("MP:nextSong", "Loading the next song");

            loadMedia(songsToPlay.get(currInd));
            if (song != null)
            {
                tracker.updateToggle();
            }
        }
        return song;
    }
    /**
     * Starts playing the previous song in the queue and return the that song. If there is no song to
     * play, return null
     * @return the new song that started playing
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void previousSong() {
        Song song = null;
        firstTime = false;
        if (currInd > 0) {
            resetSong();
            playingSong = true;
            currInd--;

            Log.d("MP:previousSong", "Loading the previous song");

            loadMedia( songsToPlay.get(currInd));
            tracker.updateToggle();

        }
    }

    public void loadNewSong(String fileName) {
        resetSong();
        playingSong = true;
        songsToPlay.clear(); // clear our album
        songsToPlay.add(fileName);
        currInd = 0;
        if( firstTime ) firstTime = false;
        Log.d("MP:loadNewSong", "Loading the new song");

        loadMedia(fileName);
    }

    /**
     * Add songs in Artist to songsToPlay List
     * @param a artist
     */
    public void loadArtist(Artist a) {
        resetSong();
        songsToPlay.clear();
        ArrayList<String> songs = a.getAllSongs();
        for (int i = 0; i < songs.size(); i++) {
            Log.d("MP:loadArtist", "adding all the songs from artist");
            songsToPlay.add(songs.get(i));
        }
        if( firstTime ) firstTime = false;
        currInd = 0;
        Log.d("MP:loadArtist", "Loading the first song of the artist");

        loadMedia(songsToPlay.get(0));g
        MainActivity.isPlaying = true;
    }

    /**
     * Add songs in album to songsToPlay List
     * @param a
     */
    public void loadAlbum(Album a) {
        resetSong();
        songsToPlay.clear();
        for (int i = 0; i < a.getNumSongs(); i++) {
            Log.d("MP:loadAlbum", "adding all the songs from album");

            songsToPlay.add(a.getSongAtIndex(i).getFileName());
        }
        if( firstTime ) firstTime = false;
        currInd = 0;
        Log.d("MP:loadAlbum", "Loading the first song of the album");

        loadMedia(songsToPlay.get(0));
        MainActivity.isPlaying = true;
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

    public String getCurrentSongFileName(){
        return getCurrSong().getFileName();
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
    public ArrayList<String> stopPlaying() {
        ArrayList<String> songInfo = new ArrayList<>();
      // If there is a song currently playing, record the song's info
        timeStamp = getTimeStamp();
      if( playingSong ) {
        lastPlayed = this.getCurrSong();
        mediaPlayer.pause();
      }
      songInfo.add( Integer.toString(timeStamp));
      songInfo.add( getCurrentSongFileName());
      return songInfo;
    }

    /**
     * To resume a song when user coming back from flashback mode 
     */
    public void resumePlaying(int timeStamp, String fileName) {
  //    if( lastPlayed != null ) {
        this.loadNewSong( fileName );
           playingSong = true;
        this.playSong();
        mediaPlayer.seekTo( timeStamp ); // Get to where user left off

   //   }
    }
}
