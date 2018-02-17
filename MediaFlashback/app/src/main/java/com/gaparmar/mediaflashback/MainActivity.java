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
import android.widget.Toast;

import static com.gaparmar.mediaflashback.Song.state.DISLIKED;
import static com.gaparmar.mediaflashback.Song.state.LIKED;
import static com.gaparmar.mediaflashback.Song.state.NEITHER;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static MusicPlayer musicPlayer;
    private static MusicQueuer musicQueuer;
    private static UITracker tracker;

    public static MusicPlayer getMusicPlayer(){
        return musicPlayer;
    }
    public static MusicQueuer getMusicQueuer() { return musicQueuer; }

    public static UITracker getUITracker() {
        return tracker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI
        tracker = new UITracker(this);
        tracker.setButtonFunctions();

        // Initializie the song functions
        if( musicQueuer == null ) {
            musicQueuer = new MusicQueuer(this);
            musicQueuer.readSongs();
            musicQueuer.readAlbums();
        }

        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer(this, musicQueuer);
        }

        // Unless there is a song playing when we get back to normal mode, hide the button
        if( !musicPlayer.wasPlayingSong()) {
            tracker.setButtonsPausing();
        }
        else {
            tracker.setButtonsPlaying();
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

    @Override
    public void onResume() {
        super.onResume();

       if( musicPlayer.isPlaying()) {
           // Update buttons and infos
           tracker.setButtonsPlaying();
           tracker.updateTrackInfo();
       }
    }
}
