package com.gaparmar.mediaflashback;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by lxyzh on 2/17/2018.
 */

public class UIHandler {

    final String NEUTRAL = "Neutral";
    final String LIKE = "Liked";
    final String DISLIKE = "Disliked";
    final String ERROR_STATE = "Error";
    final String INIT_INFO = "NONE";

    // All the buttons and views on the MainActivity
    Context context;
    TextView songTitleDisplay;
    TextView songLocationDisplay;
    TextView songDateDisplay;
    TextView songTimeDisplay;
    TextView songArtistDisplay;
    TextView songAlbumDisplay;

    public UIHandler(){}
    // Initilize everything so we can actually use it
    public UIHandler( Context context ){
        this.context = context;
        songTitleDisplay = (TextView) ((Activity)context).findViewById(R.id.song_title);
        songLocationDisplay = (TextView) ((Activity)context).findViewById(R.id.song_location);
        songDateDisplay = (TextView) ((Activity)context).findViewById(R.id.song_date);
        songTimeDisplay = (TextView) ((Activity)context).findViewById(R.id.song_time);
        songArtistDisplay = (TextView) ((Activity)context).findViewById(R.id.artist_title);
        songAlbumDisplay = (TextView) ((Activity)context).findViewById(R.id.album_title);
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
            songAlbumDisplay.setText(song.getParentAlbum());
            songArtistDisplay.setText(song.getArtistName());
        }
        else {
            songTitleDisplay.setText("NONE");
            songDateDisplay.setText("NONE");
            // songLocationDisplay.setText( songInfo.get(LOC_POS));
            songTimeDisplay.setText("NONE");
            songAlbumDisplay.setText("NONE");
            songArtistDisplay.setText("NONE");
        }
    }
}
