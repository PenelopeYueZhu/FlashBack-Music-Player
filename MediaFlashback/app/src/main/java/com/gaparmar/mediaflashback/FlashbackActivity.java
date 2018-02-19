package com.gaparmar.mediaflashback;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class represents the Flashback Mode screen
 */
public class FlashbackActivity extends AppCompatActivity {
    private Song s;
    ArrayList<Integer> arr = new ArrayList<>();

    private Handler handler;

    // This is all the fields on the main screen
    TextView songTitleDisplay;
    TextView songLocationDisplay;
    TextView songDateDisplay;
    TextView songTimeDisplay;
    TextView songAlbumDisplay;
    TextView songArtistDisplay;
    ImageButton playButton;
    ImageButton pauseButton;
    ImageButton nextButton;
    ImageButton prevButton;
    Button launchRegularMode;

    FlashbackPlayer flashbackPlayer;
    MusicQueuer mq;
    UserLocation userLocation;

    /**
     * Initializes all the View components of this activity
     */
    private void initializeViewComponents(){
        launchRegularMode = findViewById(R.id.regular_button);
        songTitleDisplay = findViewById(R.id.song_title);
        songDateDisplay = findViewById(R.id.song_date);
        songLocationDisplay = findViewById(R.id.song_location);
        songTimeDisplay = findViewById(R.id.song_time);
        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.previous_button);
        flashbackPlayer = new FlashbackPlayer(this, mq);
    }

    /**
     * Runs when the activity is created. Initializes the buttons,
     * UI, and music functinos
     * @param savedInstanceState The saved Bundle passed in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashback);
        //initializeViewComponents();

        userLocation = new UserLocation(this);

        launchRegularMode = (Button) findViewById(R.id.regular_button);
        songTitleDisplay = (TextView) findViewById(R.id.song_title);
        songDateDisplay = (TextView) findViewById(R.id.song_date);
        songLocationDisplay = (TextView) findViewById(R.id.song_location);
        songTimeDisplay = (TextView) findViewById(R.id.song_time);
        playButton = (ImageButton) findViewById(R.id.play_button);
        pauseButton = (ImageButton) findViewById(R.id.pause_button);
        nextButton = (ImageButton) findViewById(R.id.next_button);
        prevButton = (ImageButton) findViewById(R.id.previous_button);
        songArtistDisplay = findViewById(R.id.artist_title);
        songAlbumDisplay = findViewById(R.id.album_title);


        // Initializie the song functions
        if( mq == null ) {
            mq = new MusicQueuer(this);
            mq.readSongs();
            mq.readAlbums();

            arr = mq.getEntireSongList();
        }

        flashbackPlayer = new FlashbackPlayer(arr,this, mq);

        flashbackPlayer.makeFlashbackPlaylist();
        flashbackPlayer.loadPlaylist();


        // Thread behind the scenes to update UI
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateTrackInfo(flashbackPlayer.getCurrSong());

                // Unless there is a song playing when we get back to normal mode, hide the button
                if( !flashbackPlayer.isPlaying()) {
                    playButton.setVisibility(View.VISIBLE);
                    pauseButton.setVisibility(View.GONE);
                }
                else {
                    playButton.setVisibility(View.GONE);
                    pauseButton.setVisibility(View.VISIBLE);
                }
                handler.postDelayed(this, 500);
            }
        });

    }

    /**
     * The onClick callback for the Regular mode button
     * @param view
     */
    public void onRegularModeClick(View view){
        flashbackPlayer.resetSong();
        StorageHandler.storeLastMode(FlashbackActivity.this, 0);
        finish();
    }

    /**
     * The onClick callback for the Play Button
     * @param view
     */
    public void onPlayButtonClick(View view){
        System.out.println("play button clicked");
        // Dont't do anything if no song is currently selected
        try {
            if (flashbackPlayer.getCurrSong() == null)
                return;

        }   catch(NullPointerException e){
            return;
        }
        flashbackPlayer.playSong();

        // Replace the buttons
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);

        // Load all the information about the song
        updateTrackInfo(flashbackPlayer.getCurrSong());
    }

    /**
     * The onClick listener for the Pause button
     * @param view
     */
    public void onPauseButtonClick(View view){
        flashbackPlayer.pauseSong();
        playButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }

    /**
     * The onClick listener for the Next button
     * @param view
     */
    public void onNextButtonClick(View view){
        flashbackPlayer.nextSong();
        Song currentSong = flashbackPlayer.getCurrSong();

        // Load all the information about the song
        updateTrackInfo(currentSong);
    }

    /**
     * The onClick listener for the Previous button
     * @param view
     */
    public void onPreviousButtonClick(View view){
        flashbackPlayer.previousSong();
        Song currentSong = flashbackPlayer.getCurrSong();
        // Load all the information about the song
        updateTrackInfo(currentSong);

    }


    /**
     * Updates the displayed track information on the UI
     * @param currentSong The song that is currently playing
     */
    public void updateTrackInfo(Song currentSong) {
        songTitleDisplay.setText( currentSong.getTitle());
        songDateDisplay.setText( Integer.toString( currentSong.getTimeLastPlayed()));
        songLocationDisplay.setText(flashbackPlayer.getCurrSong().getLocationString(this));
        songTimeDisplay.setText( Integer.toString( currentSong.getLengthInSeconds() ));
        songAlbumDisplay.setText(flashbackPlayer.getCurrSong().getArtistName());
        songArtistDisplay.setText(flashbackPlayer.getCurrSong().getParentAlbum());
    }

}
