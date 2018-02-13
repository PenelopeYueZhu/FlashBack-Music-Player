package com.gaparmar.mediaflashback;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by veronica.lin1218 on 2/12/2018.
 */

public class MusicQueuer {
    private HashMap<Song, Integer> allTracks = new HashMap();
    private HashMap<Album, Integer> allAlbums = new HashMap();
    public MusicQueuer() {

    }
    public void readSongs(Context context) {

        // Get all the song files from raw folder
        Field[] songLists = R.raw.class.getFields();
        for( int count = 0 ; count < songLists.length ; count ++) {
            String name = songLists[count].getName();
            int songId = context.getResources().getIdentifier(name, "raw", "com.gaparmar.mediaflashback");
            Uri songPath = Uri.parse("android.resource://com.gaparmar.mediaflashback/raw/"+name );
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, songPath);
            Song song = new Song( retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST), 0,
                    0, songId);

            Log.i("Title", song.getTitle());
        }

    }



}
