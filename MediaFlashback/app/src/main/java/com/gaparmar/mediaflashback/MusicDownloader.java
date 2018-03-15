package com.gaparmar.mediaflashback;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.gaparmar.mediaflashback.DataStorage.StorageHandler;
import com.gaparmar.mediaflashback.UI.BackgroundService;
import com.gaparmar.mediaflashback.UI.MainActivity;
import com.google.api.client.googleapis.notifications.StoredChannel;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by veronica.lin1218 on 3/3/2018.
 */

public class MusicDownloader {
    private Context myContext;
    private MusicQueuer mq;
    private DownloadManager dm;
    HashMap<String, String> allUrls = new HashMap<>();
    private final String MEDIA_PATH = Environment.DIRECTORY_DOWNLOADS
            + File.separator + "myDownloads";
    private final String COMPLETE_PATH =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                    + File.separator + "myDownloads";

    public MusicDownloader(Context context) {
        myContext = context;
        mq = BackgroundService.getMusicQueuer();
        dm = (DownloadManager) myContext.getSystemService(DOWNLOAD_SERVICE);

    }

    /**
     * Download song given a URL and it's song name to myDownloads folder (raw folder is RO)
     * @param url string link
     * @param filename Name of file to be downloaded
     *  @param type mp3 (for song) or zip (for album)
     */
    public void downloadData(String url, String filename, String type) {
        final String t = type;
        final String filenameReceiver = filename;
        final String inputURL = url;
        /**
         * Unzip file after download completion
         */
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                if (t.equals("zip")) {
                    File temp = new File(COMPLETE_PATH + File.separator + filenameReceiver + ".zip");
                    Log.d("MD:unzip", "Start unzipping " + temp.getParent());
                    unZip(COMPLETE_PATH + File.separator + filenameReceiver + ".zip",
                            temp.getParent() + File.separator, filenameReceiver +".zip", inputURL);
                } else {
                    addUrl(filenameReceiver + ".mp3", inputURL);
                    Log.d("MD:onComplete", "Finished Downloading " + filenameReceiver);
                    BackgroundService.getMusicQueuer().readAll();
                }
            }
        };
        myContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setDescription(url);
        request.setTitle(filename);

        request.setDestinationInExternalPublicDir(MEDIA_PATH, filename+"." + type);


        // add song to download list
        dm.enqueue(request);


    }

    /**
     * Add url along with filename
     * @param url
     * @param filename
     */
    public void addUrl(String filename, String url) {
        if(StorageHandler.getSongUrl(myContext, filename) == null || !StorageHandler.getSongUrl(myContext, filename).equals(url)) {
            StorageHandler.storeSongUrl(myContext, filename, url);
            Log.d("MD:addUrl", "adding the url " + url);
        }
        else {
            Log.d("MD:addUrl", "already have url " + StorageHandler.getSongUrl(myContext, filename) + " for " + filename);
        }
    }

    /**
     * get url used to download the song with filename
     * @param filename
     * @return the url used to download the song
     */
    public String getUrl(String filename) {
        if (StorageHandler.getSongUrl(myContext, filename) != null) {
            Log.d("MD:getUrl", "getting the url " + StorageHandler.getSongUrl(myContext, filename) + " with filename " + filename);
            return StorageHandler.getSongUrl(myContext, filename);
        }
        return null;
    }

    public void removeUrl(String filename) {
        if (allUrls.get(filename) != null) {
            allUrls.remove(filename);
            Log.d("MD: removeUrl", "File url" + filename + " is removed");
        }
    }

    public void deleteFile(String filename) {
        File file = new File(MEDIA_PATH+File.separator + filename);
        boolean deleted = file.delete();
        if (deleted) {
            Log.d("MD: deleteFile", "File " + filename + " is deleted");
            allUrls.remove(filename);
        }
    }

    /**
     * Unzip a file (album) for its songs
     * Source: https://stackoverflow.com/questions/3382996/how-to-unzip-files-programmatically-in-android
     * @param path Directory to save album in
     * @param targetDir Album Name
     * @param zipName filename
     */
    public boolean unZip(String path, String targetDir, String zipName, String url) {
        InputStream is;
        ZipInputStream zis;
        Log.d("unzip", "in unzip method " + path);

        try {
            String songFileName;
            is = new FileInputStream(path);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                songFileName = ze.getName();
                Log.d("unzip: fileName", songFileName);
                if (ze.isDirectory()) {
                    Log.d("unzip: isDirectory", targetDir+ songFileName);
                    File newFile = new File(targetDir + songFileName);
                    newFile.mkdirs();
                    continue;
                }
                FileOutputStream fout = new FileOutputStream(targetDir + songFileName);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }
                fout.close();
                zis.closeEntry();
                Log.d("MD:Adding URL", "Finished download/ URL for: " + songFileName);
                addUrl(songFileName, url);
            }
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Log.d("MD:unzip", "Finished downloading zip album " + zipName);
        return true;
    }

    /**
     * If URL is added, should be done downloading
     * @param filename
     * @return
     */
    public boolean isDoneDownloading(String filename) {
        return allUrls.get(filename) != null;
    }
}
