package com.gaparmar.mediaflashback;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by veronica.lin1218 on 2/12/2018.
 */

public class MusicQueuer {

    // Member variables of the class
    private HashMap<Integer, Song> allTracks = new HashMap<>();
    private HashMap<String, Album> allAlbums = new HashMap<>();
    private final static String UNKNOWN_STRING = "Unknown";
    private final static String UNKNOWN_INT = "0";
    private Context context;
    final private String PACKAGE = "com.gaparmar.mediaflashback";
    final private String RES_FOLDER = "raw";
    final private String URI_PREFIX = "android.resource://com.gaparmar.mediaflashback/raw/";


    /**
     * The constructor of the MusicQueuer Object
     * @param context the activity context reference
     */
    public MusicQueuer( Context context ) {
        this.context = context;
    }


    /**
     * Populates the allTracks hashmap
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public void readSongs() {
        // Get all the song files from raw folder
        Field[] songLists = R.raw.class.getFields();
        // TODO:: What if we have other non-song resources in the raw folder?
        for( int count = 0 ; count < songLists.length ; count ++) {
            // Push a new object
            // Get the name of the song
            String name = songLists[count].getName();
            // Get the ID of the song
            int songId = context.getResources().getIdentifier(name, RES_FOLDER, PACKAGE);
            // Get the path of the song
            Uri songPath = Uri.parse(URI_PREFIX+name );
            // Get all the metadata
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            System.out.println(songPath);
            String title = UNKNOWN_STRING;
            String year = UNKNOWN_INT;
            String duration = UNKNOWN_INT;
            String album = UNKNOWN_STRING;
            String artist = UNKNOWN_STRING;
            try{
                retriever.setDataSource(context, songPath);
                title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                year = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
                duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            } catch (Exception e){
                e.printStackTrace();
                title = UNKNOWN_STRING;
                year = UNKNOWN_INT;
                duration = UNKNOWN_INT;
                album = UNKNOWN_STRING;
                artist = UNKNOWN_STRING;
            }
            // If any field is null, set it to default values
            if (title == null)
                title = UNKNOWN_STRING;
            if (year == null)
                year = UNKNOWN_INT;
            if(duration == null)
                duration = UNKNOWN_INT;
            if(album == null)
                album = UNKNOWN_STRING;
            if(artist == null)
                artist = UNKNOWN_STRING;

            // Create a song object
            Song song = new Song( title, album, artist, Integer.parseInt(duration),
                    Integer.parseInt(year), songId, StorageHandler.getSongLocation(context, songId));  //TODO maybe change null to location?

            // Put the song object inside the track hashmap
            allTracks.put(songId, song);
        }
    }

    /*
     * Read in albums from song lists
     * Populates the allAlbums hashmap
     */
    public void readAlbums(){
        // Iterate through the song map to get all the albums
        Iterator<Map.Entry<Integer, Song>> it = allTracks.entrySet().iterator();
        while( it.hasNext() ) {
            Map.Entry<Integer, Song> currEntry = it.next();
            Song currSong = currEntry.getValue();

            String albumName = currSong.getParentAlbum();
            if( albumName == null )albumName = UNKNOWN_STRING;
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


    /**
     * ArrayList of all the Song IDs
     * @return Convers the allTracks hashmap into an ArrayList
     */
    public ArrayList<Integer> getEntireSongList(){
        ArrayList<Integer> songs = new ArrayList<>();

        Iterator it = allTracks.entrySet().iterator();
        while( it.hasNext() ){
            HashMap.Entry currEntry = (HashMap.Entry) it.next();
            Song currSong = (Song) currEntry.getValue();

            songs.add( currSong.getResID() );
        }
        return songs;
    }

    /**
     * ArrayList of all the Album names
     * @return Convers the allAlbums hashmap into an ArrayList
     */
    public ArrayList<String> getEntireAlbumList() {
        ArrayList<String> albums = new ArrayList<>();
        Iterator it = allAlbums.entrySet().iterator();
        while( it.hasNext() ){
            HashMap.Entry currEntry = (HashMap.Entry) it.next();
            Album currAlbum = (Album) currEntry.getValue();

            albums.add( currAlbum.getAlbumTitle() );
        }
        return albums;
    }

    // TODO:: Add null checks first to see if the album exists

    /**
     * Gets the Album object based on the Album name
     * @param albumName name of the Album
     * @return The corresponding Album object
     */
    public Album getAlbum( String albumName ){
        return allAlbums.get( albumName );
    }

    // TODO:: make a helper function that gets song based on song name
    /**
     * Gets the Song object based on the Song ID
     * @param ID ID of the song
     * @return The corresponding
     */
    public Song getSong( int ID ){
        return allTracks.get(ID);
    }

    /**
     * Get song information
     * @param ID ID of the song
     * @return An ArrayList of strings that contains the song's information
     */
    public ArrayList<String> getSongInfo( int ID) {
        ArrayList<String> infoBus = new ArrayList<String>();
        Song song = this.getSong(ID);

        infoBus.add(song.getTitle());
        infoBus.add(song.getDayOfWeek());
        infoBus.add(Integer.toString(song.getTimeLastPlayed())+":00");
        infoBus.add(song.getLocationString(context));
        return infoBus;
    }

    /**
     * @return the total number of currently stored songs
     */
    public int getNumSongs( ){
        return allTracks.size();
    }

    /**
     * @return the total number of currently stored albums
     */
    public int getNumAlbums(){
        return allAlbums.size();
    }

}
