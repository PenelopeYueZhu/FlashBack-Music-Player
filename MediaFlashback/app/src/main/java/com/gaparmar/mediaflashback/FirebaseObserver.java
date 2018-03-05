package com.gaparmar.mediaflashback;

/**
 * Created by lxyzh on 3/4/2018.
 */

public interface FirebaseObserver {

    //public void update(int id, double[] location, String locationString, String dayOfWeek, String userName );

    public void updateLocation( int id, String locationString );
    public void updateDayOfWeek( int id, String dayOfWeek );
    public void updateUserName( int id, String userName );
    public void updateCoord( int id, double lat, double lon );
    public void updateTimeStamp( int id, long timeStamp );
    public void updateTime( int id, long time);
    public void updateRate(int id, long rate);
    public void updateProb( int id, int prob);
}
