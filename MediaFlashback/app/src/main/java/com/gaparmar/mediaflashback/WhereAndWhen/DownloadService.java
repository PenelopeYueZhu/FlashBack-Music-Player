package com.gaparmar.mediaflashback.WhereAndWhen;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import com.gaparmar.mediaflashback.MusicDownloader;
import com.gaparmar.mediaflashback.MusicPlayer;
import com.gaparmar.mediaflashback.MusicQueuer;

public class DownloadService extends Service {
    private static MusicDownloader md;

    public static MusicDownloader getMusicDownloader() { return md; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        Log.d("BS", "Started Download Service");

        Log.d("BS: OnCREATE", "creating mq");
        if (md == null) {
            md = new MusicDownloader(this);
        }

        Toast.makeText(this, "Download Service started by user.", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.d("DS: onCreate", "Creating Music Downloader");
        md = new MusicDownloader(this);
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Download Service destroyed by user.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}