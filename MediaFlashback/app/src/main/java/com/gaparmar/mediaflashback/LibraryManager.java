package com.gaparmar.mediaflashback;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Manages a list of all downloaded songs and albums
 */
public class LibraryManager {

    // Member variables of the class
    protected HashMap<String, Song> allTracks = new HashMap<>();
    protected HashMap<String, Album> allAlbums = new HashMap<>();

    private final static String UNKNOWN_STRING = "Unknown";
    private final static String UNKNOWN_INT = "0";
    private Context context;

    // final private String PACKAGE = "com.gaparmar.mediaflashback";

    protected Calendar currDate;
    protected SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
    protected SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.US);
    protected SimpleDateFormat fullTimeFormat = new SimpleDateFormat("HH:mm 'at' MM/dd/YY");

    /**
     * The constructor of the LibraryManager Object
     * @param context the activity context reference
     */
    public LibraryManager(Context context) {
        this.context = context;
    }


    /**
     * Read all files and directories
     */
    public void readSongs() {
        File dir = new File(Constant.MEDIA_PATH);
        File[] fileList = dir.listFiles();
        Log.i("MQ", "Reading all files");
        // Iterate over list of files in the downloads folder
        for (int i = 0; i<fileList.length; i++){
            File currentFile = fileList[i];
            if(currentFile.isFile()){
                addSongFromPath(currentFile.getPath(), currentFile.getName(), this.context);
            }
        }
        if (fileList == null)
            new File(Environment.getExternalStorageDirectory() + File.separator + "myDownloads").mkdirs();
    }

    /**
     * Adds song from the given location into the hashmap
     * @param songPath the path to the file
     * @param fileName the name of the file
     * @param context The application context
     */
    public void addSongFromPath(String songPath, String fileName, Context context){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        // Initialize the fields to the default values
        String title = "unknown";
        String year = "-1";
        String album = "unknown";
        String artist = "unknown";
        try {
            retriever.setDataSource(context, Uri.parse(songPath));
            title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            year = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
            album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        } catch (Exception e){
            e.printStackTrace();
        }
        // Checks if the song has already been added
        if (!songExists(title)){
            Song song = new Song();
            song.setTitle(title);
            song.setArtistName(artist);
            song.setParentAlbum(album);
            song.setState(Constant.State.NEUTRAL);
            song.setYearOfRelease(Integer.parseInt(year));
            song.setFileName(fileName);
            allTracks.put(title, song);
        }
    }

    /**
     * Checks if a song by the name already exists in the hashmap
     * @param songName the name to be checked for
     * @return true if song is already added
     */
    private boolean songExists(String songName){
        Song s = allTracks.get(songName);
        return !(s==null);
    }


    /**
     * Read in albums from song lists
     * Populates the allAlbums hashmap
     */
    public void readAlbums() {
        // Iterate through the song map to get all the albums
        Iterator<Map.Entry<String, Song>> it = allTracks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Song> currEntry = it.next();
            Song currSong = currEntry.getValue();

            Log.d("MQ:readAlbum", "Getting the song " + currSong.getTitle());
            String albumName = currSong.getParentAlbum();
            if (albumName == null) albumName = UNKNOWN_STRING;
            Album currAlbum = allAlbums.get(albumName);

            // If the album does not exists in the list, we create the new album
            if (currAlbum == null) {
                currAlbum = new Album(albumName);
                allAlbums.put(albumName, currAlbum);
            }
            Log.d("readAlbum", "Putting the song " + currSong.getTitle() + " into Album"
                    + currAlbum.getAlbumTitle());
            currAlbum.addSong(currSong);
        }
    }

    /**
     * ArrayList of all the Song IDs
     *
     * @return Convers the allTracks hashmap into an ArrayList
     */
    public ArrayList<String> getEntireSongList() {
        ArrayList<String> songs = new ArrayList<>();

        Iterator it = allTracks.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry currEntry = (HashMap.Entry) it.next();
            Song currSong = (Song) currEntry.getValue();

            songs.add(currSong.getFileName());
            System.err.println( " the file name is : " + currSong.getFileName());
        }
        return songs;
    }

    /**
     * ArrayList of all the Album names
     *
     * @return Convers the allAlbums hashmap into an ArrayList
     */
    public ArrayList<String> getEntireAlbumList() {
        ArrayList<String> albums = new ArrayList<>();
        Iterator it = allAlbums.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry currEntry = (HashMap.Entry) it.next();
            Album currAlbum = (Album) currEntry.getValue();

            Log.d("MQ:getEntireAlbumList", "putting the album " + currAlbum.getAlbumTitle()
                    + " into the list");
            albums.add(currAlbum.getAlbumTitle());
        }
        return albums;
    }

    /**
     * Gets the Album object based on the Album name
     *
     * @param albumName name of the Album
     * @return The corresponding Album object
     */
    public Album getAlbum(String albumName) {
        return allAlbums.get(albumName);
    }


    /**
     * Gets the Song object based on the Song name
     * @param songName title of the song
     * @return The corresponding song
     */
    public Song getSong(String songName) {
        return allTracks.get(songName);
    }

}
