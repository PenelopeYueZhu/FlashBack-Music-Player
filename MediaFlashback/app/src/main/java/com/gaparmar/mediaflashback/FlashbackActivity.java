package com.gaparmar.mediaflashback;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class represents the Flashback Mode screen
 */
public class FlashbackActivity extends AppCompatActivity {
    private Song s;
    ArrayList<Integer> arr = new ArrayList<>();

    private FusedLocationProviderClient mFusedLocationClient;

    private Handler handler;

    public final static int TITLE_POS = 0;
    public final static int DATE_POS = 1;
    public final static int DURATION_POS = 2;
    public final static int LOC_POS = 3;
    public final static int ALBUM_POS = 5;
    public final static int ARTIST_POS = 4;

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
    VibeQueuer vq;
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

// Initializie the song functions
        if( mq == null ) {
            mq = new MusicQueuer(this);
            mq.readSongs();
            mq.readAlbums();

            arr = mq.getEntireSongList();
        }

        if( vq == null ) {
            vq = new VibeQueuer(this);
            vq.readSongs();
            vq.readAlbums();

            arr = vq.getEntireSongList();
        }

        flashbackPlayer = new FlashbackPlayer(arr,this, mq);

     /*   if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d("test1","ins");
            return;
        }else {
            Log.d("test2", "outs");
        }*/

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                MainActivity.getAddressRetriver().setLocation(location);
                Log.d("FBLgetting location", "Setting the location to address retriver");
                vq.makeVibeList();
                vq.loadPlaylist(flashbackPlayer);
                flashbackPlayer.loadList();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        /// Register the listener with the Location Manager to receive location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
        } catch( SecurityException e){

        }

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



        // Thread behind the scenes to update UI
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Unless there is a song playing when we get back to normal mode, hide the button
                if( !flashbackPlayer.isPlaying()) {
                    playButton.setVisibility(View.VISIBLE);
                    pauseButton.setVisibility(View.GONE);
                }
                else {
                    updateTrackInfo(flashbackPlayer.getCurrSong());
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
        Log.d("UINormal", "Reset displayed information of the song to the current song");
        ArrayList<String> songInfo = mq.getSongInfo(currentSong.getResID());
        songTitleDisplay.setText( songInfo.get(TITLE_POS));
        songDateDisplay.setText( songInfo.get(DATE_POS));
        songLocationDisplay.setText( songInfo.get(LOC_POS));
        songTimeDisplay.setText( songInfo.get(DURATION_POS));
        songAlbumDisplay.setText(songInfo.get(ALBUM_POS));
        songArtistDisplay.setText(songInfo.get(ARTIST_POS));
    }

}
