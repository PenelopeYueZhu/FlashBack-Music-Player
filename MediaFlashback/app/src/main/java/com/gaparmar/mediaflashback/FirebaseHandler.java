package com.gaparmar.mediaflashback;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Created by lxyzh on 3/4/2018.
 */

public class FirebaseHandler {
    // Connection to the database
    final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference ref = database.getReference();
    static DatabaseReference songs = ref.child("songs");
    static SongString song;
    static double[] location = {0.0,0.0};

    public static void saveSong(SongString songString){
       Map<String, SongString> toBePushed = new HashMap<>();
       toBePushed.put(songString.getId(), songString);
       songs.setValue(toBePushed);
    }

    public static void storeLocation(String ID, double[] location) {
        DatabaseReference updateRef = songs.child(ID);
        Map<String, Object> updateMap = new HashMap<>();

        String lat = Double.toString(location[0]);
        String lon = Double.toString(location[1]);
        updateMap.put("lat", lat);
        updateMap.put("lon", lon);
        updateRef.updateChildren(updateMap);
    }

    public static double[] getLocation(String ID){
        //DatabaseReference singleSong = songs.child(ID);
        location[0]=0.2;
        songs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FH:getLoation", "OnDataChange called");
                FirebaseHandler.location[0]=0.3;
                if( dataSnapshot== null || dataSnapshot.getValue() == null ){
                    Log.d("FH: getLocation","Can't find the object");
                }
                else {
                    Log.d("FH:getLocation", "Can find the object");
                    song = dataSnapshot.getValue(SongString.class);
                    FirebaseHandler.location[0]=0.5;
                    FirebaseHandler.location[1]=0.5;
                    FirebaseHandler.returnLocation(song.getLat(), song.getLon());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseHandler.location[0]=0.8;
            }
        });
        return location;
    }

    public static void returnLocation( String lat, String lon){
        if(lat == null )         location[0] = 1.0;
        else  location[0] = Double.parseDouble(lat);
        if(lon == null )    location[1]=1.0;
        else location[1] = Double.parseDouble(lon);
    }
}
