package com.gaparmar.mediaflashback;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Song s;
    ArrayList<Song> arr = new ArrayList<Song>();

    // This is all the fields on the main screen
    TextView songTitleDisplay;
    TextView songLocationDisplay;
    TextView songDateDisplay;
    TextView songTimeDisplay;
    ImageButton playButton;
    ImageButton pauseButton;
    ImageButton nextButton;
    ImageButton prevButton;

    MusicPlayer musicPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize all the fields
        songTitleDisplay = (TextView) findViewById(R.id.song_title);
        songDateDisplay = (TextView) findViewById(R.id.song_date);
        songLocationDisplay = (TextView) findViewById(R.id.song_location);
        songTimeDisplay = (TextView) findViewById(R.id.song_time);
        playButton = (ImageButton) findViewById(R.id.play_button);
        pauseButton = (ImageButton) findViewById(R.id.pause_button);
        nextButton = (ImageButton) findViewById(R.id.next_button);
        prevButton = (ImageButton) findViewById(R.id.previous_button);

        MusicQueuer mq = new MusicQueuer();
        mq.readSongs(this);
        musicPlayer = new MusicPlayer(this);

        // Unless there is a song playing when we get back to normal mode, hide the button
        if( !musicPlayer.wasPlayingSong()) {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }
        else {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        }

        /*
         * This is a test
         */
        int songOne= R.raw.replay;
        int songTwo = R.raw.jazz_in_paris;
        final Song songLonger = new Song( "At Afternppn", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songTwo);
        final Song songShorter = new Song( "At Midnight", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songOne);

        // Make a list of songs
        ArrayList<Song> list = new ArrayList<>();
        list.add( songLonger );
        list.add( songShorter );
        musicPlayer = new MusicPlayer(list, this);
        //  musicPlayer = new MusicPlayer(this);
        musicPlayer.loadMedia( songLonger.getRawID() );

        /*
         * This is the end of the test
         */

        // Set the button's functions
        playButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayer.playSong();
                // Replace the buttons
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);

                // Load all the information about the song
                songTitleDisplay.setText( musicPlayer.getCurrSong().getTitle());
                songDateDisplay.setText( Integer.toString( musicPlayer.getCurrSong().getTimeLastPlayed()));
                songLocationDisplay.setText( musicPlayer.getCurrSong().getLocation());
                songTimeDisplay.setText( Integer.toString( musicPlayer.getCurrSong().getLengthInSeconds() ));

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

                // Load all the information about the song
                songTitleDisplay.setText( currentSong.getTitle());
                songDateDisplay.setText( Integer.toString( currentSong.getTimeLastPlayed()));
                songLocationDisplay.setText( currentSong.getLocation());
                songTimeDisplay.setText( Integer.toString( currentSong.getLengthInSeconds() ));
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayer.previousSong();
                Song currentSong = musicPlayer.getCurrSong();

                // Load all the information about the song
                songTitleDisplay.setText( currentSong.getTitle());
                songDateDisplay.setText( Integer.toString( currentSong.getTimeLastPlayed()));
                songLocationDisplay.setText( currentSong.getLocation());
                songTimeDisplay.setText( Integer.toString( currentSong.getLengthInSeconds() ));
            }
        });

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
                launchActivity();
            }
        });
    }

    public void launchActivity(){
        //input = (EditText)findViewById(R.id.in_time) ;
        Intent intent = new Intent(this, FlashbackActivity.class);
        //intent.putExtra("transferred_string", input.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    public void launchLibrary() {
        Intent intent = new Intent(this, Library.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

}
