package com.gaparmar.mediaflashback.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gaparmar.mediaflashback.Constant;
import com.gaparmar.mediaflashback.DataStorage.FirebaseHandler;
import com.gaparmar.mediaflashback.DataStorage.FirebaseObserver;
import com.gaparmar.mediaflashback.DataStorage.LogInstance;
import com.gaparmar.mediaflashback.MusicDownloader;
import com.gaparmar.mediaflashback.MusicPlayer;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.R;
import com.gaparmar.mediaflashback.DataStorage.StorageHandler;
import com.gaparmar.mediaflashback.Song;

import java.util.ArrayList;

/**
 * Created by lxyzh on 2/17/2018.
 */

public class UINormal implements FirebaseObserver {

    final static String NEUTRAL = "Neutral";
    final static String LIKE = "Liked";
    final static String DISLIKE = "Disliked";
    final static String ERROR_STATE = "Error";
    final String INIT_INFO = "NONE";
    String day = "Unknown";

    // All the buttons and views on the MainActivity
    Context context;
    TextView songTitleDisplay;
    TextView songLocationDisplay;
    TextView songDateDisplay;
    TextView songTimeDisplay;
    TextView songArtistDisplay;
    TextView songAlbumDisplay;
    private Handler handler;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageButton toggleBtn;
    private ImageButton downloadBtn;
    private EditText inputURL;

    MusicDownloader musicDownloader;
    private String songURL = "http://soundbible.com/grab.php?id=2190&type=mp3";
    MusicQueuer musicQueuer;
    MusicPlayer musicPlayer;

    public final static int TITLE_POS = 0;
    public final static int LOC_POS = 3;
    public final static int TIME_POS = 4;
    public final static int DAY_POS = 5;
    public final static int USER_POS = 6;

    public final static int ALBUM_POS = 2;
    public final static int ARTIST_POS = 1;

    String timeOfDay;

