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

    final String NEUTRAL = "Neutral";
    final String LIKE = "Liked";
    final String DISLIKE = "Disliked";
    final String ERROR_STATE = "Error";
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

    // Initilize everything so we can actually use it
    public UINormal( Context context ){
        this.context = context;
        songTitleDisplay = (TextView) ((Activity)context).findViewById(R.id.song_title);
        songLocationDisplay = (TextView) ((Activity)context).findViewById(R.id.song_location);
        songDateDisplay = (TextView) ((Activity)context).findViewById(R.id.song_date);
        songTimeDisplay = (TextView) ((Activity)context).findViewById(R.id.song_time);
        songArtistDisplay = (TextView) ((Activity)context).findViewById(R.id.artist_title);
        songAlbumDisplay = (TextView) ((Activity)context).findViewById(R.id.album_title);
       // musicQueuer = MainActivity.getMusicQueuer();
       // musicPlayer = MainActivity.getMusicPlayer();
        musicDownloader = MainActivity.getMusicDownloader();
        musicQueuer = BackgroundService.getMusicQueuer();
        musicPlayer = BackgroundService.getMusicPlayer();
        this.context = context;

        playButton =  ((Activity)context).findViewById(R.id.play_button);
        pauseButton =((Activity)context).findViewById(R.id.pause_button);
        nextButton = ((Activity)context).findViewById(R.id.next_button);
        prevButton = ((Activity)context).findViewById(R.id.previous_button);
        toggleBtn = ((Activity)context).findViewById(R.id.toggleBtn);
//        inputURL = ((Activity)context).findViewById(R.id.inputURL);
//        downloadBtn = ((Activity)context).findViewById(R.id.downloadBtn);

        toggleBtn.setImageResource(R.drawable.neutral);
        toggleBtn.setTag(NEUTRAL);

        handler = new android.os.Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                if( musicPlayer != null ) {
                    // Unless there is a song playing when we get back to normal mode, hide the button
                    if (!musicPlayer.isPlaying()) {
                        playButton.setVisibility(View.VISIBLE);
                        pauseButton.setVisibility(View.GONE);
                    } else {
                       // updateTrackInfo();
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
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;
                }  catch(NullPointerException e){
                    toggleState = ERROR_STATE;
                }

                switch (toggleState) {
                    // switch from neutral to like
                    case NEUTRAL:
                        toggleBtn.setImageResource(R.drawable.like);
                        toggleBtn.setTag(LIKE);

                        if (musicPlayer.getCurrSong() != null) {
                            //musicPlayer.getCurrSong().setCurrentState(1);
                            FirebaseHandler.storeRate(musicPlayer.getCurrentSongFileName(), Constant.LIKED);
                        }

                        Toast likeToast = Toast.makeText(context, LIKE, Toast.LENGTH_SHORT);
                        likeToast.show();
                        FirebaseHandler.storeRate(musicPlayer.getCurrentSongFileName(), Constant.LIKED);
                        break;

                    // switch from like to dislike
                    case LIKE:
                        toggleBtn.setImageResource(R.drawable.unlike);
                        toggleBtn.setTag(DISLIKE);

                        if (musicPlayer.getCurrSong() != null) {
                            //musicPlayer.getCurrSong().setCurrentState(-1);
                            FirebaseHandler.storeRate(musicPlayer.getCurrentSongFileName(), Constant.DISPLIKED);
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
                            FirebaseHandler.storeRate(musicPlayer.getCurrentSongFileName(), Constant.NEUTRAL);
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
    }

    /**
     * Update toggle button
     */
    public void updateToggle(){
        //firebaseHandler.getField(musicPlayer.getCurrentSongFileName(), Constant.RATE_FIELD);
       // updateToggle();
    }

    /************* Observer that listens in for the changes ********************/
    public void updateLocation( String filename, String locationString ){
        songLocationDisplay.setText( locationString);
    }

    public void updateDayOfWeek( String filename, String dayOfWeek ){
        songTimeDisplay.setText( timeOfDay + " on " + dayOfWeek );
    }

    public void updateUserName( String filename, String userName ) {
        songDateDisplay.setText(userName);
    }

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

    public void updateCoord( String filename, double lat, double lon ){}

    public void updateTimeStamp(String filename, long timeStamp ){}

    public void updateRate(String filename, long rate){
        Log.d("UINormal:updateRate", "Updating state to " + rate );
        this.setButtonToggle(context, (int)rate);
    }

    public void updateProb(String filename, int prob){}
    public void updateLogList(String filename, ArrayList<LogInstance> list){}
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
            case 1:
                toggleBtn.setImageResource(R.drawable.like);
                toggleBtn.setTag(LIKE);
                break;
            case 0:
                toggleBtn.setImageResource(R.drawable.neutral);
                toggleBtn.setTag(NEUTRAL);
                break;
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

    public void setQueuerPlayer( MusicPlayer mp, MusicQueuer mq) {
        Log.d("UINormal", "set player and queuer");
        musicPlayer = mp;
        musicQueuer = mq;
    }
}
