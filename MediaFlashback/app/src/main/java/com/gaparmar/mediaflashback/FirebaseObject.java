package com.gaparmar.mediaflashback;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lxyzh on 3/4/2018.
 */

public interface FirebaseObject {

    ArrayList<FirebaseObserver> observers = new ArrayList<>();
    public void register( FirebaseObserver observer);
    public void remove( FirebaseObserver observer);

    public void notifyLocation(int ID, double lat, double lon);
    public void notifyAddress(int ID, String address);
    public void notifyDayOfWeek( int ID, String dayOfWeek);
    public void notifyUserName( int ID, String userName);
    public void notifyTimeStamp( int ID, long timeStamp);
    public void notifyTime( int id, long time);
    public void notifyRate( int id, long rate );
    public void notifyProb( int id, int prob);
}
