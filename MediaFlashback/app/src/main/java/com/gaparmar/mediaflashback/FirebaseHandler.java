package com.gaparmar.mediaflashback;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

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
     * Saves the Song object to the new_song_list subdirectory and the song_log directory
     * Ensures there are no 2 songs with the same song_title field
     * @param song The song to be added
     */
    public static void saveSongToSongList(Song song){
        Log.d("FH:saveSong", "Saving song with file name " + song.getFileName());
        DatabaseReference ref = database.getReference();
        final Song song_ref = song;
        DatabaseReference newRef = ref.child("new_song_list").push();
        newRef.setValue(song);
        // Checks if the current song got added twice
        Query songQuery = ref.child("new_song_list").orderByChild("title").equalTo(song.getTitle());
        songQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 1)
                    dataSnapshot.getChildren().iterator().next().getRef().setValue(null);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // Create the song in the songs_logs subdirectory
        ref = database.getReference();
        songQuery = ref.child("song_logs").orderByChild("song_title").equalTo(song.getTitle());
        songQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() < 1){
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();
                    HashMap<String, String> t = new HashMap<>();
                    t.put("song_title", song_ref.getTitle());
                    ref.child("song_logs").push().setValue(t);
                    System.out.println(t.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    /**
     * Ensure that the song is added to the database before logging an instance of the song
     * @param song The song who's log field is to be populated
     * @param toLog The Data that needs to be logged to the Firebase
     */
    public static void saveLoggedSong(Song song, SongLogInstance toLog){
        final SongLogInstance cpy = toLog;
        Query query = ref.child("song_logs").orderByChild("song_title").equalTo(song.getTitle());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    data.getRef().child("logs").push().setValue(cpy);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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
