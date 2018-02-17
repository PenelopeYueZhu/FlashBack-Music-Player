package com.gaparmar.mediaflashback;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class FlashbackActivity extends AppCompatActivity {
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
    Button launchRegularMode;

    FlashbackPlayer flashbackPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashback);

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

        // Unless there is a song playing when we get back to normal mode, hide the button
        if( !flashbackPlayer.wasPlayingSong()) {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }
        else {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        }

        launchRegularMode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                flashbackPlayer.resetSong();
                StorageHandler.storeLastMode(FlashbackActivity.this, 0);
                finish();
            }
        });

        playButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashbackPlayer.playSong();
                // Replace the buttons
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);

                // Load all the information about the song
                songTitleDisplay.setText( flashbackPlayer.getCurrSong().getTitle());
                songDateDisplay.setText( Integer.toString( flashbackPlayer.getCurrSong().getTimeLastPlayed()));
                songLocationDisplay.setText( "" + flashbackPlayer.getCurrSong().getLocation());
                songTimeDisplay.setText( Integer.toString( flashbackPlayer.getCurrSong().getLengthInSeconds() ));

            }
        });

        pauseButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view ) {
                flashbackPlayer.pauseSong();
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashbackPlayer.nextSong();
                Song currentSong = flashbackPlayer.getCurrSong();

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
                flashbackPlayer.previousSong();
                Song currentSong = flashbackPlayer.getCurrSong();

                // Load all the information about the song
                songTitleDisplay.setText( currentSong.getTitle());
                songDateDisplay.setText( Integer.toString( currentSong.getTimeLastPlayed()));
                songLocationDisplay.setText("" +  currentSong.getLocation());
                songTimeDisplay.setText( Integer.toString( currentSong.getLengthInSeconds() ));
            }
        });

        int songOne = R.raw.bleed;
        int songTwo = R.raw.tightrope_walker;
        int songThree = R.raw.tightrope_walker;
        int songFour = R.raw.after_the_storm;
        int songFive = R.raw.america_religious;
        final Song s5 = new Song( "Bleed", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songOne, new double[]{34, -117});
        final Song s2 = new Song( "Jazz in Paris", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songTwo,null);
        final Song s3 = new Song( "Tightrope Walker", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songThree,null);
        final Song s4 = new Song( "After the Storm", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songFour,null);
        final Song s1 = new Song( "America Religious", "I Will Not Be Afraid", "Unknown Artist",
                0, 0, songFive,null);

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

        double[] tmp = s5.getLocation();
        s5.setLocation(tmp);
        flashbackPlayer = new FlashbackPlayer(list, this);
        flashbackPlayer.loadMedia( s5.getResID() );

        /*Button launchRegularActivity = (Button) findViewById(R.id.regular_button);
        ImageButton playButton = (ImageButton) findViewById(R.id.play_button);

        launchRegularActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick( View view ){
                launchActivity();
            }
        });*/
    }

    public void launchActivity(){
        //input = (EditText)findViewById(R.id.in_time) ;
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("transferred_string", input.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }


}
