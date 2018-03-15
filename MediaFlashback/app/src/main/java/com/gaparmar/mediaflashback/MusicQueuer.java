package com.gaparmar.mediaflashback;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;

import com.gaparmar.mediaflashback.DataStorage.FirebaseHandler;
import com.gaparmar.mediaflashback.DataStorage.FirebaseObserver;
import com.gaparmar.mediaflashback.DataStorage.LogInstance;
import com.gaparmar.mediaflashback.DataStorage.StorageHandler;
import com.gaparmar.mediaflashback.UI.BackgroundService;
import com.gaparmar.mediaflashback.UI.FlashbackActivity;
import com.gaparmar.mediaflashback.WhereAndWhen.AddressRetriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.gaparmar.mediaflashback.UI.MainActivity;

/**
 * Created by veronica.lin1218 on 2/12/2018.
 */

public class MusicQueuer implements FirebaseObserver{

    // Member variables of the class
    protected HashMap<String, Song> allTracks = new HashMap<>();
    protected HashMap<String, Album> allAlbums = new HashMap<>();
    protected HashMap<String, Artist> allArtists = new HashMap<>();
    protected ArrayList<LogInstance> list = new ArrayList<>();

    private final static String UNKNOWN_STRING = "Unknown";
    private final static String UNKNOWN_INT = "0";
    private Context context;
    protected Calendar currDate;
    protected SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
    protected SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.US);

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

    public void readAll() {
        readSongs();
        readAlbums();
        readArtists();
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
            year = "0";
        if (album == null)
            album = Constant.UNKNOWN;
        if (artist == null)
            artist = Constant.UNKNOWN;

        Song song = new Song(fileName);
        song.setMetadata(title, album, artist,year);
        // Put the song object inside the track hashmap
        allTracks.put(fileName, song);
        FirebaseHandler.saveSong(song);
        Log.d("MQ:readSong()", "Just loaded song " + song.getTitle() + " into map");
    }

    /**
     *
     * @param fileName
     */
    public void addDummySong(String fileName, String title, String album, String artist) {

        Song song = new Song(fileName);
        song.setMetadata(title, album, artist,"0");
        // Put the song object inside the track hashmap
        allTracks.put(fileName, song);
        FirebaseHandler.saveSong(song);
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
            currAlbum.addSong(currSong);
            Log.d("readAlbum", "Album " + currAlbum.getAlbumTitle()+  " size now: " + currAlbum.getNumSongs());
        }
    }

    /**
     * Read in albums from song lists
     * Populates the allAlbums hashmap
     */
    public void readArtists() {
        // Iterate through the song map to get all the albums
        Iterator<Map.Entry<String, Album>> it = allAlbums.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Album> currEntry = it.next();
            Album currAlbum = currEntry.getValue();

            String artistName = currAlbum.getArtistName();
            if (artistName == null) artistName = UNKNOWN_STRING;
            Artist currArtist = allArtists.get(artistName);

            // If the album does not exists in the list, we create the new album
            if (currArtist == null) {
                currArtist = new Artist(artistName);
                allArtists.put(artistName, currArtist);
            }
            Log.d("readArtist", "Putting the Album " + currAlbum.getAlbumTitle() +
                    " into Artist "
                    + currArtist.getArtistName());
            currArtist.addAlbum(currAlbum);
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
     * ArrayList of all the Artist names
     *
     * @return Convers the allAlbums hashmap into an ArrayList
     */
    public ArrayList<String> getEntireArtistList() {
        ArrayList<String> artists = new ArrayList<>();
        Iterator it = allArtists.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry currEntry = (HashMap.Entry) it.next();
            Artist currArtist = (Artist) currEntry.getValue();

            Log.d("MQ:getEntireAlbumList", "putting the album " + currArtist.getArtistName()
                    + " into the list");
            artists.add(currArtist.getArtistName());
        }
        return artists;
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

    public Artist getArtist(String artistName) { return allArtists.get(artistName); }

    // TODO:: make a helper function that gets song based on song name

    /**
     * Gets the Song object based on the Song ID
     *
     * @param filePath file path of the song
     * @return The corresponding
     */
    public Song getSong(String filePath) {
        if( allTracks.containsKey( filePath )) return allTracks.get(filePath);
        else return null;
    }

    /**
     * Add song to the list
     * @param song the song object we are storing
     */
    public void setSong( Song song ){
        if( allTracks.containsKey(song.getFileName())){
            return;
        }
        allTracks.put(song.getFileName(), song);
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

        infoBus.add(song.getParentAlbum());
        infoBus.add(song.getArtistName());
        infoBus.add(StorageHandler.getSongLocationString(context, fileName));
        infoBus.add(getTimeOfDay(StorageHandler.getSongTime(context, fileName)));
        infoBus.add(StorageHandler.getSongDay(context, fileName));
        infoBus.add(song.getUserName());
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
        FirebaseHandler.storeAddress(ID, ar.getAddress());
        Log.d("MQ:updateSongInfo", "Storing song address: " + ar.getAddress());
        getSong(ID).setLocation(ar.getLatLon());

        // Store the coordinates
        FirebaseHandler.storeLocation(ID, ar.getLatLon());

        // Store the weekday
        String weekdayStr = dayFormat.format(currDate.getTime());
        getSong(ID).setDayOfWeek(weekdayStr);
        FirebaseHandler.storeDayOfWeek(ID, weekdayStr);

        // Store the time of the day when the song is played
        int timeOfDay = Integer.parseInt(hourFormat.format(currDate.getTime()));
        getSong(ID).setTime(timeOfDay);
        FirebaseHandler.storeTime(ID, timeOfDay);

        // Store the time string
        Long timeStamp = System.currentTimeMillis()/1000;
        getSong(ID).setTimeStamp(timeStamp);
        FirebaseHandler.storeTimeStamp(ID, timeStamp);

        // Storing the song's rating
        StorageHandler.storeSongState(context, ID, getSong(ID).getRate());
        getSong(ID).setTime(timeOfDay);
        StorageHandler.storeSongTime(context, ID, timeOfDay);

        // Storing the song's URL if it is downloaded locally by this user
        if( MainActivity.getMusicDownloader().getUrl(ID)!= null) {
            FirebaseHandler.storeURL(ID, MainActivity.getMusicDownloader().getUrl(ID));
        }

        FirebaseHandler.storeUsername(ID, MainActivity.me.getName());
        FirebaseHandler.saveSongToSongList(getSong(ID));
        FirebaseHandler.logToFirebase(getSong(ID).getTitle(),getSong(ID).getParentAlbum(), getSong(ID).getArtistName(),
                ID, ar.getAddress(), MainActivity.me.getName(),
                weekdayStr, timeStamp, timeOfDay, ar.getLatLon()[0], ar.getLatLon()[1], getSong(ID).getSongURL());
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

    /********************************************** For vibe mode ********************************/

    ArrayList<Song>sortedList = new ArrayList<Song>();
    ArrayList<String> songFromDatabase = new ArrayList<>();

    Song track;
    int totalSongs = getNumSongs();

    public void updateSongList( ArrayList<String> songList ){
        songFromDatabase = songList;
        totalSongs = songFromDatabase.size();
        Log.d("MQ:updateSongList", "We got " + songList.size() + " songs");
        sortedList.clear();
        for(String filename : songFromDatabase){
            FirebaseHandler.getLogList(filename);
        }
    }

    public void updateLogList( String filename, ArrayList<LogInstance> list){
        this.list = list;
        Log.d("MQ:updateLogList", "the size of the list passed in is " + list.size());
        updateProbablity(filename);
    }

    public void updateProbablity( String filename ) {
        track = getSong(filename);
        if( track == null ){
            // When the song is not actually in the list, add it
            track = new Song(filename);
        }
        if( StorageHandler.getSongState(context, filename ) == -1 ){
            // This is when a song is disliked, skip it
            totalSongs--;
            return;
        }
        LogInstance chosenInstance = null;

        // Get all the instances where the song was played

        Log.d("MQ:updateProbability", "song log has a size of " + list.size());

        Log.d("VQ:updateProbability", "Updating song " + track.getTitle());
        int prob = 1;

        final AddressRetriver ar = MainActivity.getAddressRetriver();
        currDate = Calendar.getInstance();

        // Getting the current coordinates
        double currLat = ar.getLatLon()[0];
        double currLon = ar.getLatLon()[1];

        // Get current day
        int day = getIntOfDay(dayFormat.format(currDate.getTime()));

        // Get current hour
        String hour = getTimeOfDay(Integer.parseInt(hourFormat.format(currDate.getTime())));

        // Loop through each instance of the song from the log
        for (int i = 0; i < list.size(); i++){
            Log.d("MQ:updateProbability", "looping the list at " + i);
            int tempProb = 1;
            // Get the current instance
            LogInstance currInstance = list.get(i);
            boolean isInRange = false, isSameDay = false, isSameTime = false;
            // Calculate probability
            isInRange = track.isInRange(new double[]{currInstance.latitude, currInstance.longitude},
                    new double[]{currLat, currLon});
            isSameDay = getIntOfDay(currInstance.dayOfWeek) == day;
            isSameTime = hour.equals(currInstance.timeOfDay);

            if( isInRange ) tempProb++;
            if( isSameDay ) tempProb++;
            if( isSameTime ) tempProb ++;

            if( tempProb > prob ) {
                chosenInstance = currInstance;
                prob = tempProb;
                if( chosenInstance != null ){
                    /*if( getSong(filename) == null ) {
                        MainActivity.getMusicDownloader().downloadData(chosenInstance.url, filename, "mp3");
                        BackgroundService.getMusicQueuer().readSongs();
                        BackgroundService.getMusicQueuer().readAlbums();
                    }*/
                    addDummySong(filename, chosenInstance.title, chosenInstance.album, chosenInstance.artist);
                    Log.d("MQ:updateProbability", "Setting the log info into the song object");
                    StorageHandler.storeSongDay(context, filename, chosenInstance.dayOfWeek);
                    StorageHandler.storeSongLocationString(context, filename, chosenInstance.locationPlayed);
                    StorageHandler.storeSongTime(context, filename, chosenInstance.timeOfDay);
                    getSong(filename).setSongURL(chosenInstance.url);
                    getSong(filename).setUserName(chosenInstance.userName);
                }
            }
        }

        if( getSong(filename).getRate() == Constant.LIKED ) prob ++;

        getSong(filename).setProbability( prob );
        FirebaseHandler.storeProb(filename, prob);
        sortedList.add(getSong(filename));
        Collections.sort(sortedList, new SongCompare());
        Log.d("MQ:updatePrbability", "sortedList has now " + sortedList.size() + " and we have in total " + totalSongs);
        /*if( sortedList.size() == 1 ){

        }
        else {
            FlashbackActivity.flashbackPlayer.addToList();
        }*/
        if( sortedList.size() == totalSongs ){
           // loadPlaylist(FlashbackActivity.flashbackPlayer);
           // FlashbackActivity.flashbackPlayer.loadList();
            // TODO: call the function that updates the track
            for( Song song : sortedList) {
                MainActivity.getMusicDownloader().downloadData(song.getSongURL(), song.getFileName(), "mp3");
            }
            boolean firstTime = true;
            for( int i = 0 ; i < sortedList.size(); i++ ){
                // If the URL exists, which means the song is downloaded
                if( MainActivity.getMusicDownloader().getUrl(sortedList.get(i).getFileName())!= null ){
                    // If this is the first song that's downloaded
                    if( firstTime ){
                        FlashbackActivity.flashbackPlayer.loadNewSong(sortedList.get(i).getFileName());
                        firstTime = false;
                    }
                    // Else just add it to the to-play list
                    else {
                        FlashbackActivity.flashbackPlayer.addToList(sortedList.get(i).getFileName());
                    }
                }
                // Get back to the beginning if we didn't download the whole list
                if( (i == sortedList.size()-1) &&
                        (FlashbackActivity.flashbackPlayer.getSongsToPlay().size() != sortedList.size()) ) {
                    i = 0;
                }
            }
        }
    }


    /**
     * Compile a list of songs to play for vibe mode
     */
    public void makeVibeList(){
        sortedList.clear();
        FirebaseHandler.getSongList();
    }

    public void loadPlaylist( FlashbackPlayer mq ) {
        mq.setPlayList(sortedList);
    }


    private int getIntOfDay(String dayString){
        int day;
        switch( dayString ){
            case "Monday":
                day = 1;
                break;
            case "Tuesday":
                day = 2;
                break;
            case "Wednesday":
                day = 3;
                break;
            case "Thursday":
                day = 4;
                break;
            case "Friday":
                day = 5;
                break;
            case "Saturday":
                day = 6;
                break;
            case "Sunday":
                day = 7;
                break;
            default:
                day = 0;
                break;
        }
        return day;
    }

    /**
     * Get the string that represents the time zone of the day
     * @param time the hour 0-23 to represent the time
     * @return Morning - if the time is in the morning
     *         Afternoon - if the time is in the afternoon
     *         Evening - if the time is at night
     *         Unknown - if this value is never stored
     */
    private String getTimeOfDay (int time ){
        String timeZone;
        if (time >= Constant.MORNING_DIVIDER && time < Constant.NOON_DIVIVER) {
            // 5 AM - 11 AM
            timeZone= Constant.MORNING;
        } else if (time >= Constant.NOON_DIVIVER && time < Constant.EVENING_DIVIDER) {
            // 11 AM - 5 PM
            timeZone= Constant.AFTERNOON;
        } else if (time >= Constant.EVENING_DIVIDER || time < Constant.MORNING_DIVIDER ){
            // 5 PM - 5 AM
            timeZone = Constant.EVENING;
        } else {
            // Unknown
            timeZone = Constant.UNKNOWN;
        }
        return timeZone;
    }

    private static class SongCompare implements Comparator<Song> {
        public int compare(Song s1, Song s2) {
            return s2.getProbability() - s1.getProbability();
        }
    }

    public void updateLocation( String filename, String locationString ){}
    public void updateDayOfWeek( String filename, String dayOfWeek ){}
    public void updateUserName( String filename, String userName ){}
    public void updateCoord( String filename, double lat, double lon ){}
    public void updateTimeStamp( String filename, long timeStamp ){}
    public void updateTime( String filename, long time){}
    public void updateRate(String filename, long rate){}
    public void updateProb( String filename, int prob){}
}
