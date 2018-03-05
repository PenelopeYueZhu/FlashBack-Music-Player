package com.gaparmar.mediaflashback;

import android.location.LocationListener;

import java.util.ArrayList;

/**
 * Created by lxyzh on 3/4/2018.
 */

public class FirebaseRetriver implements FirebaseObserver {

    public void updateLocation( int id, String locationString ){

    }

    public void updateDayOfWeek( int id, String dayOfWeek ){
    }

    public void updateUserName( int id, String userName ) {
    }

    public void updateCoord( int id, double lat, double lon ){
        MainActivity.getMusicQueuer().getSong(id).setLocation(new double[]{lat, lon});
    }

    public void updateTimeStamp(int id, long timeStamp ){
        MainActivity.getMusicQueuer().getSong(id).setFullTimeStamp(timeStamp);
    }

    public void updateTime(int id, long time ){}

    public void updateRate(int id, long rate){
        MainActivity.getMusicQueuer().getSong(id).setState((int)rate);
    }
}
