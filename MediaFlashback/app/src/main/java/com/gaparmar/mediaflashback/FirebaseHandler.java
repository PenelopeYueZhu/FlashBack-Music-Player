package com.gaparmar.mediaflashback;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxyzh on 3/4/2018.
 */

public class FirebaseHandler {
    // Connection to the database
    final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference ref = database.getReference();

    // The subtree of songs
    static DatabaseReference songs = ref.child("songs");

    /**
     * Save a new song into database
     * @param song the song to be stored
     */
    public static void saveSong(SongString song){
        final int songId = song.getId();
        Query songQuery = songs.orderByChild("id").equalTo(songId);
        if( songQuery == null ) songs.child(Integer.toString(songId)).setValue(song);
        else return;
    }

    /**
     * Store a new location of a song into the database
     * @param ID the id of the song we are storing
     * @param location the new coordinates
     */
    public static void storeLocation(int ID, double[] location) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("lat", location[0]);
        updateMap.put("lon", location[1]);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Get the location of a song stored in the database
     * @param ID the id of the song
     */
    public static void getLocation(int ID){
        Query songQuery = songs.orderByChild("id").equalTo(ID);
        final String id = Integer.toString(ID);
        songQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FH:getLoation", "OnDataChange called");
                if( dataSnapshot== null || dataSnapshot.getValue() == null ){
                    Log.d("FH: getLocation","Can't find the object");
                }
                else {
                    Log.d("FH:getLocation", "Can find the object");
                    double lat = (Double)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get("lat");
                    Log.d("FH:getLocation", "Retrieved latitude: " + lat);
                    double lon = (Double)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get("lon");
                    MainActivity.getFirebaseInfoBus().notifyLocation(Integer.parseInt(id),lat, lon);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Store the day of the week a song latly played into the database
     * @param ID the id of the song we are storing
     * @param dayOfWeek the day of the week this song is played
     */
    public static void storeDayOfWeek(int ID, String dayOfWeek) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("dayOfWeek", dayOfWeek);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Get the day of the week of a song stored in the database
     * @param ID the id of the song
     */
    public static void getDayOfWeek(int ID){
        Query songQuery = songs.orderByChild("id").equalTo(ID);
        final String id = Integer.toString(ID);
        songQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FH:getDayOfWeek", "OnDataChange called");
                if( dataSnapshot== null || dataSnapshot.getValue() == null ){
                    Log.d("FH: getDayOfWeek","Can't find the object");
                }
                else {
                    Log.d("FH:getDayOfWeek", "Can find the object");
                    String dayOfWeek = (String)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get("dayOfWeek");
                    MainActivity.getFirebaseInfoBus().notifyDayOfWeek(Integer.parseInt(id), dayOfWeek);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Store the the address string where a song latly played into the database
     * @param ID the id of the song we are storing
     * @param address the day of the week this song is played
     */
    public static void storeAddress(int ID, String address) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("address", address);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Get the address that a song stored in the database
     * @param ID the id of the song
     */
    public static void getAddress(int ID){
        Query songQuery = songs.orderByChild("id").equalTo(ID);
        final String id = Integer.toString(ID);
        songQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FH:getAddress", "OnDataChange called");
                if( dataSnapshot== null || dataSnapshot.getValue() == null ){
                    Log.d("FH: getAddress","Can't find the object");
                }
                else {
                    Log.d("FH:getAddress", "Can find the object");
                    String address = (String)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get("address");
                    MainActivity.getFirebaseInfoBus().notifyAddress(Integer.parseInt(id), address);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Store the the address string where a song latly played into the database
     * @param ID the id of the song we are storing
     * @param username the user who played the song
     */
    public static void storeUsername(int ID, String username) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("userName", username);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Get the user name of a song stored in the database
     * @param ID the id of the song
     */
    public static void getUsername(int ID){
        Query songQuery = songs.orderByChild("id").equalTo(ID);
        final String id = Integer.toString(ID);
        songQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FH:getUsername", "OnDataChange called");
                if( dataSnapshot== null || dataSnapshot.getValue() == null ){
                    Log.d("FH: getUsername","Can't find the object");
                }
                else {
                    Log.d("FH:getUsername", "Can find the object");
                    String userName = (String)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get("userName");
                    MainActivity.getFirebaseInfoBus().notifyUserName(Integer.parseInt(id), userName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Store the full time stampe of a song latly played into the database
     * @param ID the id of the song we are storing
     * @param timeStamp timeStamp when the user who played the song
     */
    public static void storeTimeStamp(int ID, long timeStamp) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("timeStamp", timeStamp);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Get the full timeStamp when a song stored in the database
     * @param ID the id of the song
     */
    public static void getTimeStamp(int ID){
        Query songQuery = songs.orderByChild("id").equalTo(ID);
        final String id = Integer.toString(ID);
        songQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FH:getTimeStamp", "OnDataChange called");
                if( dataSnapshot== null || dataSnapshot.getValue() == null ){
                    Log.d("FH: getTimeStamp","Can't find the object");
                }
                else {
                    Log.d("FH:getTimeStamp", "Can find the object");
                    long timeStamp = (Integer)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get("timeStamp");
                    MainActivity.getFirebaseInfoBus().notifyTimeStamp(Integer.parseInt(id), timeStamp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Store the time of the day that a song latly played into the database
     * @param ID the id of the song we are storing
     * @param hour timeStamp when the user who played the song
     */
    public static void storeTime(int ID, int hour) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("time", hour);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Get the hour when a song stored in the database
     * @param ID the id of the song
     */
    public static void getTime(int ID){
        Query songQuery = songs.orderByChild("id").equalTo(ID);
        final String id = Integer.toString(ID);
        songQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FH:getTime", "OnDataChange called");
                if( dataSnapshot== null || dataSnapshot.getValue() == null ){
                    Log.d("FH: getTime","Can't find the object");
                }
                else {
                    Log.d("FH:getTime", "Can find the object");
                    int time = (Integer)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get("time=");
                    MainActivity.getFirebaseInfoBus().notifyTimeStamp(Integer.parseInt(id), time);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
