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
 * Created by veronica.lin1218 on 2/12/2018.
 */

public class MusicQueuer {

    // Member variables of the class
    protected HashMap<String, Song> allTracks = new HashMap<>();
    protected HashMap<String, Album> allAlbums = new HashMap<>();

    private final static String UNKNOWN_STRING = "Unknown";
    private final static String UNKNOWN_INT = "0";
    private Context context;
    // final private String PACKAGE = "com.gaparmar.mediaflashback";
    final private String RES_FOLDER = "raw";
    //final private String URI_PREFIX = "android.resource://com.gaparmar.mediaflashback/raw/";
    protected Calendar currDate;
    protected SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
    protected SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.US);
    protected SimpleDateFormat fullTimeFormat = new SimpleDateFormat("HH:mm 'at' MM/dd/YY");

    // Default constructor
    public MusicQueuer () {}
    /**
     * The constructor of the MusicQueuer Object
     *
     * @param context the activity context reference
     */
    public MusicQueuer(Context context) {
        this.context = context;
    }

    /**
     * Read all files and directories
     */
    public void readSongs() {
        File dir = new File(Constant.MEDIA_PATH);
        File[] fileList = dir.listFiles();
        Log.i("MQ", "Reading all files");
        if (fileList != null) {
            for (File f : fileList) {
                Log.i("MQ: Reading file ", f.getName());
                if (f.isDirectory()) {
                    scanDirectory(f);
                } else {
                    addSong(f.getPath(),f.getName() );
                }
            }
        } else {
            // if directory does not exist, create a new one
            File newDir = new File(Environment.getExternalStorageDirectory() + File.separator + "myDownloads");
            newDir.mkdirs();
        }
    }

    /**
     * Scan directory for songs
     * @param dir the directory where we want to scan
     */
    public void scanDirectory(File dir) {
        if (dir != null) {
            File[] fileList = dir.listFiles();
            if (fileList != null) {
                for (File f : fileList) {
                    addSong(f.getPath(), f.getName());
                }
            }
        }
    }

    public void addSong(String songPath, String fileName) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        String title, year, album, artist;

        try {

            retriever.setDataSource(context, Uri.parse(songPath));
            Log.d("MQ:readSong", "Retrieving the song's metadata");
            title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            year = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
            album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);


        } catch (Exception e) {
            //e.printStackTrace();
            title = UNKNOWN_STRING;
            year = UNKNOWN_INT;
            album = UNKNOWN_STRING;
            artist = UNKNOWN_STRING;
        }
        // If any field is null, set it to default values
        if (title == null)
            title = Constant.UNKNOWN;
        if (year == null)
            year = Constant.UNKNOWN;
        if (album == null)
            album = Constant.UNKNOWN;
        if (artist == null)
            artist = Constant.UNKNOWN;

        Song song = new Song(fileName);
        System.err.println("In added song, we got the old filename as " + fileName + " and new filename as " + Song.reformatFileName(fileName));
        song.setMetadata(title, album, artist,year);
        // Put the song object inside the track hashmap
        allTracks.put(fileName, song);
        /*FirebaseHandler.saveSong(song);*/ //TODO needs refactoring
        Log.d("MQ:readSong()", "Just loaded song " + song.getTitle() + " into map");
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

    // TODO:: Add null checks first to see if the album exists

    /**
     * Gets the Album object based on the Album name
     *
     * @param albumName name of the Album
     * @return The corresponding Album object
     */
    public Album getAlbum(String albumName) {
        return allAlbums.get(albumName);
    }

    // TODO:: make a helper function that gets song based on song name

    /**
     * Gets the Song object based on the Song ID
     *
     * @param filePath file path of the song
     * @return The corresponding
     */
    public Song getSong(String filePath) {
        return allTracks.get(filePath);
    }

    /**
     * Get song information
     *
     * @param fileName File name of the song
     * @return An ArrayList of strings that contains the song's information
     */
    public ArrayList<String> getSongInfo(String fileName) {
        ArrayList<String> infoBus = new ArrayList<String>();
        Song song = this.getSong(fileName);

        infoBus.add(song.getTitle());

        infoBus.add(StorageHandler.getSongDay(context, fileName));
        infoBus.add(StorageHandler.getSongBigTimeStamp(context, fileName));
        infoBus.add(StorageHandler.getSongLocationString(context, fileName));
        infoBus.add(song.getArtistName());
        infoBus.add(song.getParentAlbum());
        return infoBus;
    }

    /**
     * upload song information
     * @param ID the id of the song we are storing information for
     */
    public void updateTrackInfo( String ID ){
        final AddressRetriver ar = MainActivity.getAddressRetriver();
        currDate = Calendar.getInstance();

        // Store address string
        //FirebaseHandler.storeAddress(ID, ar.getAddress()); TODO:: needs refactoring
        Log.d("MQ:updateSongInfo", "Storing song address: " + ar.getAddress());
        getSong(ID).setLocation(ar.getLatLon());

        // Store the coordinates
        //FirebaseHandler.storeLocation(ID, ar.getLatLon()); TODO:: needs refactoring

        // Store the weekday
        String weekdayStr = dayFormat.format(currDate.getTime());
        getSong(ID).setDayOfWeek(weekdayStr);
        //FirebaseHandler.storeDayOfWeek(ID, weekdayStr); TODO:: needs refactoring

        // Store the time of the day when the song is played
        int timeOfDay = Integer.parseInt(hourFormat.format(currDate.getTime()));
        getSong(ID).setTime(timeOfDay);
        //FirebaseHandler.storeTime(ID, timeOfDay); TODO:: needs refactoring

        // Store the time string
        Long timeStamp = System.currentTimeMillis()/1000;
        getSong(ID).setTimeStamp(timeStamp);
        //FirebaseHandler.storeTimeStamp(ID, timeStamp); TODO:: needs refactoring

        StorageHandler.storeSongState(context, ID, getSong(ID).getRate());
        getSong(ID).setTime(timeOfDay);
        StorageHandler.storeSongTime(context, ID, timeOfDay);

    }

    /**
     * Get the number of songs queued
     *
     * @return the total number of currently stored songs
     */
    public int getNumSongs() {
        return allTracks.size();
    }

    /**
     * Get the number of albums queued
     *
     * @return the total number of currently stored albums
     */
    public int getNumAlbums() {
        return allAlbums.size();
    }

    public void copyRawToSD() {

    }
}