    /**
     * Initializes all of the necessary variables.
     * @param context The context being used.
     */
    public UINormal( Context context ){
        this.context = context;
        songTitleDisplay = (TextView) ((Activity)context).findViewById(R.id.song_title);
        songLocationDisplay = (TextView) ((Activity)context).findViewById(R.id.song_location);
        songDateDisplay = (TextView) ((Activity)context).findViewById(R.id.song_date);
        songTimeDisplay = (TextView) ((Activity)context).findViewById(R.id.song_time);
        songArtistDisplay = (TextView) ((Activity)context).findViewById(R.id.artist_title);
        songAlbumDisplay = (TextView) ((Activity)context).findViewById(R.id.album_title);
        musicDownloader = MainActivity.getMusicDownloader();
        musicQueuer = BackgroundService.getMusicQueuer();
        musicPlayer = BackgroundService.getMusicPlayer();
        this.context = context;

        playButton =  ((Activity)context).findViewById(R.id.play_button);
        pauseButton =((Activity)context).findViewById(R.id.pause_button);
        nextButton = ((Activity)context).findViewById(R.id.next_button);
        prevButton = ((Activity)context).findViewById(R.id.previous_button);
        toggleBtn = ((Activity)context).findViewById(R.id.toggleBtn);

        toggleBtn.setImageResource(R.drawable.neutral);
        toggleBtn.setTag(NEUTRAL);

        handler = new android.os.Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                if( musicPlayer != null ) {
                    // Updates the visible buttons based on current song condition
                    if (!musicPlayer.isPlaying()) {
                        playButton.setVisibility(View.VISIBLE);
                        pauseButton.setVisibility(View.GONE);
                    } else {
                        updateUI();
                        playButton.setVisibility(View.GONE);
                        pauseButton.setVisibility(View.VISIBLE);
                    }
                    handler.postDelayed(this, 500);
                }
            }
        });

    }

    /**
     * Link all the buttons with listeners
     */
    public void setButtonFunctions() {
        // Set the button's functions

        // Plays the song and updates the com.gaparmar.mediaflashback.UI when the play button is pressed
        playButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UINomarl", "playbutton clicked");
                // Dont't do anything if no song is currently selected
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;

                }catch(NullPointerException e){
                    return;
                }

                // Plays the song and updates the UI information
                musicPlayer.playSong();
                updateUI();
                setButtonsPlaying();
            }

        });

        // Pauses the song and updates the com.gaparmar.mediaflashback.UI when the pause button is pressed
        pauseButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view ) {
                Log.d("UINomarl", "pausebutton clicked");
                musicPlayer.pauseSong();
                setButtonsPausing();
            }
        });

        // Skips to the next song and updates the com.gaparmar.mediaflashback.UI when the next button is pressed
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UINomarl", "nextbutton clicked");
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;
                }catch(NullPointerException e){
                    return;
                }

                musicPlayer.nextSong();
                updateToggle();
                // Dont't do anything if no song is currently selected

                // Load all the information about the song
                updateUI();
                setButtonsPlaying();
            }
        });

        // Goes to previous song and updates com.gaparmar.mediaflashback.UI when previous button is pressed
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UINomarl", "prevbutton clicked");
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;
                }catch (NullPointerException e){
                    return;
                }
                musicPlayer.previousSong();
                updateToggle();
                // Dont't do anything if no song is currently selected

                // Load all the information about the song
                updateUI();
                updateToggle();
                setButtonsPlaying();
            }
        });



        // Switches the liked button and changes the state of the song
        // when clicked.
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UINomarl", "togglebutton clicked");
                String toggleState = toggleBtn.getTag().toString();
                // Checks the state of the current song
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;
                }  catch(NullPointerException e){
                    toggleState = ERROR_STATE;
                }

                // Checks the liked state of the current song
                switch (toggleState) {
                    // switch from neutral to like
                    case NEUTRAL:
                        toggleBtn.setImageResource(R.drawable.like);
                        toggleBtn.setTag(LIKE);

                        if (musicPlayer.getCurrSong() != null) {
                            //musicPlayer.getCurrSong().setCurrentState(1);
                            StorageHandler.storeSongState(context, musicPlayer.getCurrentSongFileName(), 1);
                        }

                        Toast likeToast = Toast.makeText(context, LIKE, Toast.LENGTH_SHORT);
                        likeToast.show();
                        break;

                    // switch from like to dislike
                    case LIKE:
                        toggleBtn.setImageResource(R.drawable.unlike);
                        toggleBtn.setTag(DISLIKE);

                        if (musicPlayer.getCurrSong() != null) {
                            //musicPlayer.getCurrSong().setCurrentState(-1);
                            StorageHandler.storeSongState(context, musicPlayer.getCurrentSongFileName(), -1);
                        }

                        Toast dislikeToast = Toast.makeText(context, DISLIKE, Toast.LENGTH_SHORT);
                        dislikeToast.show();
                        break;

                    // switch from dislike to neutral
                    case DISLIKE:
                        toggleBtn.setImageResource(R.drawable.neutral);
                        toggleBtn.setTag(NEUTRAL);

                        if (musicPlayer.getCurrSong() != null) {
                            //musicPlayer.getCurrSong().setCurrentState(0);
                            StorageHandler.storeSongState(context, musicPlayer.getCurrentSongFileName(), 0);
                        }

                        Toast neutralToast = Toast.makeText(context, NEUTRAL, Toast.LENGTH_SHORT);
                        neutralToast.show();
                        break;
                    case ERROR_STATE:
                        break;
                }
            }
        });


    }

    /**
     * Update all the information about the song when it is playing
     */
    public void updateUI() {
        Log.d("UINormal", "Reset displayed information of the song to the current song");
        ArrayList<String> songInfo = musicQueuer.getSongInfo(musicPlayer.getCurrentSongFileName());
        songTitleDisplay.setText( songInfo.get(TITLE_POS));
        songAlbumDisplay.setText(songInfo.get(ALBUM_POS));
        songArtistDisplay.setText(songInfo.get(ARTIST_POS));
        FirebaseHandler.getField(musicPlayer.getCurrentSongFileName(), Constant.TIME_FIELD);
        FirebaseHandler.getField(musicPlayer.getCurrentSongFileName(), Constant.ADDRESS_FIELD);
        FirebaseHandler.getField(musicPlayer.getCurrentSongFileName(), Constant.WEEKDAY_FIELD);
        FirebaseHandler.getField(musicPlayer.getCurrentSongFileName(), Constant.USER_FIELD);
        setButtonToggle(context, StorageHandler.getSongState(context, musicPlayer.getCurrentSongFileName()));
    }

    /**
     * Update toggle button
     */
    public void updateToggle(){
        //firebaseHandler.getField(musicPlayer.getCurrentSongFileName(), Constant.RATE_FIELD);
       // updateToggle();
    }

    /************* Observer that listens in for the changes ********************/

    /**
     * Updates the location of the song.
     * @param filename The filename of the song
     * @param locationString The location to update
     */
    public void updateLocation( String filename, String locationString ){
        songLocationDisplay.setText( locationString);
    }

    /**
     * Updates the day of the week.
     * @param filename The filename of the song
     * @param dayOfWeek The day of the week to update
     */
    public void updateDayOfWeek( String filename, String dayOfWeek ){
        songTimeDisplay.setText( timeOfDay + " on " + dayOfWeek );
    }

    /**
     * Updates the username
     * @param filename The filename of the song
     * @param userName The username to update
     */
    public void updateUserName( String filename, String userName ) {
        songDateDisplay.setText(userName);
    }

    /**
     * Updates the time
     * @param filename The filename of the song
     * @param time The time to update
     */
    public void updateTime( String filename, long time) {
        String timeZone;
        if (time >= Constant.MORNING_DIVIDER && time < Constant.NOON_DIVIVER) {
            // 5 AM - 11 AM
            timeZone= Constant.MORNING;
        } else if (time >= Constant.NOON_DIVIVER && time < Constant.EVENING_DIVIDER) {
            // 11 AM - 5 PM
            timeZone= Constant.AFTERNOON;
        } else if (time >= Constant.EVENING_DIVIDER || time < Constant.MORNING_DIVIDER ){
            // 5 PM - 5 AM
            timeZone = Constant.EVENING;
        } else {
            // Unknown
            timeZone = Constant.UNKNOWN;
        }

        timeOfDay = timeZone;
    }

    /**
     * Updates the coordinates
     * @param filename The filename of the song
     * @param lat The latitude to update
     * @param lon The longitude to update
     */
    public void updateCoord( String filename, double lat, double lon ){}

    /**
     * Updates the timestamp
     * @param filename The filename of the song
     * @param timeStamp The timestamp to update
     */
    public void updateTimeStamp(String filename, long timeStamp ){}

    /**
     * Updates the rating of the song
     * @param filename The filename of the song
     * @param rate The rating to update
     */
    public void updateRate(String filename, long rate){
        Log.d("UINormal:updateRate", "Updating state to " + rate );
        this.setButtonToggle(context, (int)rate);
    }

    /**
     * Updates the probability
     * @param filename The filename of the song
     * @param prob The probability to update
     */
    public void updateProb(String filename, int prob){}

    /**
     * Updates the log list of the song
     * @param filename The filename of the song
     * @param list The list to update
     */
    public void updateLogList(String filename, ArrayList<LogInstance> list){}

    /**
     * Updates the song list
     * @param songList The song list to update
     */
    public void updateSongList( ArrayList<String> songList ){}

    /******************************** end of observer listners ****************************/

    /**
     * Change the buttons according to the song's state
     * @param context the calling context of the song
     * @param rate 1 - liked
     *             0 - neutral
     *             -1 disliked
     */
    public void setButtonToggle(Context context, int rate)
    {
        switch(rate)
        {
            // Liked
            case 1:
                toggleBtn.setImageResource(R.drawable.like);
                toggleBtn.setTag(LIKE);
                break;
            // Neutral
            case 0:
                toggleBtn.setImageResource(R.drawable.neutral);
                toggleBtn.setTag(NEUTRAL);
                break;
            // Disliked
            case -1:
                toggleBtn.setImageResource(R.drawable.unlike);
                toggleBtn.setTag(DISLIKE);
                break;
        }
    }

    /**
     * Hide play button and show pause button
     */
    public void setButtonsPlaying() {
        Log.d("UINormal", "set button playing");
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    /**
     * Hide pause button and show play button
     */
    public void setButtonsPausing() {
        Log.d("UINormal", "set Button Pauseing");
        playButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }

    /**
     * Sets the queuer player
     * @param mp The musicPlayer to set
     * @param mq The musicQueuer to set
     */
    public void setQueuerPlayer( MusicPlayer mp, MusicQueuer mq) {
        Log.d("UINormal", "set player and queuer");
        musicPlayer = mp;
        musicQueuer = mq;
    }
}
