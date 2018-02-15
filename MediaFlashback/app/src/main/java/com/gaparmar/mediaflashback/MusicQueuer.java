package com.gaparmar.mediaflashback;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by veronica.lin1218 on 2/12/2018.
 */

public class MusicQueuer {

    /*
     * Two hash maps that store everything
     */
    private HashMap<Integer, Song> allTracks = new HashMap<Integer, Song>();
    private HashMap<String, Album> allAlbums = new HashMap<String, Album>();
    final static String strUnknown = "Unknown";
    final static String intUnknown = "0";
    Context context;
    public MusicQueuer( Context context ) {
        this.context = context;
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public void readSongs() {

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

            String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if( title == null ) title = strUnknown;
            String year = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
            if( year == null ) year = intUnknown;
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if( duration == null ) duration = intUnknown;
            String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            if( album == null ) album = strUnknown;
            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            if( artist == null ) artist = strUnknown;

            // Create a song object
            Song song = new Song( title, album, artist, Integer.parseInt(duration),
                    Integer.parseInt(year), songId);

            // Put the song object inside the track hashmap
            allTracks.put(songId, song);
        }
    }

    /*
     * Read in albums from song lists
     */
    public void readAlbums(){

        // Iterate through the song map to get all the albums
        Iterator<Map.Entry<Integer, Song>> it = allTracks.entrySet().iterator();
        //System.out.println( allTracks.get(R.raw.after_the_storm).getTitle());
        while( it.hasNext() ) {
            Map.Entry<Integer, Song> currEntry = it.next();
            Song currSong = currEntry.getValue();

            String albumName = currSong.getParentAlbum();
            if( albumName == null )albumName = "UnKnown";
            Album currAlbum = allAlbums.get(albumName);

            // If the album does not exists in the list, we create the new album
            if (currAlbum == null) {
                currAlbum = new Album(albumName);
                allAlbums.put(albumName, currAlbum);
                //Log.i("Putting Album", albumName);
            }
            currAlbum.addSong(currSong);
        }
    }

    public ArrayList<Integer> getEntireSongList(){
        ArrayList<Integer> songs = new ArrayList<Integer>();

        Iterator it = allTracks.entrySet().iterator();
        while( it.hasNext() ){
            HashMap.Entry currEntry = (HashMap.Entry) it.next();
            Song currSong = (Song) currEntry.getValue();

            songs.add( currSong.getRawID() );
        }
        return songs;
    }

    /*
     * returns a list of album names
     */
    public ArrayList<String> getEntireAlbumList() {
        ArrayList<String> albums = new ArrayList<String>();

        Iterator it = allAlbums.entrySet().iterator();
        while( it.hasNext() ){
            HashMap.Entry currEntry = (HashMap.Entry) it.next();
            Album currAlbum = (Album) currEntry.getValue();

            albums.add( currAlbum.getAlbumTitle() );
        }

        //for( String a : albums ){
         //   System.out.println( "Album Title: " + this.getAlbum( a ).getAlbumTitle() );
       // }
        return albums;
    }

    /*
     * Return the specific album object with given name
     */
    public Album getAlbum( String albumName ){
        return allAlbums.get( albumName );
    }

    public Song getSong( int ID ){
        return allTracks.get(ID);
    }

    public int getNumSongs( ){
        return allTracks.size();
    }

    public int getNumAlbums(){
        return allAlbums.size();
    }

}
