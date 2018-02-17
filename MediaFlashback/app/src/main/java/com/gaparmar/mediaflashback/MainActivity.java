package com.gaparmar.mediaflashback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Constants
    public final static int TITLE_POS = 0;
    public final static int LOC_POS = 1;
    public final static int DATE_POS = 2;
    public final static int DURATION_POS = 3;

    // This is all the fields on the main screen
    private TextView songTitleDisplay;
    private TextView songLocationDisplay;
    private TextView songDateDisplay;
    private TextView songTimeDisplay;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;

    private static MusicPlayer musicPlayer;
    private static MusicQueuer musicQueuer;

    public static MusicPlayer getMusicPlayer(){
        return musicPlayer;
    }
    public static MusicQueuer getMusicQueuer() { return musicQueuer; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songTitleDisplay = findViewById(R.id.song_title);
        songDateDisplay = findViewById(R.id.song_date);
        songLocationDisplay = findViewById(R.id.song_location);
        songTimeDisplay = findViewById(R.id.song_time);
        playButton =  findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.previous_button);

        musicQueuer = new MusicQueuer(this);
        musicQueuer.readSongs();
        musicQueuer.readAlbums();

        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer(this, musicQueuer);
        }

        // Unless there is a song playing when we get back to normal mode, hide the button
        if( !musicPlayer.wasPlayingSong()) {
            enableButton(playButton);
            disableButton(pauseButton);
        }
        else {
            disableButton( playButton );
            enableButton( pauseButton );
        }

        // Location of Brennan Hall
        final double[] currLocation ={32.882988, -117.232886};
        // Location of Geisel
        final double[] destLocation = {32.881535, -117.237493};

        // Set the button's functions
        Song test = new Song("", "", "", 0, 0, 0, currLocation);
        playButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songLocationDisplay.setText(""+Song.calculateDist(destLocation, currLocation));
                // Dont't do anything if no song is currently selected
                if (musicPlayer.getCurrSong() == null)
                    return;

                musicPlayer.playSong();
                updateTrackInfo();
            }
        });

        pauseButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view ) {
                musicPlayer.pauseSong();
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayer.nextSong();
                Song currentSong = musicPlayer.getCurrSong();
                // Dont't do anything if no song is currently selected
                if (currentSong == null)
                    return;
                // Load all the information about the song
                songTitleDisplay.setText( currentSong.getTitle());
                songDateDisplay.setText( Integer.toString( currentSong.getTimeLastPlayed()));
                songLocationDisplay.setText( "" + currentSong.getLocation());
                songTimeDisplay.setText( Integer.toString( currentSong.getLengthInSeconds() ));
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayer.previousSong();
                Song currentSong = musicPlayer.getCurrSong();
                if (currentSong == null)
                    return;
                // Load all the information about the song
               updateTrackInfo();
            }
        });

        if (musicPlayer.isPlaying()) {
            updateTrackInfo();
        }

        //mPlayer.loadMedia(R.raw.replay);
        Button launchFlashbackActivity = (Button) findViewById(R.id.flashback_button);
        ImageButton playButton = (ImageButton) findViewById(R.id.play_button);
        Button browseBtn = (Button) findViewById(R.id.browse_button);
        browseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick( View view ){
                launchLibrary();
                finish();
            }
        });

        launchFlashbackActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick( View view ){
                musicPlayer.resetSong();
                StorageHandler.storeLastMode(MainActivity.this, 1);
                launchActivity();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        if(StorageHandler.getLastMode(this) == 1){
            launchActivity();
        }
    }

    public void launchActivity(){
        //input = (EditText)findViewById(R.id.in_time) ;
        Intent intent = new Intent(this, FlashbackActivity.class);
        //intent.putExtra("transferred_string", input.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    public void launchLibrary() {
        Intent intent = new Intent(this, LibraryActivity.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    public void updateTrackInfo() {
        // Replace the buttons
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);

        // Load all the information about the song
       /* songTitleDisplay.setText( musicPlayer.getCurrSong().getTitle());
        songDateDisplay.setText( Integer.toString( musicPlayer.getCurrSong().getTimeLastPlayed()));
        songLocationDisplay.setText( musicPlayer.getCurrSong().getLocation().toString());
        songTimeDisplay.setText( Integer.toString( musicPlayer.getCurrSong().getLengthInSeconds() ));*/
        ArrayList<String> songInfo = musicQueuer.getSongInfo(musicPlayer.getCurrentSongId());
        songTitleDisplay.setText( songInfo.get(TITLE_POS));
        songDateDisplay.setText( songInfo.get(DATE_POS));
        songLocationDisplay.setText( songInfo.get(LOC_POS));
        songTimeDisplay.setText( songInfo.get(DURATION_POS));
    }

    public void disableButton( ImageButton button ) {
        button.setVisibility(View.GONE);
    }

    public void enableButton( ImageButton button ) {
        button.setVisibility(View.VISIBLE);
    }

   /* @Override
    public void onResume() {
        super.onResume();

        final String sender = this.getIntent().getExtras().getString("SENDER_KEY");

        if( sender != null ){
            this.receiveData();

        }
    }*/
}
