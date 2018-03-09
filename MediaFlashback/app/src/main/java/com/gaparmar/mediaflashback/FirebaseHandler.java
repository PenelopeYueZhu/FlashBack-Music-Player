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

}
