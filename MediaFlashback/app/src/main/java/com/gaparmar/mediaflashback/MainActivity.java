package com.gaparmar.mediaflashback;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates and handles events relating to the regular mode UI screen
 */
public class MainActivity extends AppCompatActivity {

    private static MusicPlayer musicPlayer;
    private static MusicQueuer musicQueuer;

    // Objects for location
    private static MusicDownloader musicDownloader;
    private FusedLocationProviderClient mFusedLocationClient;
    private Handler addressHandler;
    private static AddressRetriver addressRetriver;
    private static UINormal tracker;

    // Objects for info updates
    private static FirebaseHandler firebaseHandler;
    private static FirebaseObject firebaseInfoBus;

    private static ArrayList<String> stoppedInfo = new ArrayList<>();
    public static boolean isPlaying;
    private static boolean browsing = false;

    public static Map<String, Integer> weekDays;

    // Getters for static variables
    public static MusicPlayer getMusicPlayer(){
        return musicPlayer;
    }
    public static MusicQueuer getMusicQueuer() { return musicQueuer; }

    public static FirebaseObject getFirebaseInfoBus() { return firebaseInfoBus; }
    public static AddressRetriver getAddressRetriver() {
        return addressRetriver;
    }
    public static FirebaseHandler getFirebaseHandler() { return firebaseHandler;}

    public static MusicDownloader getMusicDownloader() {
        return musicDownloader;
    }

    public static UINormal getUITracker() {
        return tracker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, 666);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Stores the days of the week
        weekDays = new HashMap<String, Integer>();
        weekDays.put("Monday", 1);
        weekDays.put("Tuesday", 2);
        weekDays.put("Wednesday", 3);
        weekDays.put("Thursday", 4);
        weekDays.put("Friday", 5);
        weekDays.put("Saturday", 6);
        weekDays.put("Sunday", 7);


        // Initialize UI
        tracker = new UINormal(this);
        tracker.setButtonFunctions();

        // Initializie the song functions
        if (musicQueuer == null) {
            musicQueuer = new MusicQueuer(this);
            musicQueuer.readSongs();
            musicQueuer.readAlbums();
            musicQueuer.readArtists();
        }

        // Initialized the player
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer(this, musicQueuer);
        }

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d("test1","ins");
            return;
        }else {
            Log.d("test2", "outs");
        }*/

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                MainActivity.getAddressRetriver().setLocation(location);
                Log.d("FBLgetting location", "Setting the location to address retriver");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        /// Register the listener with the Location Manager to receive location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
        } catch( SecurityException e) {

        }

        // Initialize the music downloader
        if (musicDownloader == null) {
            musicDownloader = new MusicDownloader(this);
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("MA:mFusedLocationClient", "Got the location");
                                addressRetriver.setLocation(location);
                            }
                        }
                    });
        } catch (SecurityException e) {
            System.out.println("Security Alert");
        }

        // Initialize the addresss retriver
        if (addressRetriver == null) {
            addressHandler = new Handler();
            addressRetriver = new AddressRetriver(this, addressHandler);
        }

        // Unless there is a song playing when we get back to normal mode, hide the button
        if (!musicPlayer.wasPlayingSong()) {
            tracker.setButtonsPausing();
        } else {
            tracker.setButtonsPlaying();
        }

        firebaseInfoBus = new FirebaseInfoBus();
        firebaseInfoBus.register(tracker);


        //mPlayer.loadMedia(R.raw.replay);
        Button launchFlashbackActivity = findViewById(R.id.flashback_button);
        ImageButton playButton =  findViewById(R.id.play_button);
        Button browseBtn = findViewById(R.id.browse_button);


        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLibraryActivity();
                finish();
            }
        });

        launchFlashbackActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = musicPlayer.isPlaying();
                StorageHandler.storeLastMode(MainActivity.this, 1);
                launchFlashbackActivity();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        if(StorageHandler.getLastMode(this) == 1){
            //launchFlashbackActivity();
        }
    }

    /**
     * Requests location permission from the user
     * @param permission The permission being requested
     * @param requestCode The code for the permissions
     */
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Checks if the user requires permissions
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                // If they do, ask for it
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        }
    }

    /**
     * Launches flashback mode activity
     */
    public void launchFlashbackActivity(){
        Log.d("MainActivity", "Launching Flashback mode");
        //input = (EditText)findViewById(R.id.in_time) ;
        Intent intent = new Intent(this, FlashbackActivity.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    /**
     * Launches the browse music screen
     */
    public void launchLibraryActivity() {
        Log.d("MainActivity", "Launching library");
        Intent intent = new Intent(this, LibraryActivity.class);
        setResult(Activity.RESULT_OK, intent);
        browsing = true;
        startActivity(intent);
    }

    /**
     * Launches the download activity screen
     */
    public void launchDownloadActivity(){
        Log.d("MainActivity", "Launching downloads");
        Intent intent = new Intent(this, DownloadHandlerActivity.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    /**
     * Pauses the song when the activity is paused
     */
    @Override
    public void onPause(){
        super.onPause();

        // Checks if the user is not browsing songs, but is playing one
        if(!browsing) {
            if (isPlaying) {
                // If they are, the song pauses
                stoppedInfo = musicPlayer.stopPlaying();
                musicPlayer.pauseSong();
                isPlaying = true;
            } else {
                isPlaying = false;
            }
        }else if (browsing && isPlaying){
            tracker.setButtonsPlaying();
        }
    }

    /**
     * Resumes the song when the activity is resumed
     */
    @Override
    public void onResume() {
        super.onResume();
        // Checks if the user is playing a song, but now browsing
        if (isPlaying) {
            if(!browsing) {
                // Updates buttons accordingly.
                Log.i("Main:onResume","song playing, you are not browsing");
                tracker.setButtonsPausing();
                tracker.updateTrackInfo();
                isPlaying = true;
            }else{
                // Updates the buttons differently if the user is browsing
                Log.i("Main:onResume", "updating the buttons");
                tracker.updateToggle();
                tracker.setButtonsPlaying();
                tracker.updateTrackInfo();
            }
        }
        browsing = false;
    }

    /**
     * Starts the download activity
     * @param view
     */
    public void OnLaunchDownloadClick(View view){
        launchDownloadActivity();
    }
}
