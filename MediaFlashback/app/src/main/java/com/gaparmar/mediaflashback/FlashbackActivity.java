package com.gaparmar.mediaflashback;

import android.content.SharedPreferences;
import android.location.LocationManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class FlashbackActivity extends AppCompatActivity {
    private Song s;
    ArrayList<Song> arr = new ArrayList<>();

    // This is all the fields on the main screen
    TextView songTitleDisplay;
    TextView songLocationDisplay;
    TextView songDateDisplay;
    TextView songTimeDisplay;
    ImageButton playButton;
    ImageButton pauseButton;
    ImageButton nextButton;
    ImageButton prevButton;
    Button launchRegularMode;
    FlashbackPlayer flashbackPlayer;
    MusicQueuer mq;
    UserLocation userLocation;
    DecimalFormat decimalFormat = new DecimalFormat("#.000");

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
        flashbackPlayer = new FlashbackPlayer(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashback);

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

        flashbackPlayer = new FlashbackPlayer(this);

        mq = new MusicQueuer(this);
        mq.readSongs();
        mq.readAlbums();

        initializeViewComponents();

        // Unless there is a song playing when we get back to normal mode, hide the button
        if( !flashbackPlayer.wasPlayingSong()) {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }
        else {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        }

        int songOne = R.raw.back_east;
        int songTwo = R.raw.crane_city;
        int songThree = R.raw.dreamatorium;
        int songFour = R.raw.after_the_storm;
        int songFive = R.raw.hero_hell;
        final Song s5 = new Song( "Back East", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songOne, new double[]{0.0,0.0});

        final Song s2 = new Song( "Jazz in Paris", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songTwo,new double[]{0.0,0.0});
        final Song s3 = new Song( "Tightrope Walker", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songThree,new double[]{0.0,0.0});
        final Song s4 = new Song( "After the Storm", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songFour,new double[]{0.0,0.0});
        final Song s1 = new Song( "America Religious", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songFive,new double[]{0.0,0.0});

        s1.setProbability(1);
        s2.setProbability(4);
        s3.setProbability(7);
        s4.setProbability(13);
        s5.setProbability(55);

        ArrayList<Song> list = new ArrayList<>();
        list.add( s1 );
        list.add( s2 );
        list.add( s3 );
        list.add( s4 );
        list.add( s5 );

        flashbackPlayer = new FlashbackPlayer(list, this);
        flashbackPlayer.loadMedia( s5.getResID() );
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
        flashbackPlayer.playSong();
        // Replace the buttons
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);

        // Load all the information about the song
        songTitleDisplay.setText( flashbackPlayer.getCurrSong().getTitle());
        songDateDisplay.setText( Integer.toString( flashbackPlayer.getCurrSong().getTimeLastPlayed()));
        songLocationDisplay.setText(flashbackPlayer.getCurrSong().getLocationString(this));
        songTimeDisplay.setText( Integer.toString( flashbackPlayer.getCurrSong().getLengthInSeconds() ));
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
    public void launchActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    public void updateTrackInfo(Song currentSong) {
        songTitleDisplay.setText( currentSong.getTitle());
        songDateDisplay.setText( Integer.toString( currentSong.getTimeLastPlayed()));
        songLocationDisplay.setText(flashbackPlayer.getCurrSong().getLocationString(this));
        songTimeDisplay.setText( Integer.toString( currentSong.getLengthInSeconds() ));
    }

}
