package com.gaparmar.mediaflashback;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.gaparmar.mediaflashback.Song.state.DISLIKED;
import static com.gaparmar.mediaflashback.Song.state.LIKED;
import static com.gaparmar.mediaflashback.Song.state.NEITHER;

/**
 * Created by lxyzh on 2/17/2018.
 */

public class UIHandler {

    private final String NEUTRAL = "Neutral";
    private final String LIKE = "Liked";
    private final String DISLIKE = "Disliked";
    private final String ERROR_STATE = "Error";

    // All the buttons and views on the MainActivity
    Context context;
    private TextView songTitleDisplay;
    private TextView songLocationDisplay;
    private TextView songDateDisplay;
    private TextView songTimeDisplay;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageButton toggleBtn;
    MusicQueuer musicQueuer;
    MusicPlayer musicPlayer;

    public final static int TITLE_POS = 0;
    public final static int DATE_POS = 1;
    public final static int DURATION_POS = 2;
    public final static int LOC_POS = 3;

    // Initilize everything so we can actually use it
    public UIHandler( Context context ){
        this.context = context;
        musicQueuer = MainActivity.getMusicQueuer();
        musicPlayer = MainActivity.getMusicPlayer();
        songTitleDisplay = (TextView) ((Activity)context).findViewById(R.id.song_title);
        songLocationDisplay = (TextView) ((Activity)context).findViewById(R.id.song_location);
        songDateDisplay = (TextView) ((Activity)context).findViewById(R.id.song_date);
        songTimeDisplay = (TextView) ((Activity)context).findViewById(R.id.song_time);

        playButton =  (ImageButton) ((Activity)context).findViewById(R.id.play_button);
        pauseButton = (ImageButton) ((Activity)context).findViewById(R.id.pause_button);
        nextButton = (ImageButton) ((Activity)context).findViewById(R.id.next_button);
        prevButton = (ImageButton) ((Activity)context).findViewById(R.id.previous_button);
        toggleBtn = (ImageButton) ((Activity)context).findViewById(R.id.toggleBtn);

        toggleBtn.setImageResource(R.drawable.neutral);
        toggleBtn.setTag(NEUTRAL);

    }

    /**
     * Link all the buttons with listeners
     */
    public void setButtonFunctions() {
        // Set the button's functions
        playButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dont't do anything if no song is currently selected
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;

                }catch(NullPointerException e){
                    return;
                }

                musicPlayer.playSong();
                updateTrackInfo();
                setButtonsPlaying();
            }
        });

        pauseButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view ) {
                musicPlayer.pauseSong();
                setButtonsPausing();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;
                }catch(NullPointerException e){
                    return;
                }

                musicPlayer.nextSong();
                // Dont't do anything if no song is currently selected

                // Load all the information about the song
                updateTrackInfo();
                setButtonsPlaying();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;
                }catch (NullPointerException e){
                    return;
                }
                musicPlayer.previousSong();
                // Dont't do anything if no song is currently selected

                // Load all the information about the song
                updateTrackInfo();
                setButtonsPlaying();
            }
        });

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toggleState = toggleBtn.getTag().toString();
                try {
                    if (musicPlayer.getCurrSong() == null)
                        return;
                }catch(NullPointerException e){
                    toggleState = ERROR_STATE;
                }

                switch (toggleState) {
                    // switch from neutral to like
                    case NEUTRAL:
                        toggleBtn.setImageResource(R.drawable.like);
                        toggleBtn.setTag(LIKE);

                        if (musicPlayer.getCurrSong() != null) {
                            musicPlayer.getCurrSong().setCurrentState(LIKED);
                        }

                        Toast likeToast = Toast.makeText(context, LIKE, Toast.LENGTH_SHORT);
                        likeToast.show();
                        break;

                    // switch from like to dislike
                    case LIKE:
                        toggleBtn.setImageResource(R.drawable.unlike);
                        toggleBtn.setTag(DISLIKE);

                        if (musicPlayer.getCurrSong() != null) {
                            musicPlayer.getCurrSong().setCurrentState(DISLIKED);
                        }

                        Toast dislikeToast = Toast.makeText(context, DISLIKE, Toast.LENGTH_SHORT);
                        dislikeToast.show();
                        break;

                    // switch from dislike to neutral
                    case DISLIKE:
                        toggleBtn.setImageResource(R.drawable.neutral);
                        toggleBtn.setTag(NEUTRAL);

                        if (musicPlayer.getCurrSong() != null) {
                            musicPlayer.getCurrSong().setCurrentState(NEITHER);
                        }

                        Toast neutralToast = Toast.makeText(context, NEUTRAL, Toast.LENGTH_SHORT);
                        neutralToast.show();
                        break;
                    case ERROR_STATE:
                        break;
                }
/*
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
                songTitleDisplay.setText( currentSong.getTitle());
                songDateDisplay.setText( Integer.toString( currentSong.getTimeLastPlayed()));
                songLocationDisplay.setText("" + currentSong.getLocation());
                songTimeDisplay.setText( Integer.toString( currentSong.getLengthInSeconds() ));
*/
            }
        });
    }

    /**
     * Grab information about the song that's playing right now and display on UI
     */
    public void updateTrackInfo() {
        ArrayList<String> songInfo = musicQueuer.getSongInfo(musicPlayer.getCurrentSongId());
        songTitleDisplay.setText( songInfo.get(TITLE_POS));
        songDateDisplay.setText( songInfo.get(DATE_POS));
        songLocationDisplay.setText( songInfo.get(LOC_POS));
        songTimeDisplay.setText( songInfo.get(DURATION_POS));
    }

    /**
     * Grab information about the song that's passed in and display on UI
     * @param song the song object passed in
     */
    public void updateTrackInfo( Song song ) {
        if ( song != null ) {
            songTitleDisplay.setText(song.getTitle());
            songDateDisplay.setText(Integer.toString(song.getTimeLastPlayed()));
            // songLocationDisplay.setText( songInfo.get(LOC_POS));
            songTimeDisplay.setText(Double.toString(song.getLengthInSeconds()));
        }
        else {
            songTitleDisplay.setText("NONE");
            songDateDisplay.setText("NONE");
            // songLocationDisplay.setText( songInfo.get(LOC_POS));
            songTimeDisplay.setText("NONE");
        }
    }

    /**
     * Hide play button and show pause button
     */
    public void setButtonsPlaying() {
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    /**
     * Hide pause button and show play button
     */
    public void setButtonsPausing() {
        playButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }
}
