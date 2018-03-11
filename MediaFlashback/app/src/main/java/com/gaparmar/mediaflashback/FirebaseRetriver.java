package com.gaparmar.mediaflashback;

import android.location.LocationListener;

import java.util.ArrayList;

/**
 * Created by lxyzh on 3/4/2018.
 */

public class FirebaseRetriver implements FirebaseObserver {

    public void updateLocation( String filename, String locationString ){

    }

    public void updateDayOfWeek( String filename, String dayOfWeek ){
        MainActivity.getMusicQueuer().getSong(filename).setDayOfWeek( dayOfWeek );
    }

    public void updateUserName( String filename, String userName ) {
    }

    public void updateCoord( String filename, double lat, double lon ){
        MainActivity.getMusicQueuer().getSong(filename).setLocation(new double[]{lat, lon});
    }

    public void updateTimeStamp(String filename, long timeStamp ){
        MainActivity.getMusicQueuer().getSong(filename).setTimeStamp((int)timeStamp);
    }

    public void updateTime(String filename, long time ){
        MainActivity.getMusicQueuer().getSong(filename).setTime( (int) time);
    }

    public void updateRate(String filename, long rate){
        MainActivity.getMusicQueuer().getSong(filename).setState((int)rate);
    }

    public void updateProb( String filename, int prob ){
        MainActivity.getMusicQueuer().getSong(filename).setProbability(prob);
    }
}
