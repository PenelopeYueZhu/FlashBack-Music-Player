package com.gaparmar.mediaflashback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.gaparmar.mediaflashback.Song.state.DISLIKED;
import static com.gaparmar.mediaflashback.Song.state.LIKED;
import static com.gaparmar.mediaflashback.Song.state.NEITHER;

public class MainActivity extends AppCompatActivity {

    private final String NEUTRAL = "Neutral";
    private final String LIKE = "Liked";
    private final String DISLIKE = "Disliked";

    // This is all the fields on the main screen
    private TextView songTitleDisplay;
    private TextView songLocationDisplay;
    private TextView songDateDisplay;
    private TextView songTimeDisplay;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageButton toggleBtn;

    private MusicPlayer musicPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize all the fields
        songTitleDisplay = findViewById(R.id.song_title);
        songDateDisplay = findViewById(R.id.song_date);
        songLocationDisplay = findViewById(R.id.song_location);
        songTimeDisplay = findViewById(R.id.song_time);
        playButton =  findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.previous_button);
        toggleBtn = findViewById(R.id.toggleBtn);

        toggleBtn.setImageResource(R.drawable.neutral);
        toggleBtn.setTag(NEUTRAL);

        MusicQueuer musicQueuer = new MusicQueuer(this);
        musicQueuer.readSongs();
        musicPlayer = new MusicPlayer(this, musicQueuer);



        // Unless there is a song playing when we get back to normal mode, hide the button
        if( !musicPlayer.wasPlayingSong()) {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }
        else {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
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
                // Replace the buttons
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);

                // Load all the information about the song
                songTitleDisplay.setText( musicPlayer.getCurrSong().getTitle());
                songDateDisplay.setText( Integer.toString( musicPlayer.getCurrSong().getTimeLastPlayed()));
                songLocationDisplay.setText( "" + musicPlayer.getCurrSong().getLocation());
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

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toggleState = toggleBtn.getTag().toString();
                switch (toggleState) {
                    // switch from neutral to like
                    case NEUTRAL:
                        toggleBtn.setImageResource(R.drawable.like);
                        toggleBtn.setTag(LIKE);

                        if (musicPlayer.getCurrSong() != null) {
                            musicPlayer.getCurrSong().setCurrentState(LIKED);
                        }

                        Toast likeToast = Toast.makeText(MainActivity.this, LIKE, Toast.LENGTH_SHORT);
                        likeToast.show();
                        break;

                    // switch from like to dislike
                    case LIKE:
                        toggleBtn.setImageResource(R.drawable.unlike);
                        toggleBtn.setTag(DISLIKE);

                        if (musicPlayer.getCurrSong() != null) {
                            musicPlayer.getCurrSong().setCurrentState(DISLIKED);
                        }

                        Toast dislikeToast = Toast.makeText(MainActivity.this, DISLIKE, Toast.LENGTH_SHORT);
                        dislikeToast.show();
                        break;

                    // switch from dislike to neutral
                    case DISLIKE:
                        toggleBtn.setImageResource(R.drawable.neutral);
                        toggleBtn.setTag(NEUTRAL);

                        if (musicPlayer.getCurrSong() != null) {
                            musicPlayer.getCurrSong().setCurrentState(NEITHER);
                        }

                        Toast neutralToast = Toast.makeText(MainActivity.this, NEUTRAL, Toast.LENGTH_SHORT);
                        neutralToast.show();
                        break;
                }
/*
                musicPlayer.previousSong();
                Song currentSong = musicPlayer.getCurrSong();
                if (currentSong == null)
                    return;
                // Load all the information about the song
                songTitleDisplay.setText( currentSong.getTitle());
                songDateDisplay.setText( Integer.toString( currentSong.getTimeLastPlayed()));
                songLocationDisplay.setText("" + currentSong.getLocation());
                songTimeDisplay.setText( Integer.toString( currentSong.getLengthInSeconds() ));
*/
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

}
