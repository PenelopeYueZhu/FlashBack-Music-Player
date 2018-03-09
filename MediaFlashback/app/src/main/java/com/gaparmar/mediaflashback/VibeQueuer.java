package com.gaparmar.mediaflashback;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by lxyzh on 3/5/2018.
 */

public class VibeQueuer extends LibraryManager {

    ArrayList<Song>sortedList = new ArrayList<Song>();

    public VibeQueuer (Context context){
        super(context);
    }
    protected int updateProbablity( String filename ){
        Song track = getSong(filename);
        Log.d("VQ:updateProbability", "Updating song " + track.getTitle());
        int prob = 1;

        final AddressRetriver ar = MainActivity.getAddressRetriver();
        currDate = Calendar.getInstance();

        // Getting the current coordinates
        double currLat = ar.getLatLon()[0];
        double currLon = ar.getLatLon()[1];

        // Get current day
        int day = getIntOfDay( dayFormat.format(currDate.getTime()));

        // Get current hour
        String hour = getTimeOfDay(Integer.parseInt(hourFormat.format(currDate.getTime())));

        // Calculate probability
        boolean isInRange = track.isInRange(new double[]{currLat, currLon});
        boolean isSameDay = getIntOfDay(track.getDayOfWeek()) == day;
        boolean isSameTime = hour.equals(getTimeOfDay( track.getTime()));

        if( isInRange ) prob ++;
        if( isSameTime ) prob ++;
        if( isSameDay ) prob ++;
        if( track.getRate() == Constant.LIKED ) prob ++;
        else if (track.getRate() == Constant.DISPLIKED ) prob = 0;

        track.setProbability( prob );
        //FirebaseHandler.storeProb(filename, prob); TODO:: needs refactoring
        return prob;
    }

    /**
     * Compile a list of songs to play for vibe mode
     */
    protected void makeVibeList(){
        for(String filename : getEntireSongList()){
            Song song = getSong(filename);
            updateProbablity( filename );
            Log.d("VQ:makeVibeList", "Adding songs to the list");
            sortedList.add(song);
        }
        Collections.sort(sortedList, new SongCompare());

        for(String filename : getEntireSongList())
        {
            Song song = getSong(filename);
            Log.d("FBP:makeFlashbackPlaylist","songName "+ song.getTitle());
        }

        for (Song x : sortedList) {
            Log.d("FBP:makeFlashbackPlaylist", "sortedList "+ x.getTitle());
        }
    }

    /**
     * Add songs in album to the list of songs this flashback player plays through
     */
    public void loadPlaylist( FlashbackPlayer mq ) {
        mq.setPlayList(sortedList);
    }


    private int getIntOfDay(String dayString){
        int day;
        switch( dayString ){
            case "Monday":
                day = 1;
                break;
            case "Tuesday":
                day = 2;
                break;
            case "Wednesday":
                day = 3;
                break;
            case "Thursday":
                day = 4;
                break;
            case "Friday":
                day = 5;
                break;
            case "Saturday":
                day = 6;
                break;
            case "Sunday":
                day = 7;
                break;
            default:
                day = 0;
                break;
        }
        return day;
    }

    /**
     * Get the string that represents the time zone of the day
     * @param time the hour 0-23 to represent the time
     * @return Morning - if the time is in the morning
     *         Afternoon - if the time is in the afternoon
     *         Evening - if the time is at night
     *         Unknown - if this value is never stored
     */
    private String getTimeOfDay (int time ){
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
        return timeZone;
    }

    private static class SongCompare implements Comparator<Song> {
        public int compare(Song s1, Song s2) {
            return s2.getProbability() - s1.getProbability();
        }
    }
}
