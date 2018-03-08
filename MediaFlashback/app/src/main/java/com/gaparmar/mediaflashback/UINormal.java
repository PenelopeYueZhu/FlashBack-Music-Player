package com.gaparmar.mediaflashback;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by lxyzh on 2/17/2018.
 */

public class UINormal extends UIHandler {

    // All the buttons and views on the MainActivity
    Context context;
    private Handler handler;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageButton toggleBtn;
    private ImageButton downloadBtn;
    private EditText inputURL;

    MusicDownloader musicDownloader;
    private String songURL = "http://soundbible.com/grab.php?id=2190&type=mp3";
    MusicQueuer musicQueuer;
    MusicPlayer musicPlayer;

    public final static int TITLE_POS = 0;
    public final static int DATE_POS = 1;
    public final static int DURATION_POS = 2;
    public final static int LOC_POS = 3;
    public final static int ALBUM_POS = 5;
    public final static int ARTIST_POS = 4;

    // Initilize everything so we can actually use it
    public UINormal( Context context ){
        super(context);
        musicQueuer = MainActivity.getMusicQueuer();
        musicPlayer = MainActivity.getMusicPlayer();
        musicDownloader = MainActivity.getMusicDownloader();
        this.context = context;

        playButton =  ((Activity)context).findViewById(R.id.play_button);
        pauseButton =((Activity)context).findViewById(R.id.pause_button);
        nextButton = ((Activity)context).findViewById(R.id.next_button);
        prevButton = ((Activity)context).findViewById(R.id.previous_button);
        toggleBtn = ((Activity)context).findViewById(R.id.toggleBtn);
//        inputURL = ((Activity)context).findViewById(R.id.inputURL);
//        downloadBtn = ((Activity)context).findViewById(R.id.downloadBtn);

        toggleBtn.setImageResource(R.drawable.neutral);
        toggleBtn.setTag(NEUTRAL);

        handler = new android.os.Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                if( musicPlayer != null ) {
                    // Unless there is a song playing when we get back to normal mode, hide the button
                    if (!musicPlayer.isPlaying()) {
                        playButton.setVisibility(View.VISIBLE);
                        pauseButton.setVisibility(View.GONE);
                    } else {
                        updateTrackInfo();
                        playButton.setVisibility(View.GONE);
                        pauseButton.setVisibility(View.VISIBLE);
                    }
                    handler.postDelayed(this, 500);
                }
            }
        });

    }

    /**
     * Link all the buttons with listeners
     */
    public void setButtonFunctions() {
        // Set the button's functions

        // Plays the song and updates the UI when the play button is pressed
        playButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UINomarl", "playbutton clicked");
                // Dont't do anything if no song is currently selected
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;

                }catch(NullPointerException e){
                    return;
                }

                musicPlayer.playSong();
                updateTrackInfo();
                setButtonsPlaying();
            }

        });

        // Pauses the song and updates the UI when the pause button is pressed
        pauseButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view ) {
                Log.d("UINomarl", "pausebutton clicked");
                musicPlayer.pauseSong();
                setButtonsPausing();
            }
        });

        // Skips to the next song and updates the UI when the next button is pressed
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UINomarl", "nextbutton clicked");
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;
                }catch(NullPointerException e){
                    return;
                }

                musicPlayer.nextSong();
                setButtonToggle(context, musicPlayer.getCurrentSongFileName());
                // Dont't do anything if no song is currently selected

                // Load all the information about the song
                updateTrackInfo();
                setButtonsPlaying();
            }
        });

        // Goes to previous song and updates UI when previous button is pressed
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UINomarl", "prevbutton clicked");
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;
                }catch (NullPointerException e){
                    return;
                }
                musicPlayer.previousSong();
                setButtonToggle(context, musicPlayer.getCurrentSongFileName());
                // Dont't do anything if no song is currently selected

                // Load all the information about the song
                updateTrackInfo();
                setButtonsPlaying();
            }
        });

        // Switches the liked button and changes the state of the song
        // when clicked.
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UINomarl", "togglebutton clicked");
                String toggleState = toggleBtn.getTag().toString();
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;
                }  catch(NullPointerException e){
                    toggleState = ERROR_STATE;
                }

                switch (toggleState) {
                    // switch from neutral to like
                    case NEUTRAL:
                        toggleBtn.setImageResource(R.drawable.like);
                        toggleBtn.setTag(LIKE);

                        if (musicPlayer.getCurrSong() != null) {
                            //musicPlayer.getCurrSong().setCurrentState(1);
                            StorageHandler.storeSongState(context, musicPlayer.getCurrentSongFileName(), 1);
                        }

                        Toast likeToast = Toast.makeText(context, LIKE, Toast.LENGTH_SHORT);
                        likeToast.show();
                        break;

                    // switch from like to dislike
                    case LIKE:
                        toggleBtn.setImageResource(R.drawable.unlike);
                        toggleBtn.setTag(DISLIKE);

                        if (musicPlayer.getCurrSong() != null) {
                            //musicPlayer.getCurrSong().setCurrentState(-1);
                            StorageHandler.storeSongState(context, musicPlayer.getCurrentSongFileName(), -1);
                        }

                        Toast dislikeToast = Toast.makeText(context, DISLIKE, Toast.LENGTH_SHORT);
                        dislikeToast.show();
                        break;

                    // switch from dislike to neutral
                    case DISLIKE:
                        toggleBtn.setImageResource(R.drawable.neutral);
                        toggleBtn.setTag(NEUTRAL);

                        if (musicPlayer.getCurrSong() != null) {
                            //musicPlayer.getCurrSong().setCurrentState(0);
                            StorageHandler.storeSongState(context, musicPlayer.getCurrentSongFileName(), 0);
                        }

                        Toast neutralToast = Toast.makeText(context, NEUTRAL, Toast.LENGTH_SHORT);
                        neutralToast.show();
                        break;
                    case ERROR_STATE:
                        break;
                }
            }
        });

        /*ownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UINomarl", "downloadButton clicked");

                Log.d("UINomarl",inputURL.getText().toString());
                // need to have input
                if (inputURL.getText() != null) {
                    String url = inputURL.getText().toString();
                    Toast downloadingToast = Toast.makeText(context, "Downloading from "
                            + url, Toast.LENGTH_SHORT);
                    downloadingToast.show();

                    if (musicDownloader == null) {
                        musicDownloader = MainActivity.getMusicDownloader();
                    }
                    musicDownloader.downloadData(url, "Song name", "mp3");

                } else {
                  // no url link provided
                    Toast noURLToast = Toast.makeText(context, "Please enter URL", Toast.LENGTH_SHORT);
                    noURLToast.show();
                }
            }
        });*/

    }

    /**
     * Grab information about the song that's playing right now and display on UI
     */
    public void updateTrackInfo() {
        Log.d("UINormal", "Reset displayed information of the song to the current song");
        ArrayList<String> songInfo = musicQueuer.getSongInfo(musicPlayer.getCurrentSongFileName());
        songTitleDisplay.setText( songInfo.get(TITLE_POS));
        songDateDisplay.setText( songInfo.get(DATE_POS));
        songLocationDisplay.setText( songInfo.get(LOC_POS));
        songTimeDisplay.setText( songInfo.get(DURATION_POS));
        songAlbumDisplay.setText(songInfo.get(ALBUM_POS));
        songArtistDisplay.setText(songInfo.get(ARTIST_POS));
    }

    public void setButtonToggle(Context context, String fileName)
    {
        switch(StorageHandler.getSongState(context, fileName))
        {
            case 1:
                toggleBtn.setImageResource(R.drawable.like);
                toggleBtn.setTag(LIKE);
                break;
            case 0:
                toggleBtn.setImageResource(R.drawable.neutral);
                toggleBtn.setTag(NEUTRAL);
                break;
            case -1:
                toggleBtn.setImageResource(R.drawable.unlike);
                toggleBtn.setTag(DISLIKE);
                break;
        }
    }

    /**
     * Changes all elements of the UI to their default value
     */
    public void resetInfo(){
        Log.d("UINomral", "Reset displayed information of songs to NONE");
        songTitleDisplay.setText(INIT_INFO);
        songDateDisplay.setText(INIT_INFO);
        songLocationDisplay.setText(INIT_INFO);
        songTitleDisplay.setText(INIT_INFO);
        songArtistDisplay.setText(INIT_INFO);
        songAlbumDisplay.setText(INIT_INFO);

    }

    /**
     * Hide play button and show pause button
     */
    public void setButtonsPlaying() {
        Log.d("UINormal", "set button playing");
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    /**
     * Hide pause button and show play button
     */
    public void setButtonsPausing() {
        Log.d("UINormal", "set Button Pauseing");
        playButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }
}
