package com.gaparmar.mediaflashback;

/**
 * Created by lxyzh on 3/4/2018.
 */

public class FirebaseInfoBus implements FirebaseObject {

    public FirebaseInfoBus(){

    }

    /**
     * Register an observer
     * @param observer the observer to register
     */
    public void register( FirebaseObserver observer){
        observers.add(observer);
    }

    /**
     * Remove an observer
     * @param observer the observer to remove
     */
    public void remove( FirebaseObserver observer){
        observers.remove(observer);
    }

    /**
     * Notify the change of the coordiantes to all the observers registered to this object
     * @param id the id of the song
     * @param lat the latitude of the coordinates
     * @param lon the longitutde of the coordinates
     */
    public void notifyLocation(int id, double lat, double lon){
        for( FirebaseObserver observer : observers){
            observer.updateCoord(id, lat, lon);
        }
    }

    /**
     * Notify the change of location address to all the observers registered to this object
     * @param id the id of the song
     * @param locationString the address string
     */
    public void notifyAddress( int id, String locationString ){
        for( FirebaseObserver observer : observers){
            observer.updateLocation(id, locationString);
        }
    }

    /**
     * Notify the day of the week change to all the observers registered to this object
     * @param id the id of the song
     * @param dayOfWeek the time when the song is lastly played
     */
    public void notifyDayOfWeek( int id, String dayOfWeek ){
        for( FirebaseObserver observer : observers){
            observer.updateDayOfWeek(id, dayOfWeek);
        }
    }

    /**
     * Notify all the observers registered to this object
     * @param id the id of the song
     * @param userName the user who played the song last time
     */
    public void notifyUserName( int id, String userName ) {
        for (FirebaseObserver observer : observers) {
            observer.updateUserName(id, userName);
        }
    }

    /**
     * Notify all the observers registered to this object
     * @param id the id of the song
     * @param timeStamp the time lastly played the song last time
     */
    public void notifyTimeStamp( int id, long timeStamp ) {
        for (FirebaseObserver observer : observers) {
            observer.updateTimeStamp(id, timeStamp);
        }
    }

    /**
     * Notify all the observers registered to this object
     * @param id the id of the song
     * @param time the time lastly played the song last time
     */
    public void notifyTime( int id, long time ) {
        for (FirebaseObserver observer : observers) {
            observer.updateTime(id, time);
        }
    }

    /**
     * Notify all the observers registered to the object of the rate of the song
     * @param id the id of the song
     * @param rate the rate of the song
     */
    public void notifyRate( int id, long rate ){
        for (FirebaseObserver observer : observers) {
            observer.updateRate(id, rate);
        }
    }

}
