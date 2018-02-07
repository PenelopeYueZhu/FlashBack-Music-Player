package com.gaparmar.mediaflashback;

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
    int currInd;
    private boolean isFinished = false;
    private boolean firstTime = true;

    private /*static */ boolean songPlaying = false;
    private /*static */ Song lastPlayed;
    private /*static */ long timeStamp;

    // temporary tester mp3 files
    private int[] mediaFiles = {R.raw.jazz_in_paris, R.raw.replay};

    /**
     * Default MusicPlayer constructor
     */
    public MusicPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener
                                     (new MediaPlayer.OnCompletionListener() {
            /**
             * Automatically play next song after each song completion
             * @param mp
             */
            @Override
            public void onCompletion(MediaPlayer mp) {
                firstTime = false;
                currInd++;
                isFinished = (currInd == songsToPlay.size()-1);
                // if not finished, automatically play next song
                if (!isFinished()) {
                    nextSong();
                }
            }
        });
        songsToPlay = new ArrayList<Song>();
    }

    public MusicPlayer(List<Song> list) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener
                                      (new MediaPlayer.OnCompletionListener() {
            /**
             * Automatically play next song after each song completion
             * @param mp
             */
            @Override
            public void onCompletion(MediaPlayer mp) {
                firstTime = false;
                currInd++;
                isFinished = (currInd == songsToPlay.size()-1);
                // if not finished, automatically play next song
                if (!isFinished()) {
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
                this.getResources().openRawResourceFd(resourceID);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(
                    new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // automatically plays the next song not first
                            if (!firstTime) {
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
        if (currInd != songsToPlay.size()-1) {
            mediaPlayer.reset();
            currInd++;
            loadMedia(mediaFiles[currInd]);
            mediaPlayer.start();
        }
    }

    public void previousSong() {
        if (currInd > 0) {
            resetSong();
            currInd--;
            loadMedia(mediaFiles[currInd]);
        } else {
            // wrap around to the last song.
            loadMedia(songsToPlay.get(songsToPlay.size()-1).getRawID());
        }
        playSong();
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
        return isFinished();
    }

    public void jumpTo( long songPosition ) {
      mediaPlayer.seekTo( songPosition );
    }

    public /*static*/ void stopPlayingNormalMode() {
      songPlaying = false;
      lastPlayed = songsToPlay.get(currInd);
      timeStamp = mediaPlayer.getCurrentPosition();
    }

    public /*static*/ void resumePlayingNormalMode() {
      // When there was no song playing, return
      if( lastPlayed == null ) return;
      
      // Load up saved values about the song last played
      this.loadNewSong( lastPlayed );
      this.jumpTo( timeStamp );
    }
}
