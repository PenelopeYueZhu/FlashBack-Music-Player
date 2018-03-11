package com.gaparmar.mediaflashback.DataStorage;

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
     * @param filename the id of the song
     * @param lat the latitude of the coordinates
     * @param lon the longitutde of the coordinates
     */
    public void notifyLocation(String filename, double lat, double lon){
        for( FirebaseObserver observer : observers){
            observer.updateCoord(filename, lat, lon);
        }
    }

    /**
     * Notify the change of location address to all the observers registered to this object
     * @param filename the id of the song
     * @param locationString the address string
     */
    public void notifyAddress( String filename, String locationString ){
        for( FirebaseObserver observer : observers){
            observer.updateLocation(filename, locationString);
        }
    }

    /**
     * Notify the day of the week change to all the observers registered to this object
     * @param filename the id of the song
     * @param dayOfWeek the time when the song is lastly played
     */
    public void notifyDayOfWeek( String filename, String dayOfWeek ){
        for( FirebaseObserver observer : observers){
            observer.updateDayOfWeek(filename, dayOfWeek);
        }
    }

    /**
     * Notify all the observers registered to this object
     * @param filename the id of the song
     * @param userName the user who played the song last time
     */
    public void notifyUserName( String filename, String userName ) {
        for (FirebaseObserver observer : observers) {
            observer.updateUserName(filename, userName);
        }
    }

    /**
     * Notify all the observers registered to this object
     * @param filename the id of the song
     * @param timeStamp the time lastly played the song last time
     */
    public void notifyTimeStamp( String filename, long timeStamp ) {
        for (FirebaseObserver observer : observers) {
            observer.updateTimeStamp(filename, timeStamp);
        }
    }

    /**
     * Notify all the observers registered to this object
     * @param filename the id of the song
     * @param time the time lastly played the song last time
     */
    public void notifyTime( String filename, long time ) {
        for (FirebaseObserver observer : observers) {
            observer.updateTime(filename, time);
        }
    }

    /**
     * Notify all the observers registered to the object of the rate of the song
     * @param filename the id of the song
     * @param rate the rate of the song
     */
    public void notifyRate( String filename, long rate ){
        for (FirebaseObserver observer : observers) {
            observer.updateRate(filename, rate);
        }
    }

    /**
     * Notify all the observers registered to the object of the probablity of the song
     * @param filename the id of the song
     * @param prob the probability of the song
     */
    public void notifyProb( String filename, int prob ){
        for (FirebaseObserver observer : observers) {
            observer.updateProb(filename, prob);
        }
    }


}
