package com.gaparmar.mediaflashback.DataStorage;

import com.gaparmar.mediaflashback.Song;

import java.util.ArrayList;

/**
 * Created by lxyzh on 3/4/2018.
 */

public interface FirebaseObject {

    public ArrayList<FirebaseObserver> observers = new ArrayList<>();
    public void register( FirebaseObserver observer);
    public void remove( FirebaseObserver observer);

    public void notifyLocation(String filename, double lat, double lon);
    public void notifyAddress(String filename, String address);
    public void notifyDayOfWeek( String filename, String dayOfWeek);
    public void notifyUserName( String filename, String userName);
    public void notifyTimeStamp( String filename, long timeStamp);
    public void notifyTime( String filename, long time);
    public void notifyRate( String filename, long rate );
    public void notifyProb( String filename, int prob);
    public void notifyLogList(String filename, ArrayList<LogInstance> list);
    public void notifySongList( ArrayList<String> songList );
}
