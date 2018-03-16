package com.gaparmar.mediaflashback.UI;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaparmar.mediaflashback.DataStorage.StorageHandler;
import com.gaparmar.mediaflashback.FlashbackPlayer;
import com.gaparmar.mediaflashback.MusicDownloader;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.R;
import com.gaparmar.mediaflashback.Song;

import java.util.ArrayList;

/**
 * This class represents the Flashback Mode screen
 */
public class VibeActivity extends AppCompatActivity {
    ArrayList<String> arr = new ArrayList<>();

    private Handler handler;

    public final static int TITLE_POS = 0;
    public final static int ALBUM_POS = 1;
    public final static int ARTIST_POS = 2;
    public final static int LOC_POS = 3;
    public final static int TIME_POS = 4;
    public final static int DAY_POS = 5;
    public final static int USER_POS = 6;

    public static boolean flashBackIsPlaying = false;
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
    ImageButton launchRegularMode;

    public static FlashbackPlayer flashbackPlayer;
    LocationManager locationManager;
    LocationListener locationListener;
    static MusicQueuer mq;

    public static boolean firstTimeQueueing;

    public static MusicQueuer getMq () {return mq;}
    /**
     * Runs when the activity is created. Initializes the buttons,
     * com.gaparmar.mediaflashback.UI, and music functinos
     * @param savedInstanceState The saved Bundle passed in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashback);
        flashBackIsPlaying = true;
        firstTimeQueueing = true;

        //TODO: remove this test
        appendUpcomingSong("string from on create");

        //initializeViewComponents();

        // Initializie the song functions
        if( mq == null ) {
            mq = new MusicQueuer(this);
            mq.readSongs();
            mq.readAlbums();

            arr = mq.getEntireSongList();
        }

        flashbackPlayer = new FlashbackPlayer(arr,this, mq);


        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                MainActivity.getAddressRetriver().setLocation(location);
                Log.d("FBLgetting location", "Setting the location to address retriver");
                Log.d( " line 114", flashbackPlayer.toString());

                mq.makeVibeList();
                //flashbackPlayer.loadList();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        /// Register the listener with the Location Manager to receive location updates
        try {
            if( flashBackIsPlaying) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
            }
        } catch( SecurityException e){

        }

        launchRegularMode = findViewById(R.id.regular_button);
        songTitleDisplay = findViewById(R.id.song_title);
        songDateDisplay = findViewById(R.id.song_date);
        songLocationDisplay = findViewById(R.id.song_location);
        songTimeDisplay = findViewById(R.id.song_time);
        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        nextButton = findViewById(R.id.next_button);
        prevButton =  findViewById(R.id.previous_button);
        songArtistDisplay = findViewById(R.id.artist_title);
        songAlbumDisplay = findViewById(R.id.album_title);



        // Thread behind the scenes to update com.gaparmar.mediaflashback.UI
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
        flashBackIsPlaying = false;
        locationManager.removeUpdates(locationListener);
        StorageHandler.storeLastMode(VibeActivity.this, 0);
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
        System.err.println("FA 225 next button clicked");
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
         * Updates the displayed track information on the com.gaparmar.mediaflashback.UI
         * @param currentSong The song that is currently playing
         */
    public void updateTrackInfo(Song currentSong) {
        Log.d("UINormal", "Reset displayed information of the song to the current song");
        if( !flashBackIsPlaying ) return;
        ArrayList<String> songInfo = mq.getSongInfo(flashbackPlayer.getCurrentSongFileName());
        songTitleDisplay.setText( songInfo.get(TITLE_POS));
        songDateDisplay.setText( songInfo.get(TIME_POS) + " at " + songInfo.get(DAY_POS));
        songLocationDisplay.setText( songInfo.get(LOC_POS));
        songAlbumDisplay.setText(songInfo.get(ALBUM_POS));
        songArtistDisplay.setText(songInfo.get(ARTIST_POS));
    }


    /**
     * Adds the given song to the Upcoming Songs in the vibe mode
     * @param song the song name to be added
     */
    public void appendUpcomingSong(String song){
        LinearLayout upcoming = findViewById(R.id.upcoming_songs);

        LinearLayout.LayoutParams temp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        temp.setMargins(100,0,0,0);
        TextView t = new TextView(this);
        t.setText(song);
        t.setLayoutParams(temp);
        t.setTextColor(Color.parseColor("#c4d7f2"));
        t.setTextSize(20);
        upcoming.addView(t);
    }

}
