package com.gaparmar.mediaflashback.DataStorage;

import android.util.Log;

import com.gaparmar.mediaflashback.Constant;
import com.gaparmar.mediaflashback.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import com.gaparmar.mediaflashback.UI.MainActivity;

/**
 * Created by lxyzh on 3/4/2018.
 */

public class FirebaseHandler {
    // Connection to the database
    final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference ref = database.getReference();

    // The subtree of songs
    static DatabaseReference songs = ref.child(Constant.SONG_SUBTREE);

    /**
     * Save a new song into database
     * @param song the song to be stored
     */
    public static void saveSong(Song song){
        final String songFileName = song.getFirebaseID();
        Log.d("FH:saveSong", "Saving song with file name " + song.getFileName());
        Query songQuery = songs.orderByChild(Constant.FILEID_FIELD).equalTo(songFileName);
        storeFirebaseID(song.getFileName(), songFileName);
        if( songQuery == null ) {
            songs.child(songFileName).setValue(song);
            Log.d("FH:saveSong", "Saving song with file ID " + songFileName);
        }
        else return;
    }

    /**
     * Store a filename string for the song
     * @param filename the song's filename we are trying to store
     * @param firebaseID the song's firebase ID for later searches
     */
    public static void storeFirebaseID( String filename, String firebaseID ){
        DatabaseReference updateRef = songs.child(firebaseID);
        Map<String, Object> updateID = new HashMap<>();

        updateID.put(Constant.FILEID_FIELD, firebaseID);
        updateRef.updateChildren(updateID);
    }
    /**
     * Store a new location of a song into the database
     * @param fileName the filename of the song we are storing
     * @param location the new coordinates
     */
    public static void storeLocation(String fileName, double[] location) {
        String fileID = Song.reformatFileName(fileName);
        DatabaseReference updateRef = songs.child(fileID);
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("lat", location[0]);
        updateMap.put("lon", location[1]);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the day of the week a song latly played into the database
     * @param fileName the filename of the song we are storing
     * @param dayOfWeek the day of the week this song is played
     */
    public static void storeDayOfWeek(String fileName, String dayOfWeek) {
        String fileID = Song.reformatFileName(fileName);
        DatabaseReference updateRef = songs.child(fileID);
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.WEEKDAY_FIELD, dayOfWeek);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the the address string where a song latly played into the database
     * @param fileName the filename of the song we are storing
     * @param address the day of the week this song is played
     */
    public static void storeAddress(String fileName, String address) {
        String firebaseID = Song.reformatFileName(fileName);
        DatabaseReference updateRef = songs.child(firebaseID);
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.ADDRESS_FIELD, address);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the the address string where a song latly played into the database
     * @param fileName the filename of the song we are storing
     * @param username the user who played the song
     */
    public static void storeUsername(String fileName, String username) {
        String fileID = Song.reformatFileName(fileName);

        DatabaseReference updateRef = songs.child(fileID);
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.USER_FIELD, username);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the full time stampe of a song latly played into the database
     * @param fileName the filename of the song we are storing
     * @param timeStamp timeStamp when the user who played the song
     */
    public static void storeTimeStamp(String fileName, long timeStamp) {
        String fileID = Song.reformatFileName(fileName);

        DatabaseReference updateRef = songs.child(fileID);
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.STAMP_FIELD, timeStamp);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the time of the day that a song latly played into the database
     * @param fileName the filename of the song we are storing
     * @param hour timeStamp when the user who played the song
     */
    public static void storeTime(String fileName, int hour) {
        String fileID = Song.reformatFileName(fileName);

        DatabaseReference updateRef = songs.child(fileID);
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.TIME_FIELD, hour);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the rating of a song latly played into the database
     * @param fileName the filename of the song we are storing
     * @param rate -1 dislike
     *             0 neutral
     *             1 like
     */
    public static void storeRate(String fileName, int rate) {
        String fileID = Song.reformatFileName(fileName);

        DatabaseReference updateRef = songs.child(fileID);
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.RATE_FIELD, rate);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the probability of a song being queued into the database
     * @param fileName the filename of the song we are storing
     * @param prob the probability
     */
    public static void storeProb(String fileName, int prob) {
        String fileID = Song.reformatFileName(fileName);

        DatabaseReference updateRef = songs.child(fileID);
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.PROB_FIELD, prob);
        updateRef.updateChildren(updateMap);
    }


