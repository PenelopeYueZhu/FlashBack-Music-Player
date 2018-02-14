package com.gaparmar.mediaflashback;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by veronica.lin1218 on 2/12/2018.
 */

public class MusicQueuer {
    private HashMap<Integer, Song> allTracks = new HashMap<Integer, Song>();
    private HashMap<String, Album> allAlbums = new HashMap<String, Album>();
    public MusicQueuer() {

    }
    public void readSongs(Context context) {

        // Get all the song files from raw folder
        Field[] songLists = R.raw.class.getFields();
        for( int count = 0 ; count < songLists.length ; count ++) {
            // Push a new object
            // Get the name of the song
            String name = songLists[count].getName();
            // Get the ID of the song
            int songId = context.getResources().getIdentifier(name, "raw", "com.gaparmar.mediaflashback");
            // Get the path of the song
            Uri songPath = Uri.parse("android.resource://com.gaparmar.mediaflashback/raw/"+name );
            // Get all the metadata
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, songPath);

            // Create a song object
            Song song = new Song( retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
                    Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)),
                    Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)),
                    songId);

            // Put the song object inside the track hashmap
            allTracks.put(songId, song);
        }
    }

    public void readAlbums(){

        // Iterate through the song map to get all the albums
        Iterator it = allTracks.entrySet().iterator();
        int count = 0;
        while( it.hasNext() ) {
            HashMap.Entry currEntry = (HashMap.Entry) it.next();
            Song currSong = (Song) currEntry.getValue();

            String albumName = currSong.getParentAlbum();
            Album currAlbum = allAlbums.get(albumName);

            // If the album does not exists in the list, we create the new album
            if (currAlbum == null) {
                currAlbum = new Album();
                allAlbums.put(albumName, currAlbum);
            }
            currAlbum.addSong(currSong);
        }
    }
}
