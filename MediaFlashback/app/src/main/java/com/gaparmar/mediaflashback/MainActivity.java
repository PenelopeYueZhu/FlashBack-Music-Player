package com.gaparmar.mediaflashback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Song s;
    private Song song1 = new Song("Jazz in Paris", "Jazz",
            "Jazzy Artist", 100, 1998, R.raw.jazz_in_paris);
    private Song song2 = new Song("Replay", "Replay Album", "Jay Park", 1000, 2016, R.raw.replay);
    ArrayList<Song> arr = new ArrayList<Song>();

    ImageButton playButton;
    ImageButton pauseButton;
    MusicPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // tester songs
        arr.add(song1);
        arr.add(song2);

        mPlayer = new MusicPlayer(arr);

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