    /**
     * Get the field specified by the argument that a song stored in the database
     * @param fileName the filename of the song we are storing
     * @param field the field we are trying to get
     */
    public static void getField(String fileName, String field){
        final String filename = fileName;
        final String fileID = Song.reformatFileName(fileName);
        Log.d("FH:getField", "Song's ID field is " + fileID);
        Query songQuery = songs.orderByChild(Constant.FILEID_FIELD).equalTo(fileID);

        final String fieldString = field;
        songQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FH:getField", "OnDataChange called");
                if( dataSnapshot== null || dataSnapshot.getValue() == null ){
                    Log.d("FH: getField","Can't find the object");
                }
                else {
                    Log.d("FH:getAddress", "Can find the object");
                    switch( fieldString ){
                        case Constant.ADDRESS_FIELD:
                            String address;
                            // Check if the field is stored already
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString) == null ){
                                Log.d("FH:getAddress", fieldString + " does not exist");
                                address = Constant.UNKNOWN;
                            }
                            else address = (String)((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyAddress(filename, address);
                            break;

                        case Constant.TIME_FIELD:
                            long time;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString) == null ){
                                Log.d("FH:getAddress", fieldString + " does not exist");
                                time = Constant.UNKNOWN_INT;
                            }
                            else time = (Long) ((HashMap) ((HashMap) dataSnapshot.getValue()).get(fileID)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyTime(filename, time);
                            break;

                        case Constant.STAMP_FIELD:
                            long timeStamp;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString) == null ){
                                Log.d("FH:getAddress", fieldString + " does not exist");
                                timeStamp = Constant.UNKNOWN_INT;
                            }
                            timeStamp = (Integer)((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyTimeStamp(filename, timeStamp);
                            break;

                        case Constant.WEEKDAY_FIELD:
                            String dayOfWeek;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString) == null ){
                                Log.d("FH:getAddress", fieldString + " does not exist");
                                dayOfWeek = Constant.UNKNOWN;
                            }
                            else dayOfWeek = (String)((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyDayOfWeek(filename, dayOfWeek);
                            break;

                        case Constant.USER_FIELD:
                            String userName;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString) == null ){
                                Log.d("FH:getAddress", fieldString + " does not exist");
                                userName = Constant.UNKNOWN;
                            }
                            else userName = (String)((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyUserName(filename, userName);
                            break;

                        case Constant.COORD_FIELD:
                            double lat = (Double)((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get("lat");
                            Log.d("FH:getLocation", "Retrieved latitude: " + lat);
                            double lon = (Double)((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get("lon");
                            MainActivity.getFirebaseInfoBus().notifyLocation(filename,lat, lon);
                            break;

                        case Constant.RATE_FIELD:
                            long rate;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString) == null ){
                                rate = Constant.UNKNOWN_INT;
                                Log.d("FH:getAddress", fieldString + " does not exist");
                            }
                            else rate = (Long) ((HashMap) ((HashMap) dataSnapshot.getValue()).get(fileID)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyRate(filename, rate);
                            break;

                        case Constant.PROB_FIELD:
                            int prob;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(fileID)).get(fieldString) == null ){
                                prob = 1;
                                Log.d("FH:getAddress", fieldString + " does not exist");
                            }
                            else prob = (int) ((HashMap) ((HashMap) dataSnapshot.getValue()).get(fileID)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyProb(filename, prob);
                            break;

                        default:
                            Log.d("FH:getField", "Cannot find field " + fieldString );
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
