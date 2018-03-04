package com.gaparmar.mediaflashback;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by veronica.lin1218 on 3/3/2018.
 */

public class MusicDownloader {
    private Context myContext;
    public MusicDownloader(Context context) {
        myContext = context;

    }


    public void downloadSong(String url, String songTitle) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(url);
        request.setTitle(songTitle);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, songTitle+".mp3");
        DownloadManager dm = (DownloadManager)myContext.getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
    }
}
