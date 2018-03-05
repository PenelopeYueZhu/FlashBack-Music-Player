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
    static DatabaseReference songs = ref.child(Constant.SONG_SUBTREE);

    /**
     * Save a new song into database
     * @param song the song to be stored
     */
    public static void saveSong(SongString song){
        final int songId = song.getId();
        Query songQuery = songs.orderByChild(Constant.ID_FIELD).equalTo(songId);
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
     * Store the day of the week a song latly played into the database
     * @param ID the id of the song we are storing
     * @param dayOfWeek the day of the week this song is played
     */
    public static void storeDayOfWeek(int ID, String dayOfWeek) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.WEEKDAY_FIELD, dayOfWeek);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the the address string where a song latly played into the database
     * @param ID the id of the song we are storing
     * @param address the day of the week this song is played
     */
    public static void storeAddress(int ID, String address) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.ADDRESS_FIELD, address);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the the address string where a song latly played into the database
     * @param ID the id of the song we are storing
     * @param username the user who played the song
     */
    public static void storeUsername(int ID, String username) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.USER_FIELD, username);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the full time stampe of a song latly played into the database
     * @param ID the id of the song we are storing
     * @param timeStamp timeStamp when the user who played the song
     */
    public static void storeTimeStamp(int ID, long timeStamp) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.STAMP_FIELD, timeStamp);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the time of the day that a song latly played into the database
     * @param ID the id of the song we are storing
     * @param hour timeStamp when the user who played the song
     */
    public static void storeTime(int ID, int hour) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.TIME_FIELD, hour);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Store the rating of a song latly played into the database
     * @param ID the id of the song we are storing
     * @param rate -1 dislike
     *             0 neutral
     *             1 like
     */
    public static void storeRate(int ID, int rate) {
        DatabaseReference updateRef = songs.child(Integer.toString(ID));
        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put(Constant.RATE_FIELD, rate);
        updateRef.updateChildren(updateMap);
    }

    /**
     * Get the field specified by the argument that a song stored in the database
     * @param ID the id of the song
     * @param field the field we are trying to get
     */
    public static void getField(int ID, String field){
        Query songQuery = songs.orderByChild(Constant.ID_FIELD).equalTo(ID);
        final String id = Integer.toString(ID);
        final String fieldString = field;
        songQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FH:getAddress", "OnDataChange called");
                if( dataSnapshot== null || dataSnapshot.getValue() == null ){
                    Log.d("FH: getAddress","Can't find the object");
                }
                else {
                    Log.d("FH:getAddress", "Can find the object");
                    switch( fieldString ){
                        case Constant.ADDRESS_FIELD:
                            String address;
                            // Check if the field is stored already
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get(fieldString) == null ){
                                Log.d("FH:getAddress", fieldString + " does not exist");
                                address = Constant.UNKNOWN;
                            }
                            else address = (String)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyAddress(Integer.parseInt(id), address);
                            break;

                        case Constant.TIME_FIELD:
                            long time;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get(fieldString) == null ){
                                Log.d("FH:getAddress", fieldString + " does not exist");
                                time = Constant.UNKNOWN_INT;
                            }
                            else time = (Long) ((HashMap) ((HashMap) dataSnapshot.getValue()).get(id)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyTime(Integer.parseInt(id), time);
                            break;

                        case Constant.STAMP_FIELD:
                            long timeStamp;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get(fieldString) == null ){
                                Log.d("FH:getAddress", fieldString + " does not exist");
                                timeStamp = Constant.UNKNOWN_INT;
                            }
                            timeStamp = (Integer)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyTimeStamp(Integer.parseInt(id), timeStamp);
                            break;

                        case Constant.WEEKDAY_FIELD:
                            String dayOfWeek;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get(fieldString) == null ){
                                Log.d("FH:getAddress", fieldString + " does not exist");
                                dayOfWeek = Constant.UNKNOWN;
                            }
                            else dayOfWeek = (String)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyDayOfWeek(Integer.parseInt(id), dayOfWeek);
                            break;

                        case Constant.USER_FIELD:
                            String userName;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get(fieldString) == null ){
                                Log.d("FH:getAddress", fieldString + " does not exist");
                                userName = Constant.UNKNOWN;
                            }
                            else userName = (String)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyUserName(Integer.parseInt(id), userName);
                            break;

                        case Constant.COORD_FIELD:
                            double lat = (Double)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get("lat");
                            Log.d("FH:getLocation", "Retrieved latitude: " + lat);
                            double lon = (Double)((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get("lon");
                            MainActivity.getFirebaseInfoBus().notifyLocation(Integer.parseInt(id),lat, lon);
                            break;

                        case Constant.RATE_FIELD:
                            long rate;
                            if( ((HashMap)((HashMap)dataSnapshot.getValue()).get(id)).get(fieldString) == null ){
                                rate = Constant.UNKNOWN_INT;
                                Log.d("FH:getAddress", fieldString + " does not exist");
                            }
                            else rate = (Long) ((HashMap) ((HashMap) dataSnapshot.getValue()).get(id)).get(fieldString);
                            MainActivity.getFirebaseInfoBus().notifyRate(Integer.parseInt(id), rate);
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
