package com.gaparmar.mediaflashback;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by veronica.lin1218 on 2/4/2018.
 */

public class MusicPlayer extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private List<Song> songsToPlay;
    int currInd = 0;
    private boolean isFinished = false;
    private boolean firstTime = true;

    private /*static*/ int timeStamp;
    private /*static*/ Song lastPlayed;
    private /*static*/ boolean playingSong = false;
    private Context context;

    // temporary tester mp3 files
    private int[] mediaFiles = {R.raw.jazz_in_paris, R.raw.replay};

    /**
     * Default MusicPlayer constructor
     */
    public MusicPlayer(Context current) {
        this.context = current;
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
                // if not finished, automatically play next song
                if (!isFinished() && songsToPlay.size() > 0) {
                    nextSong();
                }
            }
        });
        songsToPlay = new ArrayList<Song>();
    }

    public MusicPlayer(List<Song> list, Context current) {
        this.context = current;
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
                // if not finished, automatically play next song
                if (!isFinished() && songsToPlay.size() > 0) {
                    nextSong();
                }
            }
        });
        songsToPlay = list;
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

            System.out.println( "Line 122 this index should be 1 " + currInd );
            loadMedia(songsToPlay.get(currInd).getRawID());
            //if( firstTime ) playSong();
            // DONT UNCOMMENT
        }
        //else {
            // wrap around the list
            //currInd = 0;
            //loadMedia(songsToPlay.get(0).getRawID());
        //}
    }

    public void previousSong() {
        if (currInd > 0) {
            resetSong();
            currInd--;
            loadMedia(songsToPlay.get(currInd).getRawID());
            System.out.println( "Line 133 This index should be 0 " + currInd);
        } /*else {
            // wrap around to the last song.
            currInd = songsToPlay.size() - 1;
            System.out.println( "Line 137 This index should be 1 " + currInd);
            loadMedia(songsToPlay.get(songsToPlay.size()-1).getRawID());
        }*/
        //if( firstTime ) playSong();
    }

    public void loadNewSong(Song s) {
        resetSong();
        songsToPlay.clear(); // clear our album
        songsToPlay.add(s);
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
            songsToPlay.add(a.getSongAtIndex(i));
        }
    }

    public Song getCurrSong() {
        return songsToPlay.get(currInd);
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
