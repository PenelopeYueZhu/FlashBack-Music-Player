package com.gaparmar.mediaflashback.UI;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import android.util.Log;
import android.widget.Toast;

import com.gaparmar.mediaflashback.DataStorage.FirebaseHandler;
import com.gaparmar.mediaflashback.DataStorage.FirebaseInfoBus;
import com.gaparmar.mediaflashback.DataStorage.FirebaseObject;
import com.gaparmar.mediaflashback.MusicPlayer;
import com.gaparmar.mediaflashback.MusicQueuer;

public class BackgroundService extends Service {
    private static MusicQueuer musicQueuer;
    private static MusicPlayer musicPlayer;
    private static FirebaseObject firebaseInfoBus;


    // Getters for static variables
    public static MusicPlayer getMusicPlayer(){
        return musicPlayer;
    }
    public static MusicQueuer getMusicQueuer() { return musicQueuer; }
    public static FirebaseObject getFirebaseInfoBus() {
        return firebaseInfoBus;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        Log.d("BS", "Started Service");
        // Initializie the song functions

        Log.d("BS: OnCREATE", "creating mq");
        if (musicQueuer == null) {
            musicQueuer = new MusicQueuer(this);
            musicQueuer.readSongs();
            musicQueuer.readAlbums();
            musicQueuer.readArtists();
        }
        // Initialized the player

        Log.d("BS: OnCREATE", "creating mp");
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer(this, musicQueuer);
        }

        if( firebaseInfoBus == null ){
            firebaseInfoBus = new FirebaseInfoBus();
            firebaseInfoBus.register(MainActivity.getUITracker());
            Log.d("Main:debugging", "trakcer called");
            firebaseInfoBus.register(musicQueuer);
        }

        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.d("BS", "Started Service");
        // Initializie the song functions

        Log.d("BS: OnCREATE", "creating mq");
        musicQueuer = new MusicQueuer(this);
        musicQueuer.readSongs();
        musicQueuer.readAlbums();
        musicQueuer.readArtists();

        // Initialized the player

        Log.d("BS: OnCREATE", "creating mp");
        musicPlayer = new MusicPlayer(this, musicQueuer);
    }

    public void initialize() {
        Log.d("BS: initialize", "creating mq");
        musicQueuer = new MusicQueuer(this);
        musicQueuer.readSongs();
        musicQueuer.readAlbums();
        musicQueuer.readArtists();

        // Initialized the player

        Log.d("BS: initialize", "creating mp");
        musicPlayer = new MusicPlayer(this, musicQueuer);
    }



    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
